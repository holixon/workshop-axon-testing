package io.holixon.workshop.context.bankaccount.query;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.As;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import io.holixon.workshop.context.bankaccount.api.event.BankAccountCreatedEvent;
import io.holixon.workshop.context.bankaccount.api.event.atm.MoneyDepositedEvent;
import io.holixon.workshop.context.bankaccount.api.event.atm.MoneyWithdrawnEvent;
import io.holixon.workshop.context.bankaccount.api.event.transfer.MoneyTransferredEvent;

import java.util.UUID;

public class CurrentBalanceGivenWhenStage extends Stage<CurrentBalanceGivenWhenStage> {

  @ProvidedScenarioState
  private final CurrentBalanceProjection projection = new CurrentBalanceProjection();

  @As("bankAccount[$]:  created with initialBalance=$")
  public CurrentBalanceGivenWhenStage bankAccount_created(String accountId, int initialBalance) {
    projection.on(new BankAccountCreatedEvent(accountId, initialBalance));
    return self();
  }

  @As("bankAccount[$]:  withdrawn=$")
  public CurrentBalanceGivenWhenStage withdraw_amount_from_account(String accountId, int amount) {
    projection.on(new MoneyWithdrawnEvent(accountId, amount));

    return self();
  }

  @As("bankAccount[$]:  deposited=$")
  public CurrentBalanceGivenWhenStage deposit_amount_on_account(String accountId, int amount) {
    projection.on(new MoneyDepositedEvent(accountId, amount));

    return self();
  }

  @As("bankAccount[$]:  transfer amount=$ to bankAccount[$]")
  public CurrentBalanceGivenWhenStage transfer_money(String sourceAccountId, int amount, String targetAccountId) {
    projection.on(new MoneyTransferredEvent(UUID.randomUUID().toString(), sourceAccountId, targetAccountId, amount));

    return self();
  }
}
