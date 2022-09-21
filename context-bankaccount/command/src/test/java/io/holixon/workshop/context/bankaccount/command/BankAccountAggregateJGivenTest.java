package io.holixon.workshop.context.bankaccount.command;

import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import io.holixon.axon.testing.jgiven.AxonJGivenJava;
import io.holixon.axon.testing.jgiven.junit5.AggregateFixtureScenarioTest;
import io.holixon.workshop.context.bankaccount.api.command.CreateBankAccountCommand;
import io.holixon.workshop.context.bankaccount.api.event.BankAccountCreatedEvent;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.junit.jupiter.api.Test;

public class BankAccountAggregateJGivenTest extends AggregateFixtureScenarioTest<BankAccountAggregate> {

  @ProvidedScenarioState
  private final AggregateTestFixture<BankAccountAggregate> fixture = AxonJGivenJava.aggregateTestFixtureBuilder(BankAccountAggregate.class)
    .build();

  @Test
  void create_a_new_bankaccount() {
    given()
      .noPriorActivity()
    ;

    when()
      .command(new CreateBankAccountCommand("1", 100))
    ;

    then()
      .expectEvent(new BankAccountCreatedEvent("1", 100))
      .and()
      .expectState("initial balance is 100", new BankAccountAggregateState())
    ;
  }
}
