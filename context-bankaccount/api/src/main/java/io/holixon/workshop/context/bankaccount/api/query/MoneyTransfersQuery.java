package io.holixon.workshop.context.bankaccount.api.query;

import org.axonframework.messaging.responsetypes.ResponseType;
import org.axonframework.messaging.responsetypes.ResponseTypes;

public record MoneyTransfersQuery(String accountId) {
  public static final ResponseType<MoneyTransfersResponse> RESPONSE_TYPE = ResponseTypes.instanceOf(MoneyTransfersResponse.class);

  public MoneyTransfersQuery() {
    this(null);
  }
}
