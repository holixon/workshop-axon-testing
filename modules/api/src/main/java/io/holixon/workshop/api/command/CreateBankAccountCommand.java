package io.holixon.workshop.api.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record CreateBankAccountCommand(
  @TargetAggregateIdentifier
  String accountId,
  int initialBalance
) {
  // empty
}
