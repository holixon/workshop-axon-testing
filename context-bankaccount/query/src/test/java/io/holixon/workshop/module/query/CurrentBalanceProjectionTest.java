package io.holixon.workshop.module.query;


import static org.assertj.core.api.Assertions.assertThat;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.As;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.junit5.DualScenarioTest;
import io.holixon.workshop.api.event.BankAccountCreatedEvent;
import io.holixon.workshop.api.event.atm.MoneyDepositedEvent;
import io.holixon.workshop.api.event.atm.MoneyWithdrawnEvent;
import io.holixon.workshop.api.event.transfer.MoneyTransferredEvent;
import io.holixon.workshop.api.query.CurrentBalanceQuery;
import io.holixon.workshop.api.query.CurrentBalanceResponse;
import io.holixon.workshop.module.query.CurrentBalanceProjectionTest.GivenWhenStage;
import io.holixon.workshop.module.query.CurrentBalanceProjectionTest.ThenStage;
import java.util.UUID;
import org.junit.jupiter.api.Test;

@SuppressWarnings({"UNUSED_RETURN_VALUE"})
class CurrentBalanceProjectionTest extends DualScenarioTest<GivenWhenStage, ThenStage> {

  @Test
  void can_create_bankAccount() {
    given()
      .bankAccount_created("1", 100);

    then()
      .expect_currentBalance_for_account("1", 100);
  }

  @Test
  void withdraw_decreases_balance() {
    given()
      .bankAccount_created("1", 100);

    when()
      .withdraw_amount_from_account("1", 60);

    then()
      .expect_currentBalance_for_account("1", 40);
// TODO: leave this, later use as example to show benefits of using jgiven.
//    projection.on(new BankAccountCreatedEvent("1", 100));
//    projection.on(new MoneyWithdrawnEvent("1", 40));
//
//
//    assertThat(projection.query(new CurrentBalanceQuery("1"))).hasValue(new CurrentBalanceResponse("1", 60));
  }

  @Test
  void deposit_increases_balance() {
    given()
      .bankAccount_created("1", 100);

    when()
      .deposit_amount_on_account("1", 60);

    then()
      .expect_currentBalance_for_account("1", 160);
  }

  @Test
  void transfer_decreases_sourceAccount_and_increases_targetAccount() {
    given()
      .bankAccount_created("1", 100)
      .and()
      .bankAccount_created("2", 200)
    ;

    when()
      .transfer_money("1", 25, "2");

    then()
      .expect_currentBalance_for_account("1", 75)
      .and()
      .expect_currentBalance_for_account("2", 225)
    ;
  }

  public static class GivenWhenStage extends Stage<GivenWhenStage> {

    @ProvidedScenarioState
    private final CurrentBalanceProjection projection = new CurrentBalanceProjection();

    @As("bankAccount[$]:  created with initialBalance=$")
    public GivenWhenStage bankAccount_created(String accountId, int initialBalance) {
      projection.on(new BankAccountCreatedEvent(accountId, initialBalance));
      return self();
    }

    @As("bankAccount[$]:  withdrawn=$")
    public GivenWhenStage withdraw_amount_from_account(String accountId, int amount) {
      projection.on(new MoneyWithdrawnEvent(accountId, amount));

      return self();
    }

    @As("bankAccount[$]:  deposited=$")
    public GivenWhenStage deposit_amount_on_account(String accountId, int amount) {
      projection.on(new MoneyDepositedEvent(accountId, amount));

      return self();
    }

    @As("bankAccount[$]:  transfer amount=$ to bankAccount[$]")
    public GivenWhenStage transfer_money(String sourceAccountId, int amount, String targetAccountId) {
      projection.on(new MoneyTransferredEvent(UUID.randomUUID().toString(), sourceAccountId, targetAccountId, amount));

      return self();
    }
  }

  public static class ThenStage extends Stage<ThenStage> {

    @ExpectedScenarioState
    private CurrentBalanceProjection projection;

    @As("bankAccount[$]:  expect currentBalance=$")
    public ThenStage expect_currentBalance_for_account(String accountId, int expectedBalance) {
      var result = projection.query(new CurrentBalanceQuery(accountId));

      assertThat(result).hasValue(new CurrentBalanceResponse(accountId, expectedBalance));

      return self();
    }
  }
}
