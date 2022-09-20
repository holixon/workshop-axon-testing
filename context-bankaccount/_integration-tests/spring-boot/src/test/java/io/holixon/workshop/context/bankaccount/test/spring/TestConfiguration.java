package io.holixon.workshop.context.bankaccount.test.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.holixon.workshop.context.bankaccount.command.BankAccountAggregate;
import io.holixon.workshop.context.bankaccount.command.MoneyTransferIdGenerator;
import io.holixon.workshop.context.bankaccount.command.MoneyTransferSaga;
import io.holixon.workshop.context.bankaccount.query.CurrentBalanceProjection;
import io.holixon.workshop.context.bankaccount.query.MoneyTransferProjection;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.common.caching.Cache;
import org.axonframework.common.caching.WeakReferenceCache;
import org.axonframework.eventhandling.tokenstore.TokenStore;
import org.axonframework.eventhandling.tokenstore.inmemory.InMemoryTokenStore;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine;
import org.axonframework.modelling.saga.repository.SagaStore;
import org.axonframework.modelling.saga.repository.inmemory.InMemorySagaStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import({
  MoneyTransferSaga.class,
  BankAccountAggregate.class,
  BankAccountActionStage.class,
  BankAccountAssertStage.class
})
@EnableAutoConfiguration
public class TestConfiguration {

  @Bean
  public CurrentBalanceProjection currentBalanceProjection() {
    return new CurrentBalanceProjection();
  }

  @Bean
  public MoneyTransferProjection moneyTransferProjection() {
    return new MoneyTransferProjection();
  }


  @Bean
  @Qualifier("defaultObjectMapper")
  public ObjectMapper holiBankObjectMapper() {
    return new ObjectMapper();
  }

  @Bean
  public MoneyTransferIdGenerator moneyTransferIdGenerator() {
    return MoneyTransferIdGenerator.RANDOM;
  }

  @Bean
  public Cache bankAccountCache() {
    return new WeakReferenceCache();
  }


  @Bean
  public CommandBus commandBus() {
    return SimpleCommandBus.builder().build();
  }

  @Bean
  public EventStorageEngine eventStorageEngine() {
    return new InMemoryEventStorageEngine();
  }


  @Bean
  public TokenStore inMemoryTokenStore() {
    return new InMemoryTokenStore();
  }

  @Bean
  public SagaStore<Object> inMemorySagaStore() {
    return new InMemorySagaStore();
  }

}
