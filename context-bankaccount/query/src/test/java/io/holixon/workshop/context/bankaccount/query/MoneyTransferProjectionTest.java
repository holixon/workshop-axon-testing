package io.holixon.workshop.context.bankaccount.query;

import com.tngtech.jgiven.junit5.DualScenarioTest;
import io.holixon.workshop.context.bankaccount.api.event.transfer.MoneyTransferCancelledEvent;
import io.holixon.workshop.context.bankaccount.api.event.transfer.MoneyTransferCompletedEvent;
import io.holixon.workshop.context.bankaccount.api.event.transfer.MoneyTransferRequestedEvent;
import io.holixon.workshop.context.bankaccount.api.query.MoneyTransfer;
import io.holixon.workshop.context.bankaccount.api.query.MoneyTransfersQuery;
import org.junit.jupiter.api.Test;

class MoneyTransferProjectionTest extends DualScenarioTest<MoneyTransferActionStage, MoneyTransferAssertStage> {

  public static final String ACCOUNT_A = "11111111";
  public static final String ACCOUNT_B = "22222222";
  public static final String ACCOUNT_C = "33333333";


  @Test
  void initially_empty() {
    given()
      .no_prior_activity();

    then()
      .no_transfers_exist();
  }

  @Test
  void requested_transfer_can_be_found() {
    given()
      .no_prior_activity();

    when()
      .transfer_requested(new MoneyTransferRequestedEvent("1", ACCOUNT_A, ACCOUNT_B, 50));

    then()
      .transfer_exists_with_state_$(new MoneyTransfer("1", ACCOUNT_A, ACCOUNT_B, 50));
  }


  @Test
  void transfer_can_be_completed() {
    given()
      .transfer_requested(new MoneyTransferRequestedEvent("1", ACCOUNT_A, ACCOUNT_B, 50));


    when()
      .transfer_completed(new MoneyTransferCompletedEvent("1", ACCOUNT_A, 50));

    then()
      .transfer_exists_with_state_$(new MoneyTransfer("1", ACCOUNT_A, ACCOUNT_B, 50, true, null));
  }

  @Test
  void transfer_can_be_cancelled() {
    given()
      .transfer_requested(new MoneyTransferRequestedEvent("1", ACCOUNT_A, ACCOUNT_B, 50));

    when()
      .transfer_cancelled(new MoneyTransferCancelledEvent("1", "some error"));

    then()
      .transfer_exists_with_state_$(new MoneyTransfer("1", ACCOUNT_A, ACCOUNT_B, 50, false, "some error"));
  }

  @Test
  void can_query_transfers_for_account() {
    given()
      .transfer_requested(new MoneyTransferRequestedEvent("1", ACCOUNT_A, ACCOUNT_B, 50))
      .and()
      .transfer_requested(new MoneyTransferRequestedEvent("2", ACCOUNT_B, ACCOUNT_C, 10))
      .and()
      .transfer_completed(new MoneyTransferCompletedEvent("1", ACCOUNT_A, 50))
      .and()
      .transfer_completed(new MoneyTransferCompletedEvent("2", ACCOUNT_B, 10))
    ;

    when()
      .query_transfers_for_account(new MoneyTransfersQuery(ACCOUNT_A))
    ;

    then()
      .query_response_contains(
        new MoneyTransfer("1", ACCOUNT_A, ACCOUNT_B, 50, true, null)
      );
  }
  
}
