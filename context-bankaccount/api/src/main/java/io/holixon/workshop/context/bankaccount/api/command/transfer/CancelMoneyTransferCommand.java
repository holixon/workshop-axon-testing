package io.holixon.workshop.context.bankaccount.api.command.transfer;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record CancelMoneyTransferCommand(

  @TargetAggregateIdentifier
  String sourceAccountId,

  String moneyTransferId,

  String reason

) {

}
