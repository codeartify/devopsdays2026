package com.codeartify.membership

import com.codeartify.membership.customer_cache.CustomerCacheRepository
import com.codeartify.membership.notifying.EmailSender
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.opentest4j.TestAbortedException
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Primary
import org.springframework.boot.web.server.servlet.context.ServletWebServerApplicationContext
import org.springframework.web.client.RestClient
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.kafka.KafkaContainer
import org.testcontainers.lifecycle.Startables
import org.testcontainers.postgresql.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName
import java.time.LocalDate
import java.util.concurrent.CopyOnWriteArrayList
import java.util.stream.Stream

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomerMembershipBillingIntegrationTest {

    private val identityDb = PostgreSQLContainer(DockerImageName.parse("postgres:18")).apply {
        withDatabaseName("identity")
        withUsername("test")
        withPassword("test")
    }

    private val membershipDb = PostgreSQLContainer(DockerImageName.parse("postgres:18")).apply {
        withDatabaseName("fitness-management")
        withUsername("test")
        withPassword("test")
    }

    private val kafka = KafkaContainer(DockerImageName.parse("apache/kafka:3.9.0"))

    private lateinit var identityContext: ConfigurableApplicationContext
    private lateinit var membershipContext: ConfigurableApplicationContext
    private lateinit var identityClient: RestClient
    private lateinit var membershipClient: RestClient

    @BeforeAll
    fun startApplications() {
        try {
            Startables.deepStart(Stream.of(identityDb, membershipDb, kafka)).join()
        } catch (exception: Exception) {
            abortIntegrationTest(exception)
        }

        identityContext = SpringApplicationBuilder(IdentityTestApplication::class.java)
            .web(WebApplicationType.SERVLET)
            .properties(
                mapOf(
                    "server.port" to "0",
                    "spring.datasource.url" to identityDb.jdbcUrl,
                    "spring.datasource.username" to identityDb.username,
                    "spring.datasource.password" to identityDb.password,
                    "spring.jpa.hibernate.ddl-auto" to "create-drop",
                    "spring.kafka.bootstrap-servers" to kafka.bootstrapServers
                )
            )
            .run()

        membershipContext = SpringApplicationBuilder(MembershipApplication::class.java, MembershipTestOverrides::class.java)
            .web(WebApplicationType.SERVLET)
            .properties(
                mapOf(
                    "server.port" to "0",
                    "spring.datasource.url" to membershipDb.jdbcUrl,
                    "spring.datasource.username" to membershipDb.username,
                    "spring.datasource.password" to membershipDb.password,
                    "spring.jpa.hibernate.ddl-auto" to "create-drop",
                    "spring.kafka.bootstrap-servers" to kafka.bootstrapServers,
                    "spring.kafka.consumer.group-id" to "membership-it"
                )
            )
            .run()

        val identityPort = (identityContext as ServletWebServerApplicationContext).webServer!!.port
        val membershipPort = (membershipContext as ServletWebServerApplicationContext).webServer!!.port

        identityClient = RestClient.builder().baseUrl("http://localhost:$identityPort").build()
        membershipClient = RestClient.builder().baseUrl("http://localhost:$membershipPort").build()
    }

    @AfterAll
    fun stopApplications() {
        if (::identityContext.isInitialized) {
            identityContext.close()
        }
        if (::membershipContext.isInitialized) {
            membershipContext.close()
        }
        kafka.stop()
        membershipDb.stop()
        identityDb.stop()
    }

    private fun abortIntegrationTest(exception: Exception): Nothing {
        val rootCause = generateSequence(exception as Throwable?) { it.cause }
            .last()
            .message
            ?.takeIf { it.isNotBlank() }
        val message = buildString {
            append("Docker is required for ")
            append(this@CustomerMembershipBillingIntegrationTest::class.simpleName)
            append(" but no valid Docker environment was available")
            if (rootCause != null) {
                append(": ")
                append(rootCause)
            }
        }
        throw TestAbortedException(message, exception)
    }

    @Test
    fun `creates customer and plan, activates membership, and sends invoice email`() {
        val customerId = identityClient.post()
            .uri("/customers")
            .body(
                mapOf(
                    "name" to "Oliver Zihler",
                    "email" to "info@codeartify.com",
                    "dateOfBirth" to "1986-08-13"
                )
            )
            .retrieve()
            .body(String::class.java)!!

        awaitUntil("customer cached in membership") {
            membershipContext.getBean(CustomerCacheRepository::class.java).findById(customerId).isPresent
        }

        val planId = membershipClient.post()
            .uri("/plans")
            .body(
                mapOf(
                    "title" to "12 Months",
                    "description" to "Annual membership plan.",
                    "price" to 999,
                    "durationInMonths" to 12
                )
            )
            .retrieve()
            .body(String::class.java)!!

        membershipClient.post()
            .uri("/memberships/activate")
            .body(
                mapOf(
                    "customerId" to customerId,
                    "planId" to planId,
                    "signedByGuardian" to false
                )
            )
            .retrieve()
            .body(String::class.java)

        val emailSender = membershipContext.getBean(CapturingEmailSender::class.java)
        awaitUntil("invoice email sent") { emailSender.sentEmails.isNotEmpty() }

        val sentEmail = emailSender.sentEmails.single()
        assertEquals("info@codeartify.com", sentEmail.to)
        assertEquals(999, sentEmail.amount)
        assertTrue(sentEmail.invoiceId.isNotBlank())
        assertEquals(LocalDate.now().plusDays(30), sentEmail.dueDate)
        assertTrue(sentEmail.message.contains("To: info@codeartify.com"))
        assertTrue(sentEmail.message.contains("Subject: Your Membership Invoice ${sentEmail.invoiceId}"))
        assertTrue(sentEmail.message.contains("Amount Due: CHF 999"))
        assertTrue(sentEmail.message.contains("Attachment: invoice-${sentEmail.invoiceId}.pdf"))
    }

    private fun awaitUntil(description: String, condition: () -> Boolean) {
        val deadline = System.currentTimeMillis() + 15_000
        while (System.currentTimeMillis() < deadline) {
            if (condition()) {
                return
            }
            Thread.sleep(250)
        }
        error("Timed out waiting for $description")
    }

    @SpringBootConfiguration
    @EnableAutoConfiguration
    @ComponentScan(basePackages = ["com.codeartify.managingcustomers"])
    class IdentityTestApplication

    @TestConfiguration
    class MembershipTestOverrides {
        @Bean
        @Primary
        fun emailSender(): CapturingEmailSender = CapturingEmailSender()
    }

    class CapturingEmailSender : EmailSender {
        val sentEmails: MutableList<SentEmail> = CopyOnWriteArrayList()

        override fun send(to: String, invoiceId: String, amount: Int, dueDate: LocalDate) {
            sentEmails.add(
                SentEmail(
                    to = to,
                    invoiceId = invoiceId,
                    amount = amount,
                    dueDate = dueDate,
                    message = renderEmail(to, invoiceId, amount, dueDate)
                )
            )
        }

        private fun renderEmail(to: String, invoiceId: String, amount: Int, dueDate: LocalDate): String =
            """
            |To: $to
            |From: billing@codeartify.com
            |Subject: Your Membership Invoice $invoiceId
            |
            |Dear customer,
            |
            |Thank you for your membership.
            |
            |Please find your invoice details below:
            |Invoice ID: $invoiceId
            |Amount Due: CHF $amount
            |Due Date: $dueDate
            |
            |Attachment: invoice-$invoiceId.pdf
            |
            |Kind regards,
            |Codeartify Billing
            """.trimMargin()
    }

    data class SentEmail(
        val to: String,
        val invoiceId: String,
        val amount: Int,
        val dueDate: LocalDate,
        val message: String
    )
}
