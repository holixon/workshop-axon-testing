package io.holixon.workshop.context.bankaccount.api.event.transfer;

import org.axonframework.serialization.Revision;

@Revision("1")
public record MoneyTransferredEvent(
  String moneyTransferId,
  String sourceAccountId,
  String targetAccountId,
  int amount
) {

}
