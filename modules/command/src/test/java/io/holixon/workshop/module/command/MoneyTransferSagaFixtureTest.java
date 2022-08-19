package io.holixon.workshop.module.command;

import io.holixon.workshop.api.event.transfer.MoneyTransferRequestedEvent;
import org.axonframework.test.saga.SagaTestFixture;
import org.junit.jupiter.api.Test;

class MoneyTransferSagaFixtureTest {

  private final SagaTestFixture<MoneyTransferSaga> fixture = new SagaTestFixture<>(MoneyTransferSaga.class);

  @Test
  void start_saga_on_moneyTransferStarted() {
    fixture
      .givenNoPriorActivity()
      .whenAggregate("1")
      .publishes(new MoneyTransferRequestedEvent("1-2", "1", "2", 100))
      .expectActiveSagas(1)

      ;

  }
}
