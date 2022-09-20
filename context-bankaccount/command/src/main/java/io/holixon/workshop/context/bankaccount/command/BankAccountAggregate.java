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
import io.holixon.workshop.context.bankaccount.command.exception.InsufficientBalanceException;
import io.holixon.workshop.context.bankaccount.command.exception.MaximumBalanceExceededException;
import io.holixon.workshop.context.bankaccount.command.model.BalanceModel;
import io.holixon.workshop.context.bankaccount.command.model.MoneyTransferModel;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Aggregate(cache = "bankAccountCache")
public class BankAccountAggregate {

  private static final Logger logger = LoggerFactory.getLogger(BankAccountAggregate.class);

  /**
   * We do not lend money. Never. To anyone.
   */
  public static final int MIN_BALANCE = 0;
  /**
   * We believe that no one will ever need more money than this. (this rule allows easier testing of failures on transfers).
   */
  public static final int MAX_BALANCE = 1000;

  @AggregateIdentifier
  private String accountId;

  private BalanceModel balance;
  private MoneyTransferModel moneyTransfers = new MoneyTransferModel(new HashMap<>());

  public BankAccountAggregate() {
    // empty default constructor used for event sourcing
  }

  @CommandHandler
  public BankAccountAggregate(CreateBankAccountCommand cmd) {
    logger.trace("handle({})", cmd);
    if (cmd.initialBalance() > MAX_BALANCE) {
      throw new MaximumBalanceExceededException(cmd.accountId(), 0, cmd.initialBalance());
    }

    // AggregateLifecycle.apply(new BankAccountCreatedEvent(cmd.accountId(), cmd.initialBalance()));
  }

  @CommandHandler
  public void handle(DepositMoneyCommand cmd) {
    logger.trace("on({})", cmd);
    assertAmountGtZero(cmd.amount());

    if (!balance.canIncreaseBalance(cmd.amount())) {
      throw new MaximumBalanceExceededException(accountId, balance.value(), cmd.amount());
    }

    AggregateLifecycle.apply(new MoneyDepositedEvent(cmd.accountId(), cmd.amount()));
  }

  @CommandHandler
  public void handle(WithdrawMoneyCommand cmd) {
    logger.trace("handle({})", cmd);
    assertAmountGtZero(cmd.amount());
    if (!balance.canDecreaseBalance(cmd.amount(), moneyTransfers.getReservedAmount())) {
      throw new InsufficientBalanceException(accountId, balance.value(), cmd.amount());
    }
    AggregateLifecycle.apply(new MoneyWithdrawnEvent(cmd.accountId(), cmd.amount()));
  }

  @CommandHandler
  public void handle(RequestMoneyTransferCommand cmd, MoneyTransferIdGenerator idGenerator) {
    logger.trace("handle({})", cmd);
    assertAmountGtZero(cmd.amount());

    AggregateLifecycle.apply(new MoneyTransferRequestedEvent(
      idGenerator.get(),
      accountId,
      cmd.targetAccountId(),
      cmd.amount()
    ));
  }

  @CommandHandler
  public void handle(CompleteMoneyTransferCommand cmd) {
    logger.trace("handle({})", cmd);
    if (!moneyTransfers.hasMoneyTransfer(cmd.moneyTransferId())) {
      throw new IllegalStateException(String.format("BankAccount[id=%s] is not part of transfer=%s", accountId, cmd.moneyTransferId()));
    }


    AggregateLifecycle.apply(new MoneyTransferCompletedEvent(
                               cmd.moneyTransferId(),
                               cmd.sourceAccountId(),
                               moneyTransfers.getAmountForTransfer(cmd.moneyTransferId())
                             )
    );
  }

  @CommandHandler
  public void handle(ReceiveMoneyTransferCommand cmd) {
    logger.trace("handle({})", cmd);
    assertAmountGtZero(cmd.amount());

    if (!balance.canIncreaseBalance(cmd.amount())) {
      throw new MaximumBalanceExceededException(accountId, balance.value(), cmd.amount());
    }

    AggregateLifecycle.apply(new MoneyTransferReceivedEvent(
      cmd.moneyTransferId(),
      cmd.targetAccountId(),
      cmd.amount()));
  }

  @CommandHandler
  public void handle(CancelMoneyTransferCommand cmd) {
    logger.trace("handle({})", cmd);

    if (!moneyTransfers.hasMoneyTransfer(cmd.moneyTransferId())) {
      throw new IllegalStateException(String.format("BankAccount[id=%s] is not part of transfer=%s", accountId, cmd.moneyTransferId()));
    }

    AggregateLifecycle.apply(new MoneyTransferCancelledEvent(cmd.moneyTransferId(), cmd.reason()));
  }

  // ----------------------------------------------------------

//  @EventSourcingHandler
//  public void on(BankAccountCreatedEvent evt) {
//    // TODO this eventHandler could be left out so people see the "Aggregate identifier must be non-null after applying an event. Make sure the aggregate identifier is initialized at the latest when handling the creation event." exception in test.
//    logger.trace("on({})", evt);
//    this.accountId = evt.accountId();
//    this.balance = new BalanceModel(evt.initialBalance(), MIN_BALANCE, MAX_BALANCE);
//  }

  @EventSourcingHandler
  public void on(MoneyDepositedEvent evt) {
    logger.trace("on({})", evt);
    this.balance = this.balance.increase(evt.amount());
  }

  @EventSourcingHandler
  public void on(MoneyWithdrawnEvent evt) {
    logger.trace("on({})", evt);
    this.balance = this.balance.decrease(evt.amount());
  }

  @EventSourcingHandler
  public void on(MoneyTransferRequestedEvent evt) {
    logger.trace("on({})", evt);
    moneyTransfers = moneyTransfers.add(evt.moneyTransferId(), evt.amount());
  }

  @EventSourcingHandler
  public void on(MoneyTransferCompletedEvent evt) {
    logger.trace("on({})", evt);

    this.balance = this.balance.decrease(moneyTransfers.getAmountForTransfer(evt.moneyTransferId()));

    moneyTransfers = moneyTransfers.remove(evt.moneyTransferId());
  }

  @EventSourcingHandler
  public void on(MoneyTransferReceivedEvent evt) {
    logger.trace("on({})", evt);
    this.balance = this.balance.increase(evt.amount());
  }

  @EventSourcingHandler
  public void on(MoneyTransferCancelledEvent evt) {
    logger.trace("on({})", evt);

    moneyTransfers = moneyTransfers.remove(evt.moneyTransferId());
  }

  // ----------------------------------------------------------

  void assertAmountGtZero(int amount) throws IllegalArgumentException {
    if (amount <= 0) {
      throw new IllegalArgumentException("Amount must be > 0.");
    }
  }

  // ----------------------------------------------------------

  public String getAccountId() {
    return accountId;
  }

  public int getCurrentBalance() {
    return balance.value();
  }

  public Map<String, Integer> getActiveMoneyTransfers() {
    return moneyTransfers.getActiveMoneyTransfers();
  }

  @Override
  public String toString() {
    return "BankAccountAggregate{" +
      "accountId='" + accountId + '\'' +
      ", currentBalance=" + balance.value() +
      ", currentTransfers=" + moneyTransfers.activeMoneyTransfers() +
      '}';
  }
}
