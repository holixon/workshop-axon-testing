package io.holixon.workshop.context.bankaccount.api.command.atm;

import java.util.UUID;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record DepositMoneyCommand(
  @TargetAggregateIdentifier
  String accountId,
  int amount
) {
  // empty
}
