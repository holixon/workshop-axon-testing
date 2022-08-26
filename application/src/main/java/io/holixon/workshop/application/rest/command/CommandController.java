package io.holixon.workshop.application.rest.command;

import io.holixon.workshop.context.bankaccount.api.command.CreateBankAccountCommand;
import io.holixon.workshop.context.bankaccount.api.command.atm.DepositMoneyCommand;
import io.holixon.workshop.context.bankaccount.api.command.atm.WithdrawMoneyCommand;
import io.holixon.workshop.context.bankaccount.api.command.transfer.RequestMoneyTransferCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static org.springframework.http.ResponseEntity.created;

@RestController
@RequestMapping("/rest/command")
public class CommandController {

  private final CommandGateway commandGateway;

  public CommandController(CommandGateway commandGateway) {
    this.commandGateway = commandGateway;
  }

  @PostMapping("/create-bank-account")
  public ResponseEntity<String> createBankAccount(@RequestBody CreateBankAccountCommand cmd) {
    commandGateway.sendAndWait(cmd);
    return created(
      ServletUriComponentsBuilder
        .fromCurrentContextPath()
        .path("/rest/query/current-balance/{accountId}")
        .buildAndExpand(cmd.accountId())
        .toUri()
    ).build();
  }

  @PutMapping("/withdraw-money")
  public void withdrawMoney(@RequestBody WithdrawMoneyCommand cmd) {
    commandGateway.sendAndWait(cmd);
  }

  @PutMapping("/deposit-money")
  public void depositMoney(@RequestBody DepositMoneyCommand cmd) {
    commandGateway.sendAndWait(cmd);
  }

  @PutMapping("/request-money-transfer")
  public void transferMoney(@RequestBody RequestMoneyTransferCommand cmd) {
    commandGateway.sendAndWait(cmd);
  }
}
