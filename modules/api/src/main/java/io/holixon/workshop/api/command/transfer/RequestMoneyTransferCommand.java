package io.holixon.workshop.api.command.transfer;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

/**
 * Received by the source account.
 * Reserves amount for transfer and starts saga.
 */
public record RequestMoneyTransferCommand(
  @TargetAggregateIdentifier
  String sourceAccountId,

  String targetAccountId,

  int amount
) {

  // empty

}
