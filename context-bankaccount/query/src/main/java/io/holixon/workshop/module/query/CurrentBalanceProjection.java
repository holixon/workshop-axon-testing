package io.holixon.workshop.module.query;

import io.holixon.workshop.api.event.BankAccountCreatedEvent;
import io.holixon.workshop.api.event.atm.MoneyDepositedEvent;
import io.holixon.workshop.api.event.atm.MoneyWithdrawnEvent;
import io.holixon.workshop.api.event.transfer.MoneyTransferredEvent;
import io.holixon.workshop.api.query.CurrentBalanceQuery;
import io.holixon.workshop.api.query.CurrentBalanceResponse;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;

public class CurrentBalanceProjection {

  private final Map<String, CurrentBalanceResponse> store = new ConcurrentHashMap<>();

  @QueryHandler
  public Optional<CurrentBalanceResponse> query(CurrentBalanceQuery query) {
    return Optional.ofNullable(store.get(query.accountId()));
  }

  @EventHandler
  public void on(BankAccountCreatedEvent evt) {
    store.put(evt.accountId(), new CurrentBalanceResponse(evt.accountId(), evt.initialBalance()));
  }

  @EventHandler
  public void on(MoneyWithdrawnEvent evt) {
    modifyCurrentBalance(evt.accountId(), -evt.amount());
  }

  @EventHandler
  public void on(MoneyDepositedEvent evt) {
    modifyCurrentBalance(evt.accountId(), +evt.amount());
  }

  @EventHandler
  public void on(MoneyTransferredEvent evt) {
    modifyCurrentBalance(evt.sourceAccountId(), -evt.amount());
    modifyCurrentBalance(evt.targetAccountId(), +evt.amount());
  }

  private void modifyCurrentBalance(String accountId, int amount) {
    store.computeIfPresent(accountId, (key, value) -> new CurrentBalanceResponse(key, value.currentBalance() + amount));
  }
}
