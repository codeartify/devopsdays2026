package com.codeartify.managingcustomers

import com.codeartify.managingcustomers.integration.CustomerPublisher
import com.codeartify.managingcustomers.integration.CustomerRegisteredIntegrationEventV1
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class CustomerService(
    private val customerRepository: CustomerRepository,
    private val customerPublisher: CustomerPublisher
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun create(request: RegisterCustomerRequest): String {
        require(request.name.isNotBlank()) { "Name must not be blank" }
        require(request.email.isNotBlank()) { "Email must not be blank" }

        val customerId = UUID.randomUUID().toString()
        val customer = CustomerEntity(
            customerId,
            request.name.trim(),
            request.email.trim(),
            request.dateOfBirth.toString()
        )
        customerRepository.save(customer)
        publishCustomerRegistered(customer)
        return customerId
    }

    @Transactional(readOnly = true)
    fun getById(customerId: String): CustomerResponse =
        customerRepository.findById(customerId)
            .map(::toResponse)
            .orElseThrow { IllegalArgumentException("Customer with ID $customerId not found") }

    @Transactional(readOnly = true)
    fun getAll(): List<CustomerResponse> =
        customerRepository.findAll().map(::toResponse)

    @Transactional
    fun update(customerId: String, request: UpdateCustomerRequest): CustomerResponse {
        require(request.name.isNotBlank()) { "Name must not be blank" }
        require(request.email.isNotBlank()) { "Email must not be blank" }

        val customer = customerRepository.findById(customerId)
            .orElseThrow { IllegalArgumentException("Customer with ID $customerId not found") }

        customer.name = request.name.trim()
        customer.email = request.email.trim()
        customer.dateOfBirth = request.dateOfBirth.toString()

        return toResponse(customerRepository.save(customer))
    }

    @Transactional
    fun delete(customerId: String) {
        val customer = customerRepository.findById(customerId)
            .orElseThrow { IllegalArgumentException("Customer with ID $customerId not found") }
        customerRepository.delete(customer)
    }

    private fun toResponse(entity: CustomerEntity) = CustomerResponse(
        id = entity.id,
        name = entity.name,
        email = entity.email,
        dateOfBirth = entity.dateOfBirth
    )

    private fun publishCustomerRegistered(customer: CustomerEntity) {
        val integrationEvent = CustomerRegisteredIntegrationEventV1(
            customerId = customer.id,
            name = customer.name,
            email = customer.email,
            dateOfBirth = java.time.LocalDate.parse(customer.dateOfBirth)
        )
        val envelope = mapOf(
            "type" to "CustomerRegistered",
            "version" to 1,
            "payload" to integrationEvent
        )
        log.info("Publishing event: {}", envelope)
        customerPublisher.publish(customer.id, envelope)
    }
}
