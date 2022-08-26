package io.holixon.workshop.test;

import io.holixon.workshop.api.query.MoneyTransfer;
import io.holixon.workshop.lib.jackson.HoliBankObjectMapper;
import io.holixon.workshop.module.command.BankAccountAggregate;
import io.holixon.workshop.module.command.MoneyTransferIdGenerator;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.config.AggregateConfiguration;
import org.axonframework.config.AggregateConfigurer;
import org.axonframework.config.Configuration;
import org.axonframework.config.Configurer;
import org.axonframework.config.DefaultConfigurer;
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.json.JacksonSerializer;

public class AxonConfigurationBuilder {

  private Serializer serializer = JacksonSerializer.builder().objectMapper(new HoliBankObjectMapper()).build();
  private InMemoryEventStorageEngine eventStore = new InMemoryEventStorageEngine();




  public Configuration build() {
    Configurer configurer = DefaultConfigurer.defaultConfiguration()
      .configureEmbeddedEventStore(c -> eventStore);
    configurer.eventProcessing().registerSaga(MoneyTransfer.class);
    configurer.configureAggregate(BankAccountAggregate.class);

    configurer.registerComponent(MoneyTransferIdGenerator.class, c -> MoneyTransferIdGenerator.RANDOM);

    //configurer.configureAggregate(AggregateConfigurer.defaultConfiguration(BankAccountAggregate.class).)

    configurer.configureCommandBus(c -> SimpleCommandBus.builder()
      .transactionManager(c.getComponent(TransactionManager.class))
      .messageMonitor(c.messageMonitor(SimpleCommandBus.class, "commandBus"))
      .build());

    return configurer.start();
  }


}
