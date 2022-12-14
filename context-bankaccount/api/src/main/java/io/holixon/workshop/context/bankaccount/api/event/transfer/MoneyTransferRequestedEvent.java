package io.holixon.workshop.context.bankaccount.api.event.transfer;

import org.axonframework.serialization.Revision;

@Revision("1")
public record MoneyTransferRequestedEvent(

  String moneyTransferId,
  String sourceAccountId,
  String targetAccountId,
  int amount

) {

}
