package io.holixon.workshop.api.command.atm;

import java.util.UUID;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record DepositMoneyCommand(
  @TargetAggregateIdentifier
  String accountId,
  int amount,

  String transferId
) {
  // empty

  public DepositMoneyCommand(String accountId, int amount) {
    this(accountId, amount, UUID.randomUUID().toString());
  }
}
