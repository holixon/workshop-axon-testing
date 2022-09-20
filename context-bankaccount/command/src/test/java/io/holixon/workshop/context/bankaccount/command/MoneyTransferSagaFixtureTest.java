package io.holixon.workshop.context.bankaccount.command;

import io.holixon.workshop.context.bankaccount.api.command.transfer.ReceiveMoneyTransferCommand;
import io.holixon.workshop.context.bankaccount.api.event.transfer.MoneyTransferRequestedEvent;
import org.axonframework.test.saga.SagaTestFixture;
import org.junit.jupiter.api.Test;

class MoneyTransferSagaFixtureTest {

  private final SagaTestFixture<MoneyTransferSaga> fixture = new SagaTestFixture<>(MoneyTransferSaga.class);

  @Test
  void start_saga_on_moneyTransferRequested() {
    fixture
      .givenNoPriorActivity()
      .whenAggregate("1")
      .publishes(new MoneyTransferRequestedEvent("mt-1-2", "1", "2", 100))
      .expectActiveSagas(1)
      .expectDispatchedCommands(new ReceiveMoneyTransferCommand("2", "mt-1-2", 100))
      ;
  }

//  @Test
//  void start_saga_on_moneyTransferRequested() {
//    fixture
//      .givenNoPriorActivity()
//      .whenAggregate("1")
//      .publishes(new MoneyTransferRequestedEvent("1-2", "1", "2", 100))
//      .expectActiveSagas(1)
//      ;
//  }
}
