package io.holixon.workshop.context.bankaccount.api.command.transfer;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record CompleteMoneyTransferCommand(

  @TargetAggregateIdentifier
  String sourceAccountId,

  String moneyTransferId

) {

}
