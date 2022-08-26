package io.holixon.workshop.context.bankaccount.command;

import io.holixon.workshop.context.bankaccount.api.command.CreateBankAccountCommand;
import io.holixon.workshop.context.bankaccount.api.command.atm.DepositMoneyCommand;
import io.holixon.workshop.context.bankaccount.api.command.atm.WithdrawMoneyCommand;
import io.holixon.workshop.context.bankaccount.api.command.transfer.CancelMoneyTransferCommand;
import io.holixon.workshop.context.bankaccount.api.command.transfer.CompleteMoneyTransferCommand;
import io.holixon.workshop.context.bankaccount.api.command.transfer.ReceiveMoneyTransferCommand;
import io.holixon.workshop.context.bankaccount.api.command.transfer.RequestMoneyTransferCommand;
import io.holixon.workshop.context.bankaccount.api.event.BankAccountCreatedEvent;
import io.holixon.workshop.context.bankaccount.api.event.atm.MoneyDepositedEvent;
import io.holixon.workshop.context.bankaccount.api.event.atm.MoneyWithdrawnEvent;
import io.holixon.workshop.context.bankaccount.api.event.transfer.MoneyTransferCancelledEvent;
import io.holixon.workshop.context.bankaccount.api.event.transfer.MoneyTransferCompletedEvent;
import io.holixon.workshop.context.bankaccount.api.event.transfer.MoneyTransferReceivedEvent;
import io.holixon.workshop.context.bankaccount.api.event.transfer.MoneyTransferRequestedEvent;
import io.holixon.workshop.context.bankaccount.command.exception.MaximumBalanceExceededException;
import io.holixon.workshop.context.bankaccount.command.exception.InsufficientBalanceException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import org.assertj.core.api.SoftAssertions;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class BankAccountAggregateFixtureTest {

  private static final Logger logger = LoggerFactory.getLogger(BankAccountAggregateFixtureTest.class);

  private final AggregateTestFixture<BankAccountAggregate> fixture = new AggregateTestFixture<>(BankAccountAggregate.class);

  static class BankAccountAggregateState implements Consumer<BankAccountAggregate> {

    private final List<BiConsumer<SoftAssertions, BankAccountAggregate>> assertions = new ArrayList<>();

    public BankAccountAggregateState accountId(String accountId) {
      assertions.add((soft, aggregate) -> {
        soft.assertThat(aggregate.getAccountId())
          .as("accountId should be=%s", accountId)
          .isEqualTo(accountId);
      });
      return this;
    }

    public BankAccountAggregateState currentBalance(int currentBalance) {
      assertions.add((soft, aggregate) -> {
        soft.assertThat(aggregate.getCurrentBalance())
          .as("currentBalance should be=%d", currentBalance)
          .isEqualTo(currentBalance);
      });
      return this;
    }

    public BankAccountAggregateState activeTransfer(String moneyTransferId, int amount) {
      assertions.add((soft, aggregate) -> {
        soft.assertThat(aggregate.getActiveMoneyTransfers())
          .as("account should be participating in transfer[id=%s,amount=%d]", moneyTransferId, amount)
          .containsEntry(moneyTransferId, amount);
      });
      return this;
    }

    public BankAccountAggregateState noActiveTransfers() {
      assertions.add((soft, aggregate) -> {
        soft.assertThat(aggregate.getActiveMoneyTransfers())
          .as("account should no be participating in any transfer, but is: %s", aggregate.getActiveMoneyTransfers())
          .isEmpty();
      });
      return this;
    }

    public BankAccountAggregateState log(Logger externalLogger) {
      assertions.add((soft, aggregate) -> externalLogger.info(aggregate.toString()));
      return this;
    }

    @Override
    public void accept(BankAccountAggregate aggregate) {
      SoftAssertions soft = new SoftAssertions();
      assertions.forEach(assertion -> assertion.accept(soft, aggregate));
      soft.assertAll();
    }
  }

  @Nested
  class AtmTest {

    @Test
    void create_bank_account() {
      fixture
        .givenNoPriorActivity()
        .when(
          new CreateBankAccountCommand("1", 100)
        )
        .expectEvents(
          new BankAccountCreatedEvent("1", 100)
        )
        .expectState(new BankAccountAggregateState()
          .accountId("1")
          .currentBalance(100)
          .log(logger)
        );
    }

    @Test
    void create_bank_account_fails_with_initial_balance_gt_max() {
      fixture
        .givenNoPriorActivity()
        .when(
          new CreateBankAccountCommand("1", 1001)
        )
        .expectException(MaximumBalanceExceededException.class);
    }

    @Test
    void deposit_money_adds_to_currentBalance() {
      fixture
        .given(
          new BankAccountCreatedEvent("1", 100)
        )
        .when(
          new DepositMoneyCommand("1", 100)
        )
        .expectEvents(
          new MoneyDepositedEvent("1", 100)
        )
        .expectState(new BankAccountAggregateState()
          .accountId("1")
          .currentBalance(200)
        );
    }

    @Test
    void depositMoney_fails_without_positive_amount() {
      fixture
        .given(
          new BankAccountCreatedEvent("1", 100)
        )
        .when(
          new DepositMoneyCommand("1", 0)
        )
        .expectNoEvents()
        .expectException(IllegalArgumentException.class)
        .expectExceptionMessage("Amount must be > 0.");
    }

    @Test
    void depositMoney_fails_when_maxBalance_exceeds() {
      fixture
        .given(
          new BankAccountCreatedEvent("1", 100)
        )
        .when(
          new DepositMoneyCommand("1", 901)
        )
        .expectNoEvents()
        .expectException(MaximumBalanceExceededException.class)
        .expectExceptionMessage("BankAccount[id=1, currentBalance=100]: Deposit of amount=901 not allowed, would exceed max. balance of 1000");
    }

    @Test
    void withdrawMoney_subtracts_from_currentBalance() {
      fixture
        .given(
          new BankAccountCreatedEvent("1", 100)
        )
        .when(
          new WithdrawMoneyCommand("1", 100)
        )
        .expectEvents(
          new MoneyWithdrawnEvent("1", 100)
        )
        .expectState(new BankAccountAggregateState()
          .accountId("1")
          .currentBalance(0)
          .noActiveTransfers()
        );
    }

    @Test
    void withdrawMoney_fails_without_positive_amount() {
      fixture
        .given(
          new BankAccountCreatedEvent("1", 100)
        )
        .when(
          new WithdrawMoneyCommand("1", 0)
        )
        .expectNoEvents()
        .expectException(IllegalArgumentException.class)
        .expectExceptionMessage("Amount must be > 0.");
    }

    @Test
    void withdrawMoney_fails_when_subBalance_subceeds() {
      fixture
        .given(
          new BankAccountCreatedEvent("1", 100)
        )
        .when(
          new WithdrawMoneyCommand("1", 101)
        )
        .expectNoEvents()
        .expectException(InsufficientBalanceException.class)
        .expectExceptionMessage("BankAccount[id=1, currentBalance=100]: Withdrawal of amount=101 not allowed, would subceed min. balance of 0");
    }
  }

  @Nested
  class MoneyTransferTest {

    @BeforeEach
    void setUp() {
      fixture.registerInjectableResource(new MoneyTransferIdGeneratorFake("mt-1-2"));
    }

    @Test
    void moneyTransfer_can_be_requested_on_source() {
      fixture
        .given(
          new BankAccountCreatedEvent("1", 100)
        )
        .when(
          new RequestMoneyTransferCommand("1", "2", 50)
        )
        .expectEvents(
          new MoneyTransferRequestedEvent("mt-1-2", "1", "2", 50)
        )
        .expectState(new BankAccountAggregateState()
          .accountId("1")
          .currentBalance(100)
          .activeTransfer("mt-1-2", 50)
        );
    }

    @Test
    void moneyTransfer_can_be_completed_on_source() {
      fixture
        .given(
          new BankAccountCreatedEvent("1", 100),
          new MoneyTransferRequestedEvent("mt-1-2", "1", "2", 60)
        )
        .when(
          new CompleteMoneyTransferCommand("1", "mt-1-2")
        )
        .expectEvents(
          new MoneyTransferCompletedEvent("mt-1-2", "1", 60)
        )
        .expectState(new BankAccountAggregateState()
          .accountId("1")
          .currentBalance(40)
          .noActiveTransfers()
        );
    }

    @Test
    void moneyTransfer_can_be_received_on_target() {
      fixture
        .given(
          new BankAccountCreatedEvent("2", 100)
        )
        .when(
          new ReceiveMoneyTransferCommand("2", "mt-1-2", 50)
        )
        .expectEvents(
          new MoneyTransferReceivedEvent("mt-1-2", "2", 50)
        )
        .expectState(new BankAccountAggregateState()
          .accountId("2")
          .currentBalance(150)
          .noActiveTransfers()
        );
    }

    @Test
    void moneyTransfer_can_be_cancelled_on_source() {
      fixture
        .given(
          new BankAccountCreatedEvent("1", 100),
          new MoneyTransferRequestedEvent("mt-1-2", "1", "2", 50)
        )
        .when(
          new CancelMoneyTransferCommand("1", "mt-1-2", "exception on target")
        )
        .expectEvents(
          new MoneyTransferCancelledEvent("mt-1-2", "exception on target")
        )
        .expectState(new BankAccountAggregateState()
          .accountId("1")
          .currentBalance(100)
          .noActiveTransfers()
        );
    }
  }
}
