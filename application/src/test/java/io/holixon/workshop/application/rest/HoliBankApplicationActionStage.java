package io.holixon.workshop.application.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.As;
import com.tngtech.jgiven.annotation.Quoted;
import com.tngtech.jgiven.integration.spring.JGivenStage;
import io.holixon.workshop.context.bankaccount.api.command.CreateBankAccountCommand;
import io.holixon.workshop.context.bankaccount.api.command.atm.WithdrawMoneyCommand;
import io.holixon.workshop.context.bankaccount.api.command.transfer.RequestMoneyTransferCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@JGivenStage
public class HoliBankApplicationActionStage extends Stage<HoliBankApplicationActionStage> {

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockMvc rest;

  public HoliBankApplicationActionStage noPriorActivity() {
    return self();
  }

  @As("new account is created with account id: $accountId and balance $initialBalance")
  public HoliBankApplicationActionStage account_is_created(@Quoted String accountId, @Quoted int initialBalance) throws Exception {
    rest
      .perform(
        post("/rest/command/create-bank-account")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(
            new CreateBankAccountCommand(accountId, initialBalance)
          ))
      ).andExpect(
        status().isCreated()
      ).andExpect(
        header()
          .string("Location", "http://localhost/rest/query/current-balance/" + accountId)
      );
    return self();
  }

  @As("amount $amount is withdrawn from account $accountId")
  public HoliBankApplicationActionStage money_is_withdrawn(@Quoted String accountId, @Quoted int amount) throws Exception {
    rest
      .perform(
        put("/rest/command/withdraw-money")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(
            new WithdrawMoneyCommand(accountId, amount)
          ))
      ).andExpect(
        status().isNoContent()
      );
    return self();
  }

  @As("amount $amount is deposited to account $accountId")
  public HoliBankApplicationActionStage money_is_deposited(@Quoted String accountId, @Quoted int amount) throws Exception {
    rest
      .perform(
        put("/rest/command/deposit-money")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(
            new WithdrawMoneyCommand(accountId, amount)
          ))
      ).andExpect(
        status().isNoContent()
      );
    return self();
  }

  @As("amount $amount is transferred from account $sourceAccount to account $targetAccount")
  public HoliBankApplicationActionStage money_is_transferred(@Quoted String sourceAccountId, @Quoted String targetAccountId, @Quoted int amount) throws Exception {
    rest
      .perform(
        put("/rest/command/request-money-transfer")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(
            new RequestMoneyTransferCommand(sourceAccountId, targetAccountId, amount)
          ))
      ).andExpect(
        status().isNoContent()
      );

    return self();
  }
}
