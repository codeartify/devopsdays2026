# Exercises

## 1 Send a command
* send a command to activate a membership
  * the command should contain:
    * the customer ID (value object, not null)
    * the agreed upon plan terms containing
      * the plan ID
      * plan duration in months
      * plan price
    * the customer eligibility snapshot
      * a customers date of birth 
      * if the customer was signed by a guardian if they were underage
  * ensure the customer exists (use the local customer cache)
  * ensure the customer does not already have an active membership (use the local membership projection)
  * get the current plan terms for the plan ID
    * the plan repository is owned by another bounded context in the same application: don't directly access it (could be an API call in the future)
    * instead, implement an adapter that uses that repository and create a value object only for the plan details needed in the membership
  * use Axon's commandGateway.sendAndWait(...) to send the command

## Add the 

## Projections
* Growth curve: for each month and year, number of memberships per month activated
* Send a bonus email for all customers that have ever paused their memberships
* Categorise members for marketing purposes into 
  * Motivated (always active, never paused or suspended)
  * Should be motivated (paused)
  * Suspicious (> 1 suspended)
  * Need habit forming (paused > 1)
  * Those who canceled their membership within 3 months of activation
* Find month where customers cancel their membership quickly

