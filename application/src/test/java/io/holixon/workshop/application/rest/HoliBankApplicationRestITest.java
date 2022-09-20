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

    given()
      .noPriorActivity();

    when()
      .account_is_created(sourceAccountId, 100);

    then()
      .account_with_id_has_balance(sourceAccountId, 100);
  }

  @Test
  void money_can_be_withdrawn() throws Exception {
    given()
      .account_is_created(sourceAccountId, 100);

    when()
      .money_is_withdrawn(sourceAccountId, 20);

    then()
      .account_with_id_has_balance(sourceAccountId, 80);
  }

  @Test
  void money_can_be_deposited() throws Exception {
    given()
      .account_is_created(sourceAccountId, 100);

    when()
      .money_is_deposited(sourceAccountId, 20);

    then()
      .account_with_id_has_balance(sourceAccountId, 120);
  }

  @Test
  void money_can_be_transferred() throws Exception {
    given()
      .account_is_created(sourceAccountId, 100)
      .and()
      .account_is_created(targetAccountId, 100);

    when()
      .money_is_transferred(sourceAccountId, targetAccountId, 20);

    then()
      .account_with_id_has_balance(sourceAccountId, 80)
      .and()
      .account_with_id_has_balance(targetAccountId, 120);
  }

}
