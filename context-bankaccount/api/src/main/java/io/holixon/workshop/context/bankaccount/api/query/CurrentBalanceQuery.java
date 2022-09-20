package io.holixon.workshop.context.bankaccount.api.query;

import java.util.Optional;
import org.axonframework.messaging.responsetypes.ResponseType;
import org.axonframework.messaging.responsetypes.ResponseTypes;

public record CurrentBalanceQuery(
  String accountId
) {
  public static ResponseType<Optional<CurrentBalanceResponse>> RESPONSE_TYPE = ResponseTypes.optionalInstanceOf(CurrentBalanceResponse.class);
}
