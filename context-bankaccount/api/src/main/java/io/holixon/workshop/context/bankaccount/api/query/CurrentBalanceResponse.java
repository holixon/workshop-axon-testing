package io.holixon.workshop.context.bankaccount.api.query;

public record CurrentBalanceResponse(
  String accountId,
  int currentBalance
) {
  // empty
}
