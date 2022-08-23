package io.holixon.workshop.application.command;

import io.holixon.workshop.api.command.CreateBankAccountCommand;
import io.holixon.workshop.api.command.atm.DepositMoneyCommand;
import io.holixon.workshop.api.command.atm.WithdrawMoneyCommand;
import io.holixon.workshop.api.command.transfer.RequestMoneyTransferCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/command")
public class CommandController {

  private final CommandGateway commandGateway;

  public CommandController(CommandGateway commandGateway) {
    this.commandGateway = commandGateway;
  }

  @PostMapping("/create-bank-account")
  public void createBankAccount(CreateBankAccountCommand cmd) {
    commandGateway.sendAndWait(cmd);
  }

  @PutMapping("/withdraw-money")
  public void withdrawMoney(WithdrawMoneyCommand cmd) {
    commandGateway.sendAndWait(cmd);
  }

  @PutMapping("/deposit-money")
  public void depositMoney(DepositMoneyCommand cmd) {
    commandGateway.sendAndWait(cmd);
  }

  @PutMapping("/request-money-transfer")
  public void transferMoney(RequestMoneyTransferCommand cmd) {
    commandGateway.sendAndWait(cmd);
  }
}
