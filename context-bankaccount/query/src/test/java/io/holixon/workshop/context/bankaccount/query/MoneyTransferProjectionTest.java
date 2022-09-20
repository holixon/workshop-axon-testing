package io.holixon.workshop.context.bankaccount.query;

import com.tngtech.jgiven.junit5.DualScenarioTest;
import io.holixon.workshop.context.bankaccount.api.event.transfer.MoneyTransferCancelledEvent;
import io.holixon.workshop.context.bankaccount.api.event.transfer.MoneyTransferCompletedEvent;
import io.holixon.workshop.context.bankaccount.api.event.transfer.MoneyTransferRequestedEvent;
import io.holixon.workshop.context.bankaccount.api.query.MoneyTransfer;
import io.holixon.workshop.context.bankaccount.api.query.MoneyTransfersQuery;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
class MoneyTransferProjectionTest extends DualScenarioTest<MoneyTransferGivenWhenStage, MoneyTransferThenStage> {

  public static final String ACCOUNT_A = "11111111";
  public static final String ACCOUNT_B = "22222222";
  public static final String ACCOUNT_C = "33333333";


  @Test
  void initially_empty() {

  }

  @Test
  void requested_transfer_can_be_found() {

  }


  @Test
  void transfer_can_be_completed() {

  }

  @Test
  void transfer_can_be_cancelled() {

  }

  @Test
  void can_query_transfers_for_account() {

  }

}
