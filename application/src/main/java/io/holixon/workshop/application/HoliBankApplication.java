package io.holixon.workshop.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.holixon.workshop.application.rest.query.QueryConfiguration;
import io.holixon.workshop.context.bankaccount.command.BankAccountAggregate;
import io.holixon.workshop.context.bankaccount.command.MoneyTransferIdGenerator;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.axonframework.common.caching.Cache;
import org.axonframework.common.caching.WeakReferenceCache;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@SpringBootApplication(scanBasePackageClasses = {
  HoliBankApplication.class,
  BankAccountAggregate.class
})
@OpenAPIDefinition
@Import({
  QueryConfiguration.class
})
public class HoliBankApplication {

  public static void main(String[] args) {
    SpringApplication.run(HoliBankApplication.class, args);
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

}
