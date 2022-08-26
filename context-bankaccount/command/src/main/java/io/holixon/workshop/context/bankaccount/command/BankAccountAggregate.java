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
import java.util.HashMap;
import java.util.Map;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

  private int currentBalance;

  private final Map<String, Integer> activeMoneyTransfers = new HashMap<>();

  public BankAccountAggregate() {
    // empty default constructor used for event sourcing
  }

  @CommandHandler
  public BankAccountAggregate(CreateBankAccountCommand cmd) {
    logger.trace("handle({})", cmd);

    AggregateLifecycle.apply(new BankAccountCreatedEvent(cmd.accountId(), cmd.initialBalance()));
  }

  @CommandHandler
  public void handle(DepositMoneyCommand cmd) {
    logger.trace("on({})", cmd);
    assertAmountGtZero(cmd.amount());
    assertCanIncreaseBalance(cmd.amount());

    AggregateLifecycle.apply(new MoneyDepositedEvent(cmd.accountId(), cmd.amount()));
  }

  @CommandHandler
  public void handle(WithdrawMoneyCommand cmd) {
    logger.trace("handle({})", cmd);
    assertAmountGtZero(cmd.amount());
    assertCanDecreaseBalance(cmd.amount());

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
    assertActiveMoneyTransfer(cmd.moneyTransferId());

    AggregateLifecycle.apply(new MoneyTransferCompletedEvent(
        cmd.moneyTransferId(),
        cmd.sourceAccountId(),
        activeMoneyTransfers.get(cmd.moneyTransferId())
      )
    );
  }

  @CommandHandler
  public void handle(ReceiveMoneyTransferCommand cmd) {
    logger.trace("handle({})", cmd);
    assertAmountGtZero(cmd.amount());
    assertCanIncreaseBalance(cmd.amount());

    AggregateLifecycle.apply(new MoneyTransferReceivedEvent(
      cmd.moneyTransferId(),
      cmd.targetAccountId(),
      cmd.amount()));
  }

  @CommandHandler
  public void handle(CancelMoneyTransferCommand cmd) {
    logger.trace("handle({})", cmd);
    assertActiveMoneyTransfer(cmd.moneyTransferId());

    AggregateLifecycle.apply(new MoneyTransferCancelledEvent(cmd.moneyTransferId(), cmd.reason()));
  }

  // ----------------------------------------------------------

  @EventSourcingHandler
  public void on(BankAccountCreatedEvent evt) {
    // TODO this eventHandler could be left out so people see the "Aggregate identifier must be non-null after applying an event. Make sure the aggregate identifier is initialized at the latest when handling the creation event." exception in test.
    logger.trace("on({})", evt);
    this.accountId = evt.accountId();
    this.currentBalance = evt.initialBalance();
  }

  @EventSourcingHandler
  public void on(MoneyDepositedEvent evt) {
    logger.trace("on({})", evt);
    this.currentBalance += evt.amount();
  }

  @EventSourcingHandler
  public void on(MoneyWithdrawnEvent evt) {
    logger.trace("on({})", evt);
    this.currentBalance -= evt.amount();
  }

  @EventSourcingHandler
  public void on(MoneyTransferRequestedEvent evt) {
    logger.trace("on({})", evt);

    activeMoneyTransfers.put(evt.moneyTransferId(), evt.amount());
  }

  @EventSourcingHandler
  public void on(MoneyTransferCompletedEvent evt) {
    logger.trace("on({})", evt);

    currentBalance -= activeMoneyTransfers.get(evt.moneyTransferId());
    activeMoneyTransfers.remove(evt.moneyTransferId());
  }

  @EventSourcingHandler
  public void on(MoneyTransferReceivedEvent evt) {
    logger.trace("on({})", evt);

    currentBalance += evt.amount();
  }

  @EventSourcingHandler
  public void on(MoneyTransferCancelledEvent evt) {
    logger.trace("on({})", evt);

    activeMoneyTransfers.remove(evt.moneyTransferId());
  }

  // ----------------------------------------------------------

  void assertAmountGtZero(int amount) throws IllegalArgumentException {
    if (amount <= 0) {
      throw new IllegalArgumentException("Amount must be > 0.");
    }
  }

  void assertCanIncreaseBalance(int amount) throws MaximumBalanceExceededException {
    if (currentBalance + amount > MAX_BALANCE) {
      throw new MaximumBalanceExceededException(accountId, currentBalance, amount);
    }
  }

  void assertCanDecreaseBalance(int amount) throws InsufficientBalanceException {
    if (getEffectiveBalance() - amount < MIN_BALANCE) {
      throw new InsufficientBalanceException(accountId, currentBalance, amount);
    }
  }

  void assertActiveMoneyTransfer(String moneyTransferId) {
    if (!activeMoneyTransfers.containsKey(moneyTransferId)) {
      throw new IllegalStateException(String.format("BankAccount[id=%s] is not part of transfer=%s", accountId, moneyTransferId));
    }
  }

  // ----------------------------------------------------------

  public int getEffectiveBalance() {
    return currentBalance - activeMoneyTransfers.values().stream().mapToInt(Integer::intValue).sum();
  }

  public String getAccountId() {
    return accountId;
  }

  public int getCurrentBalance() {
    return currentBalance;
  }

  public Map<String, Integer> getActiveMoneyTransfers() {
    return activeMoneyTransfers;
  }

  @Override
  public String toString() {
    return "BankAccountAggregate{" +
      "accountId='" + accountId + '\'' +
      ", currentBalance=" + currentBalance +
      ", currentTransfers=" + activeMoneyTransfers +
      '}';
  }
}
