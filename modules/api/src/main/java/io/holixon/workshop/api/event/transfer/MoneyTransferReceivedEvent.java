package io.holixon.workshop.api.event.transfer;

import org.axonframework.serialization.Revision;

@Revision("1")
public record MoneyTransferReceivedEvent(
  String moneyTransferId,
  int amount
) {

}
