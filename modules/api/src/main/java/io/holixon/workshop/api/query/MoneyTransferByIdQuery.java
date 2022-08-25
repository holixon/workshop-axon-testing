package io.holixon.workshop.api.query;

import java.util.Optional;
import org.axonframework.messaging.responsetypes.ResponseType;
import org.axonframework.messaging.responsetypes.ResponseTypes;

public record MoneyTransferByIdQuery(String moneyTransferId) {

  public static final ResponseType<Optional<MoneyTransfer>> RESPONSE_TYPE = ResponseTypes.optionalInstanceOf(MoneyTransfer.class);

}
