package io.holixon.workshop.module.command;

import io.holixon.workshop.api.command.CreateBankAccountCommand;
import io.holixon.workshop.api.event.BankAccountCreatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aggregate(cache = "bankAccountCache")
public class BankAccountAggregate {
  private static final Logger logger = LoggerFactory.getLogger(BankAccountAggregate.class);

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

}
