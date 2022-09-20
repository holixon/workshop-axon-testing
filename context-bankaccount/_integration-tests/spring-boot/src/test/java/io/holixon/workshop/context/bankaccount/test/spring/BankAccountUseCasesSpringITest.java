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

  }

  @Test
  void withdraw_amount() {

  }

  @Test
  void deposit_amount() {

  }

  @Test
  void transfer_10_from_source_to_target() {

  }

  @Test
  void transfer_fails_when_target_would_exceed_maxBalance() {

  }

  private String accountId() {
    return UUID.randomUUID().toString();
  }

}
