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
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

class BankAccountAggregateFixtureTest {

  private final AggregateTestFixture<BankAccountAggregate> fixture = new AggregateTestFixture<>(BankAccountAggregate.class);

  @Nested
  class AtmTest {

    @Test
    void create_bank_account() {
      fail("Implement me");
    }

    @Test
    void create_bank_account_fails_with_initial_balance_gt_max() {
      fail("Implement me");
    }

    @Test
    void deposit_money_adds_to_currentBalance() {
      fail("Implement me");
    }

    @Test
    void depositMoney_fails_without_positive_amount() {
      fail("Implement me");
    }

    @Test
    void depositMoney_fails_when_maxBalance_exceeds() {
      fail("Implement me");
    }

    @Test
    void withdrawMoney_subtracts_from_currentBalance() {
      fail("Implement me");
    }

    @Test
    void withdrawMoney_fails_without_positive_amount() {
      fail("Implement me");
    }

    @Test
    void withdrawMoney_fails_when_subBalance_subceeds() {
      fail("Implement me");
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
      fail("Implement me");
    }

    @Test
    void moneyTransfer_can_be_completed_on_source() {
      fail("Implement me");
    }

    @Test
    void moneyTransfer_can_be_received_on_target() {
      fail("Implement me");
    }

    @Test
    void moneyTransfer_can_be_cancelled_on_source() {
      fail("Implement me");
    }
  }
}
