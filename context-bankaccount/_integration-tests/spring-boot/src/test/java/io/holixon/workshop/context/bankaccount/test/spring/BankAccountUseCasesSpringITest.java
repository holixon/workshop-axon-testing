package io.holixon.workshop.context.bankaccount.test.spring;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import com.tngtech.jgiven.integration.spring.EnableJGiven;
import com.tngtech.jgiven.integration.spring.junit5.DualSpringScenarioTest;
import io.holixon.workshop.infrastructure.jackson.HoliBankObjectMapper;
import io.holixon.workshop.context.bankaccount.command.BankAccountAggregate;
import io.holixon.workshop.context.bankaccount.command.MoneyTransferIdGenerator;
import io.holixon.workshop.context.bankaccount.command.MoneyTransferSaga;
import io.holixon.workshop.context.bankaccount.query.CurrentBalanceProjection;
import io.holixon.workshop.context.bankaccount.query.MoneyTransferProjection;
import io.holixon.workshop.context.bankaccount.test.spring.BankAccountUseCasesSpringITest.TestConfiguration;
import java.util.UUID;
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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@SpringBootTest(classes = TestConfiguration.class)
@EnableJGiven
public class BankAccountUseCasesSpringITest extends DualSpringScenarioTest<GivenWhenStage, ThenStage> {


  @Test
  void create_account_and_query_balance() {
    String account1 = accountId();

    given()
      .create_bankAccount(account1, 100);

    then()
      .account_has_balance(account1, 100);
  }

  @Test
  void withdraw_amount() {
    String account1 = accountId();

    given()
      .create_bankAccount(account1, 100)
      ;
    when()
      .withdraw(account1, 10);
    then()
      .account_has_balance(account1, 90);

  }

  @Test
  void deposit_amount() {
    String account1 = accountId();

    given()
      .create_bankAccount(account1, 100)
      ;
    when()
      .deposit(account1, 10);
    then()
      .account_has_balance(account1, 110);
  }

  @Test
  void transfer_10_from_source_to_target() {
    String account1 = accountId();
    String account2 = accountId();

    given()
      .create_bankAccount(account1, 100)
      .and()
      .create_bankAccount(account2, 0)
      ;
    when()
      .transfer(account1, account2, 30);

    then()
      .account_has_balance(account1, 70)
      .and()
      .account_has_balance(account2, 30)
    ;
  }



  private String accountId() {
    return UUID.randomUUID().toString();
  }

  @Import({
    MoneyTransferSaga.class,
    BankAccountAggregate.class,
    GivenWhenStage.class,
    ThenStage.class
  })
  @EnableAutoConfiguration
  public static class TestConfiguration {

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
    public HoliBankObjectMapper holiBankObjectMapper() {
      return new HoliBankObjectMapper();
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
}
