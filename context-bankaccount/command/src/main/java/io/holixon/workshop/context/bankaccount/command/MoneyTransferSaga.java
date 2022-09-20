package io.holixon.workshop.context.bankaccount.command;

import io.holixon.workshop.context.bankaccount.api.command.transfer.CancelMoneyTransferCommand;
import io.holixon.workshop.context.bankaccount.api.command.transfer.CompleteMoneyTransferCommand;
import io.holixon.workshop.context.bankaccount.api.command.transfer.ReceiveMoneyTransferCommand;
import io.holixon.workshop.context.bankaccount.api.event.transfer.MoneyTransferCompletedEvent;
import io.holixon.workshop.context.bankaccount.api.event.transfer.MoneyTransferReceivedEvent;
import io.holixon.workshop.context.bankaccount.api.event.transfer.MoneyTransferRequestedEvent;
import io.holixon.workshop.context.bankaccount.api.event.transfer.MoneyTransferredEvent;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Saga
public class MoneyTransferSaga {

  private static final Logger logger = LoggerFactory.getLogger(MoneyTransferSaga.class);

  private String moneyTransferId;
  private String sourceAccountId;
  private String targetAccountId;
  private int amount;

  @SagaEventHandler(associationProperty = "moneyTransferId")
  @StartSaga
  public void on(MoneyTransferRequestedEvent evt, CommandGateway commandGateway) {
    this.moneyTransferId = evt.moneyTransferId();
    this.sourceAccountId = evt.sourceAccountId();
    this.targetAccountId = evt.targetAccountId();
    this.amount = evt.amount();

    logger.info("MoneyTransferSaga[{}] started with: {}", this.moneyTransferId, evt);

    var receiveCmd = new ReceiveMoneyTransferCommand(this.targetAccountId, this.moneyTransferId, this.amount);

    commandGateway.send(receiveCmd, (commandMessage, commandResultMessage) -> {
      if (commandResultMessage.isExceptional()) {
        // on error cancel
        var cancelCmd = new CancelMoneyTransferCommand(
          this.sourceAccountId,
          this.moneyTransferId,
          commandResultMessage.exceptionResult().getMessage()
        );
        commandGateway.sendAndWait(cancelCmd);
      }
    });
  }

  @SagaEventHandler(associationProperty = "moneyTransferId")
  public void on(MoneyTransferReceivedEvent evt, CommandGateway commandGateway) {
    var completeCmd = new CompleteMoneyTransferCommand(sourceAccountId, moneyTransferId);

    // applied simplification: completion can/will not fail. Otherwise we would have to implement compensation on the target account (2-phase commit).
    commandGateway.sendAndWait(completeCmd);
  }

  @SagaEventHandler(associationProperty = "moneyTransferId")
  @EndSaga
  public void on(MoneyTransferCompletedEvent evt, EventGateway eventGateway) {
    logger.info("on({})", evt);

    // Milestone event
    var transferredEvent = new MoneyTransferredEvent(
      moneyTransferId,
      sourceAccountId,
      targetAccountId,
      amount
    );
    eventGateway.publish(transferredEvent);
    logger.info("MoneyTransferSaga[{}] started with: {}", this.moneyTransferId, transferredEvent);
  }

  @Override
  public String toString() {
    return "MoneyTransferSaga{" +
      "moneyTransferId='" + moneyTransferId + '\'' +
      ", sourceAccountId='" + sourceAccountId + '\'' +
      ", targetAccountId='" + targetAccountId + '\'' +
      ", amount=" + amount +
      '}';
  }
}
