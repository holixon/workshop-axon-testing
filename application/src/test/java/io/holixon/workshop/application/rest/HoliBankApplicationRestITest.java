package io.holixon.workshop.application.rest;

import com.tngtech.jgiven.integration.spring.EnableJGiven;
import io.holixon.workshop.application.HoliBankApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {
  HoliBankApplication.class
})
@AutoConfigureMockMvc
@EnableJGiven
@Disabled
public class HoliBankApplicationRestITest extends AbstractTestContainerIntegrationTestBase<HoliBankApplicationActionStage, HoliBankApplicationAssertStage> {

  private String sourceAccountId;
  private String targetAccountId;

  @BeforeEach
  public void initializeAccountId() {
    sourceAccountId = UUID.randomUUID().toString();
    targetAccountId = UUID.randomUUID().toString();
  }

  @Test
  void bank_account_can_be_created() throws Exception {
    // GIVEN nothing happened
    // WHEN account with id and balance is created
    // THEN balance for account can be queried
  }

  @Test
  void money_can_be_withdrawn() throws Exception {
    // GIVEN an account with balance
    // WHEN we withdraw an amount
    // THEN the balance is decreased by amount
  }

  @Test
  void money_can_be_transferred() throws Exception {
    // GIVEN two accounts
    // WHEN we transfer amount from 1st to 2nd
    // THEN 1st balance is decreased by amount
    // AND 2nd balance is increased by amount
  }

}
