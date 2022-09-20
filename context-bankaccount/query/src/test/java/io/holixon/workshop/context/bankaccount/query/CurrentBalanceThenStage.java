package io.holixon.workshop.context.bankaccount.query;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.As;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import io.holixon.workshop.context.bankaccount.api.query.CurrentBalanceQuery;
import io.holixon.workshop.context.bankaccount.api.query.CurrentBalanceResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class CurrentBalanceThenStage extends Stage<CurrentBalanceThenStage> {

  @ExpectedScenarioState
  private CurrentBalanceProjection projection;

  @As("bankAccount[$]:  expect currentBalance=$")
  public CurrentBalanceThenStage expect_currentBalance_for_account(String accountId, int expectedBalance) {
    var result = projection.query(new CurrentBalanceQuery(accountId));

    assertThat(result).hasValue(new CurrentBalanceResponse(accountId, expectedBalance));

    return self();
  }
}
