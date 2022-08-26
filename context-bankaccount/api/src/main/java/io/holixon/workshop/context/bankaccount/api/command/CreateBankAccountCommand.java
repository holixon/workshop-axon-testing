package io.holixon.workshop.context.bankaccount.api.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record CreateBankAccountCommand(
  @TargetAggregateIdentifier
  String accountId,
  int initialBalance
) {
  // empty
}
