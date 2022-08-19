package io.holixon.workshop.module.command;

import io.holixon.workshop.api.command.CreateBankAccountCommand;
import io.holixon.workshop.api.event.BankAccountCreatedEvent;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.junit.jupiter.api.Test;

class BankAccountAggregateFixtureTest {

  private final AggregateTestFixture<BankAccountAggregate> fixture = new AggregateTestFixture<>(BankAccountAggregate.class);

  @Test
  void createBankAccount() {
    fixture
      .givenNoPriorActivity()
      .when(new CreateBankAccountCommand("1", 100))
      .expectEvents(new BankAccountCreatedEvent("1", 100));
  }
}
