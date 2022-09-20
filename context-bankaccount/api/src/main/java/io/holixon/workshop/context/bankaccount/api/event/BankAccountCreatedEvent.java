package io.holixon.workshop.context.bankaccount.api.event;

import org.axonframework.serialization.Revision;

@Revision("1")
public record BankAccountCreatedEvent(
  String accountId,
  int initialBalance
) {
  // empty
}
