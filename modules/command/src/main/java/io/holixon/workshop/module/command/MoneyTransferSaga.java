package io.holixon.workshop.module.command;

import io.holixon.workshop.api.event.transfer.MoneyTransferRequestedEvent;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;

@Saga
public class MoneyTransferSaga {

  private String moneyTransferId;
  private String sourceAccountId;
  private String targetAccountId;
  private int amount;

  @SagaEventHandler(associationProperty = "moneyTransferId")
  @StartSaga
  public void on(MoneyTransferRequestedEvent evt) {
    this.moneyTransferId = evt.moneyTransferId();
    this.sourceAccountId = evt.sourceAccountId();
    this.targetAccountId = evt.targetAccountId();
    this.amount = evt.amount();
  }

}
