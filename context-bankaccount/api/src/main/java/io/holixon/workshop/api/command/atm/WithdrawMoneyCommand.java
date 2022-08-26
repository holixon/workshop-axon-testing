package io.holixon.workshop.api.command.atm;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record WithdrawMoneyCommand(
  @TargetAggregateIdentifier
  String accountId,
  int amount
) {
  // empty
}
