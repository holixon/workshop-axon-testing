package io.holixon.workshop.api.event;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record MoneyWithdrawnEvent(
  String accountId,
  int amount
) {
  // empty
}
