package io.holixon.workshop.context.bankaccount.query;


import com.tngtech.jgiven.junit5.DualScenarioTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
class CurrentBalanceProjectionTest extends DualScenarioTest<CurrentBalanceActionStage, CurrentBalanceAssertStage> {

  @Test
  void can_create_bankAccount() {
    // GIVEN: nothing happened
    // WHEN: bank account created with id and initial balance
    // THEN query for current balance for id returns balance
  }

  @Test
  void withdraw_decreases_balance() {
    // GIVEN: account with id and balance
    // WHEN amount is withdrawn from account
    // THEN query for current balance for id returns balance - amount
  }

  @Test
  void transfer_decreases_sourceAccount_and_increases_targetAccount() {
    // GIVEN two accounts with id and balance
    // WHEN we transfer amount from 1st to 2nd
    // THEN query for 1st account returns balance - amount
    // AND query for 2nd account returns balance + account
  }

}
