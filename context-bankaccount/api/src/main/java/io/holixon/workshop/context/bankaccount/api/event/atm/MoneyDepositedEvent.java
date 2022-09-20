package io.holixon.workshop.context.bankaccount.api.event.atm;

import org.axonframework.serialization.Revision;

@Revision("1")
public record MoneyDepositedEvent(
  String accountId,
  int amount
) {
  // empty
}
