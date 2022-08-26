package io.holixon.workshop.test;

import io.holixon.workshop.api.command.CreateBankAccountCommand;
import java.util.UUID;
import org.axonframework.config.Configuration;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled("learn hot to register parameter resolvers")
class BankAccountUseCasesTest {



  @Test
  void name() {
    AxonConfigurationBuilder axon = new AxonConfigurationBuilder();
    Configuration configuration = axon.build();

    configuration.commandGateway().sendAndWait(new CreateBankAccountCommand("1", 100));


  }


  private String accountId() {
    return UUID.randomUUID().toString();
  }
}
