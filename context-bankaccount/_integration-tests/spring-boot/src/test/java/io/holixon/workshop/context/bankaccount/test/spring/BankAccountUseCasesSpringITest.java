package io.holixon.workshop.context.bankaccount.test.spring;

import com.tngtech.jgiven.integration.spring.EnableJGiven;
import com.tngtech.jgiven.integration.spring.junit5.DualSpringScenarioTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest(classes = TestConfiguration.class)
@EnableJGiven
@Disabled
public class BankAccountUseCasesSpringITest extends DualSpringScenarioTest<BankAccountActionStage, BankAccountAssertStage> {

  @Test
  void create_account_and_query_balance() {
    // GIVEN: create account with id and balance
    // THEN: query the balance of this account
  }

  @Test
  void withdraw_amount() {
    // GIVEN account with id and balance
    // WHEN we withdraw an amount
    // THEN the balance is decreased by amount
  }


  @Test
  void transfer_10_from_source_to_target() {
    // GIVEN two accounts
    // WHEN we transfer amount from 1st to 2nd
    // THEN 1st balance is decreased
  }

  private String accountId() {
    return UUID.randomUUID().toString();
  }

}
