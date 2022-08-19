package io.holixon.workshop.module.command;

import io.holixon.workshop.api.command.CreateBankAccountCommand;
import io.holixon.workshop.api.command.DepositMoneyCommand;
import io.holixon.workshop.api.command.WithdrawMoneyCommand;
import io.holixon.workshop.api.event.BankAccountCreatedEvent;
import io.holixon.workshop.api.event.MoneyDepositedEvent;
import io.holixon.workshop.api.event.MoneyWithdrawnEvent;
import io.holixon.workshop.module.command.exception.InsufficientBalanceException;
import io.holixon.workshop.module.command.exception.MaximumBalanceExceededException;
import java.util.function.Consumer;
import org.assertj.core.api.SoftAssertions;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.junit.jupiter.api.Test;

class BankAccountAggregateFixtureTest {

  private final AggregateTestFixture<BankAccountAggregate> fixture = new AggregateTestFixture<>(BankAccountAggregate.class);

  private Consumer<BankAccountAggregate> expectedState(String accountId, int currentBalance) {
    return bankAccountAggregate -> {
      var collect = new SoftAssertions();
      collect.assertThat(bankAccountAggregate.getAccountId())
        .as("accountId should be=%s", accountId)
        .isEqualTo(accountId);
      collect.assertThat(bankAccountAggregate.getCurrentBalance())
        .as("currentBalance should be=%d", currentBalance)
        .isEqualTo(currentBalance);
      collect.assertAll();
    };
  }

  @Test
  void create_bank_account() {
    fixture
      .givenNoPriorActivity()
      .when(new CreateBankAccountCommand("1", 100))
      .expectEvents(new BankAccountCreatedEvent("1", 100))
      .expectState(expectedState("1", 100))
    ;
  }

  @Test
  void deposit_money_adds_to_currentBalance() {
    fixture
      .given(new BankAccountCreatedEvent("1", 100))
      .when(new DepositMoneyCommand("1", 100))
      .expectEvents(new MoneyDepositedEvent("1", 100))
      .expectState(expectedState("1", 200));
  }

  @Test
  void depositMoney_fails_without_positive_amount() {
    fixture
      .given(new BankAccountCreatedEvent("1", 100))
      .when(new DepositMoneyCommand("1", 0))
      .expectNoEvents()
      .expectException(IllegalArgumentException.class)
      .expectExceptionMessage("Amount must be > 0.");
  }

  @Test
  void depositMoney_fails_when_maxBalance_exceeds() {
    fixture
      .given(new BankAccountCreatedEvent("1", 100))
      .when(new DepositMoneyCommand("1", 901))
      .expectNoEvents()
      .expectException(MaximumBalanceExceededException.class)
      .expectExceptionMessage("BankAccount[id=1, currentBalance=100]: Deposit of amount=901 not allowed, would exceed max. balance of 1000");
  }

  @Test
  void withdrawMoney_subtracts_from_currentBalance(){
    fixture
      .given(new BankAccountCreatedEvent("1", 100))
      .when(new WithdrawMoneyCommand("1", 100))
      .expectEvents(new MoneyWithdrawnEvent("1",100))
      .expectState(expectedState("1",0));
  }

  @Test
  void withdrawMoney_fails_without_positive_amount(){
    fixture
      .given(new BankAccountCreatedEvent("1", 100))
      .when(new WithdrawMoneyCommand("1", 0))
      .expectNoEvents()
      .expectException(IllegalArgumentException.class)
      .expectExceptionMessage("Amount must be > 0.");
  }


  @Test
  void withdrawMoney_fails_when_subBalance_subceeds() {
    fixture
      .given(new BankAccountCreatedEvent("1", 100))
      .when(new WithdrawMoneyCommand("1", 101))
      .expectNoEvents()
      .expectException(InsufficientBalanceException.class)
      .expectExceptionMessage("BankAccount[id=1, currentBalance=100]: Withdrawl of amount=101 not allowed, would subceed min. balance of 0");
  }

}
