package io.holixon.workshop.context.bankaccount.query;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import io.holixon.workshop.context.bankaccount.api.event.transfer.MoneyTransferCancelledEvent;
import io.holixon.workshop.context.bankaccount.api.event.transfer.MoneyTransferCompletedEvent;
import io.holixon.workshop.context.bankaccount.api.event.transfer.MoneyTransferRequestedEvent;
import io.holixon.workshop.context.bankaccount.api.query.MoneyTransfersQuery;
import io.holixon.workshop.context.bankaccount.api.query.MoneyTransfersResponse;

import java.util.concurrent.atomic.AtomicReference;

public class MoneyTransferActionStage extends Stage<MoneyTransferActionStage> {

  @ProvidedScenarioState
  private final MoneyTransferProjection projection = new MoneyTransferProjection();

  @ProvidedScenarioState
  private final AtomicReference<MoneyTransfersResponse> found = new AtomicReference<>();

  public MoneyTransferActionStage no_prior_activity() {
    return self();
  }

  public MoneyTransferActionStage transfer_requested(MoneyTransferRequestedEvent evt) {
    projection.on(evt);
    return self();
  }

  public MoneyTransferActionStage transfer_completed(MoneyTransferCompletedEvent evt) {
    projection.on(evt);
    return self();
  }

  public MoneyTransferActionStage transfer_cancelled(MoneyTransferCancelledEvent evt) {
    projection.on(evt);
    return self();
  }

  public MoneyTransferActionStage query_transfers_for_account(MoneyTransfersQuery query) {
    found.set(projection.query(query));
    return self();
  }


}
