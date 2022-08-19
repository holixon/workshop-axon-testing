package io.holixon.workshop.api.event;

public record MoneyDepositedEvent(
  String accountId,
  int amount
) {
  // empty
}
