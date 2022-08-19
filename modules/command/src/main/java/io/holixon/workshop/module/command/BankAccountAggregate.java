package io.holixon.workshop.module.command;

import io.holixon.workshop.api.command.CreateBankAccountCommand;
import io.holixon.workshop.api.command.DepositMoneyCommand;
import io.holixon.workshop.api.command.WithdrawMoneyCommand;
import io.holixon.workshop.api.event.BankAccountCreatedEvent;
import io.holixon.workshop.api.event.MoneyDepositedEvent;
import io.holixon.workshop.api.event.MoneyWithdrawnEvent;
import io.holixon.workshop.module.command.exception.InsufficientBalanceException;
import io.holixon.workshop.module.command.exception.MaximumBalanceExceededException;
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

  public BankAccountAggregate() {
    // empty default constructor used for event sourcing
  }

  @CommandHandler
  public BankAccountAggregate(CreateBankAccountCommand cmd) {
    logger.trace("handle({})", cmd);
    AggregateLifecycle.apply(new BankAccountCreatedEvent(cmd.accountId(), cmd.initialBalance()));
  }

  @EventSourcingHandler
  public void on(BankAccountCreatedEvent evt) {
    // TODO this eventHandler could be left out so people see the "Aggregate identifier must be non-null after applying an event. Make sure the aggregate identifier is initialized at the latest when handling the creation event." exception in test.
    logger.trace("on({})", evt);
    this.accountId = evt.accountId();
    this.currentBalance = evt.initialBalance();
  }

  @CommandHandler
  public void handle(DepositMoneyCommand cmd) {
    logger.trace("on({})", cmd);

    if (cmd.amount() <= 0) {
      throw new IllegalArgumentException("Amount must be > 0.");
    } else if (currentBalance + cmd.amount() > MAX_BALANCE) {
      throw new MaximumBalanceExceededException(accountId, currentBalance, cmd.amount());
    }

    AggregateLifecycle.apply(new MoneyDepositedEvent(cmd.accountId(), cmd.amount()));
  }

  @EventSourcingHandler
  public void on(MoneyDepositedEvent evt) {
    logger.trace("on({})", evt);
    this.currentBalance += evt.amount();
  }

  @CommandHandler
  public void handle(WithdrawMoneyCommand cmd) {
    logger.trace("on({})", cmd);

    if (cmd.amount() <= 0) {
      throw new IllegalArgumentException("Amount must be > 0.");
    } else if (currentBalance - cmd.amount() < MIN_BALANCE) {
      throw new InsufficientBalanceException(accountId, currentBalance, cmd.amount());
    }

    AggregateLifecycle.apply(new MoneyWithdrawnEvent(cmd.accountId(), cmd.amount()));
  }

  @EventSourcingHandler
  public void on(MoneyWithdrawnEvent evt) {
    logger.trace("on({})", evt);
    this.currentBalance -= evt.amount();
  }

  public String getAccountId() {
    return accountId;
  }

  public int getCurrentBalance() {
    return currentBalance;
  }

  @Override
  public String toString() {
    return "BankAccountAggregate{" +
      "accountId='" + accountId + '\'' +
      ", currentBalance=" + currentBalance +
      '}';
  }
}
