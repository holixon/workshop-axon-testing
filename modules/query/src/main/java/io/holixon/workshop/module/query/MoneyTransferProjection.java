package io.holixon.workshop.module.query;

import io.holixon.workshop.api.event.transfer.MoneyTransferCancelledEvent;
import io.holixon.workshop.api.event.transfer.MoneyTransferCompletedEvent;
import io.holixon.workshop.api.event.transfer.MoneyTransferReceivedEvent;
import io.holixon.workshop.api.event.transfer.MoneyTransferRequestedEvent;
import io.holixon.workshop.api.event.transfer.MoneyTransferredEvent;
import io.holixon.workshop.api.query.MoneyTransfer;
import io.holixon.workshop.api.query.MoneyTransferByIdQuery;
import io.holixon.workshop.api.query.MoneyTransfersQuery;
import io.holixon.workshop.api.query.MoneyTransfersResponse;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;

public class MoneyTransferProjection {

  private final Map<String, MoneyTransfer> store = new ConcurrentHashMap<>();

  @QueryHandler
  public Optional<MoneyTransfer> query(MoneyTransferByIdQuery query) {
    return Optional.ofNullable(store.get(query.moneyTransferId()));
  }

  @QueryHandler
  public MoneyTransfersResponse query(MoneyTransfersQuery query) {
    var filtered = store.values().stream()
      .filter(it -> it.sourceAccountId().equals(query.accountId()) || it.targetAccountId().equals(query.accountId()))
      .toList();

    return new MoneyTransfersResponse(filtered);
  }

  @EventHandler
  public void on(MoneyTransferRequestedEvent evt) {
    store.put(evt.moneyTransferId(), new MoneyTransfer(evt.moneyTransferId(), evt.sourceAccountId(), evt.targetAccountId(), evt.amount()));
  }

  @EventHandler
  public void on(MoneyTransferCompletedEvent evt) {
    store.computeIfPresent(evt.moneyTransferId(), (s, moneyTransfer) ->
      new MoneyTransfer(moneyTransfer.moneyTransferId(), moneyTransfer.sourceAccountId(), moneyTransfer.targetAccountId(),moneyTransfer.amount(), null)
    );
  }

  @EventHandler
  public void on(MoneyTransferCancelledEvent evt) {
    store.computeIfPresent(evt.moneyTransferId(), (s, moneyTransfer) ->
      new MoneyTransfer(moneyTransfer.moneyTransferId(), moneyTransfer.sourceAccountId(), moneyTransfer.targetAccountId(),moneyTransfer.amount(), evt.reason())
    );
  }
  MoneyTransfersResponse findAll() {
    return new MoneyTransfersResponse(store.values());
  }


}
