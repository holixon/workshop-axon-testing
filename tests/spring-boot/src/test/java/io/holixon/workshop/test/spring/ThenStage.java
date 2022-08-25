package io.holixon.workshop.test.spring;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.As;
import com.tngtech.jgiven.annotation.Quoted;
import com.tngtech.jgiven.integration.spring.JGivenStage;
import io.holixon.workshop.api.query.CurrentBalanceQuery;
import io.holixon.workshop.api.query.CurrentBalanceResponse;
import org.assertj.core.api.Assertions;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;

@JGivenStage
public class ThenStage extends Stage<ThenStage> {
  @Autowired
  private CommandGateway commandGateway;

  @Autowired
  private QueryGateway queryGateway;

  @As("account $ has balance=$")
  public ThenStage account_has_balance(@Quoted String accountId, int balance) {

    await().untilAsserted(() -> {

      assertThat(queryGateway.query(new CurrentBalanceQuery(accountId), CurrentBalanceQuery.RESPONSE_TYPE).join()).hasValue(new CurrentBalanceResponse(accountId, balance));

    });


    return self();
  }
}
