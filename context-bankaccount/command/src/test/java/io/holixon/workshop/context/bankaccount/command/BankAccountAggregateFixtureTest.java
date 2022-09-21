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

/**
 * Aggregate testing has following aspects:
 *
 * * command can be handled correctly (constraints)
 * * commandHandler emits expected Event
 * * (eventSourcingHandler modifies internal state of aggregate)
 */
class BankAccountAggregateFixtureTest {

  private final AggregateTestFixture<BankAccountAggregate> fixture = new AggregateTestFixture<>(BankAccountAggregate.class);

  @Nested
  class AtmTest {

    @Test
    void create_bank_account() {
      fail("Implement me");

      // GIVEN: nothing happened before
      // WHEN: we create a bank account with id and initial balance
      // THEN: event: BankAccountCreated(id, initial)
      // AND: account.id==id, account.balance==initial

    }

    @Test
    void deposit_money_adds_to_currentBalance() {
      fail("Implement me");

      // GIVEN: account with id and initial balance
      // WHEN: we deposit money "amount" to account
      // THEN: event: MoneyDeposited(id, amount")
      // AND: account.balance==initial+amount
    }

    @Test
    void withdrawMoney_fails_when_subBalance_subceeds() {
      fail("Implement me");

      // GIVEN: account with id and initial balance
      // WHEN: we try to withdraw more than the initial balance
      // THEN: no events
      // AND: error that balance is insufficient

    }

  }

  @Nested
  class MoneyTransferTest {

    @BeforeEach
    void setUp() {
      // this allows to use a fixed value instead of a random uuid
      fixture.registerInjectableResource(new MoneyTransferIdGeneratorFake("mt-1-2"));
    }

    @Test
    void moneyTransfer_can_be_requested_on_source() {
      fail("Implement me");

      // GIVEN: account with id and initial balance
      // WHEN we start a money transfer to another account
      // THEN event MoneyTransferRequestedEvent(transferId, id, otherId, amount)
      // AND account.activeTransfers contains the new money transfer
    }

    @Test
    void moneyTransfer_can_be_completed_on_source() {
      fail("Implement me");

      // GIVEN: account with id and initial balance
      // AND we start a money transfer to another account
      // WHEN the transfer is completed successfully
      // THEN event MoneyTransferCompletedEvent(transferId, id, amount)
      // AND account has no active transfers
    }

  }
}
