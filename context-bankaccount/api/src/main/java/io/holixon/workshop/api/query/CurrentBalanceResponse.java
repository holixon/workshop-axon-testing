package io.holixon.workshop.api.query;

public record CurrentBalanceResponse(
  String accountId,
  int currentBalance
) {
  // empty
}
