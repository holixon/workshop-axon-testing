package io.holixon.workshop.module.command;

import io.holixon.workshop.api.command.transfer.CancelMoneyTransferCommand;
import io.holixon.workshop.api.command.transfer.CompleteMoneyTransferCommand;
import io.holixon.workshop.api.command.transfer.ReceiveMoneyTransferCommand;
import io.holixon.workshop.api.event.transfer.MoneyTransferReceivedEvent;
import io.holixon.workshop.api.event.transfer.MoneyTransferRequestedEvent;
import org.axonframework.commandhandling.gateway.CommandGateway;
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
  @EndSaga
  public void on(MoneyTransferReceivedEvent evt, CommandGateway commandGateway) {
    var completeCmd = new CompleteMoneyTransferCommand(sourceAccountId, moneyTransferId);
    commandGateway.sendAndWait(completeCmd);
  }

}
