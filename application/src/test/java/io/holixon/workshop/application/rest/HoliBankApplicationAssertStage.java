package io.holixon.workshop.application.rest;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.integration.spring.JGivenStage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.awaitility.Awaitility.await;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@JGivenStage
public class HoliBankApplicationAssertStage extends Stage<HoliBankApplicationAssertStage> {

  @Autowired
  private MockMvc rest;

  public HoliBankApplicationAssertStage account_with_id_has_balance(String accountId, int balance) throws Exception {
    await().untilAsserted(
      () -> rest
        .perform(
          get("/rest/query/current-balance/{accountId}", accountId))
        .andExpect(
          status().isOk())
        .andExpect(
          content().contentType(MediaType.APPLICATION_JSON))
        // .andDo(print())  // to demonstrate the request and the response processed by the controller
        .andExpect(
          jsonPath("$.currentBalance").value(balance)
        )
    );
    return self();
  }
}
