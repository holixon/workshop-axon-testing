package io.holixon.workshop.context.bankaccount.test.spring;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.As;
import com.tngtech.jgiven.annotation.Quoted;
import com.tngtech.jgiven.integration.spring.JGivenStage;
import io.holixon.workshop.context.bankaccount.api.query.CurrentBalanceQuery;
import io.holixon.workshop.context.bankaccount.api.query.CurrentBalanceResponse;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@JGivenStage
public class BankAccountAssertStage extends Stage<BankAccountAssertStage> {
  @Autowired
  private CommandGateway commandGateway;

  @Autowired
  private QueryGateway queryGateway;

  @As("account $ has balance=$")
  public BankAccountAssertStage account_has_balance(@Quoted String accountId, int balance) {
    await().untilAsserted(() -> {
      assertThat(queryGateway.query(new CurrentBalanceQuery(accountId), CurrentBalanceQuery.RESPONSE_TYPE).join()).hasValue(new CurrentBalanceResponse(accountId, balance));
    });
    return self();
  }
}
