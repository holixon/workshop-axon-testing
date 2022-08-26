package io.holixon.workshop.api.command.transfer;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

/**
 * Invoked by the saga, recipient is target account.
 */
public record ReceiveMoneyTransferCommand(
  @TargetAggregateIdentifier
  String targetAccountId,

  String moneyTransferId,

  int amount
) {

}
