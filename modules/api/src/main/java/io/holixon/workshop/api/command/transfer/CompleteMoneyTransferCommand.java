package io.holixon.workshop.api.command.transfer;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record CompleteMoneyTransferCommand(

  @TargetAggregateIdentifier
  String sourceAccountId,

  String moneyTransferId

) {

}
