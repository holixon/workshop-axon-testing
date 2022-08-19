package io.holixon.workshop.api.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record DepositMoneyCommand(
  @TargetAggregateIdentifier
  String accountId,
  int amount
) {
  // empty
}
