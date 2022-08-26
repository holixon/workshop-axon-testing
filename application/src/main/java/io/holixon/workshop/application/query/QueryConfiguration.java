package io.holixon.workshop.application.query;

import io.holixon.workshop.context.bankaccount.query.CurrentBalanceProjection;
import io.holixon.workshop.context.bankaccount.query.MoneyTransferProjection;
import org.springframework.context.annotation.Bean;

public class QueryConfiguration {

  @Bean
  public CurrentBalanceProjection currentBalanceProjection() {
    return new CurrentBalanceProjection();
  }

  @Bean
  public MoneyTransferProjection moneyTransferProjection() {
    return new MoneyTransferProjection();
  }


}
