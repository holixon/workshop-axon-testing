package io.holixon.workshop.application.rest.query;

import io.holixon.workshop.context.bankaccount.api.query.CurrentBalanceQuery;
import io.holixon.workshop.context.bankaccount.api.query.CurrentBalanceResponse;
import io.holixon.workshop.context.bankaccount.api.query.MoneyTransfersQuery;
import io.holixon.workshop.context.bankaccount.api.query.MoneyTransfersResponse;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("rest/query")
public class QueryController {

  private final QueryGateway queryGateway;

  public QueryController(QueryGateway queryGateway) {
    this.queryGateway = queryGateway;
  }

  @GetMapping("/current-balance/{accountId}")
  public ResponseEntity<CurrentBalanceResponse> findCurrentBalance(
    @PathVariable("accountId") String accountId
  ) {
    var future = queryGateway.query(new CurrentBalanceQuery(accountId), CurrentBalanceQuery.RESPONSE_TYPE);

    return ResponseEntity.of(future.join());
  }

  @GetMapping("/money-transfers/{accountId}")
  public ResponseEntity<MoneyTransfersResponse> findMoneyTransfers(
    @PathVariable("accountId") String accountId
  ) {
    var future = queryGateway.query(new MoneyTransfersQuery(accountId), MoneyTransfersQuery.RESPONSE_TYPE);

    return ResponseEntity.ok(future.join());
  }


}
