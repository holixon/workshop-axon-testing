package io.holixon.workshop.context.bankaccount.api.event.transfer;

import org.axonframework.serialization.Revision;

@Revision("1")
public record MoneyTransferCompletedEvent(
  String moneyTransferId,
  String sourceAccountId,

  int amount
) {

}
