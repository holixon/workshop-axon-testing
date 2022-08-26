package io.holixon.workshop.context.bankaccount.test.spring;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.As;
import com.tngtech.jgiven.annotation.Hidden;
import com.tngtech.jgiven.integration.spring.JGivenStage;
import io.holixon.workshop.context.bankaccount.api.command.CreateBankAccountCommand;
import io.holixon.workshop.context.bankaccount.api.command.atm.DepositMoneyCommand;
import io.holixon.workshop.context.bankaccount.api.command.atm.WithdrawMoneyCommand;
import io.holixon.workshop.context.bankaccount.api.command.transfer.RequestMoneyTransferCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;

@JGivenStage
public class GivenWhenStage extends Stage<GivenWhenStage> {

  @Autowired
  private CommandGateway commandGateway;

  @Autowired
  private QueryGateway queryGateway;

  @As("a bankAccount with id=$ and initialBalance=$")
  public GivenWhenStage create_bankAccount(String accountId, int initialBalance) {

    commandGateway.sendAndWait(new CreateBankAccountCommand(accountId, initialBalance));

    return self();
  }

  @As("an amount of $ is withdrawn")
  public GivenWhenStage withdraw(@Hidden String accountId, int amount) {
    commandGateway.sendAndWait(new WithdrawMoneyCommand(accountId, amount));
    return self();
  }

  @As("an amount of $ is deposited")
  public GivenWhenStage deposit(@Hidden String accountId, int amount) {
    commandGateway.sendAndWait(new DepositMoneyCommand(accountId, amount));
    return self();
  }

  @As("an amount of $amount is transferred from source=$sourceAccountId to target=$targetAccountId")
  public GivenWhenStage transfer(String sourceAccountId, String targetAccountId, int amount) {
    commandGateway.sendAndWait(new RequestMoneyTransferCommand(sourceAccountId, targetAccountId, amount));
    return self();
  }


}
