package io.holixon.workshop.context.bankaccount.api.command.atm;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record WithdrawMoneyCommand(
  @TargetAggregateIdentifier
  String accountId,
  int amount
) {
  // empty
}
