package io.holixon.workshop.context.bankaccount.query;


import com.tngtech.jgiven.junit5.DualScenarioTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class CurrentBalanceProjectionTest extends DualScenarioTest<CurrentBalanceActionStage, CurrentBalanceAssertStage> {

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

    // this is how this would look like
    // without jgiven ...
    //
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

}
