package io.holixon.workshop.api.event;

public record BankAccountCreatedEvent(
  String accountId,
  int initialBalance
) {
  // empty
}
