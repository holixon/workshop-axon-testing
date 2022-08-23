package io.holixon.workshop.application;

import io.holixon.workshop.lib.jackson.HoliBankObjectMapper;
import io.holixon.workshop.module.command.BankAccountAggregate;
import io.holixon.workshop.module.command.MoneyTransferIdGenerator;
import io.holixon.workshop.module.query.CurrentBalanceProjection;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.axonframework.common.caching.Cache;
import org.axonframework.common.caching.WeakReferenceCache;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackageClasses = {
  HoliBankApplication.class,
  BankAccountAggregate.class
})
@OpenAPIDefinition(

)
public class HoliBankApplication {

  public static void main(String[] args) {
    SpringApplication.run(HoliBankApplication.class, args);
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
  public CurrentBalanceProjection currentBalanceProjection() {
    return new CurrentBalanceProjection();
  }
}
