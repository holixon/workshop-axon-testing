package io.holixon.workshop.context.bankaccount.command;

import org.assertj.core.api.SoftAssertions;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

class BankAccountAggregateState implements Consumer<BankAccountAggregate> {

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
