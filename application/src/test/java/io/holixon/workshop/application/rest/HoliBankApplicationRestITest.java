package io.holixon.workshop.application.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.holixon.axon.testcontainer.AxonServerContainer;
import io.holixon.axon.testcontainer.spring.AxonServerContainerSpring;
import io.holixon.workshop.application.HoliBankApplication;
import io.holixon.workshop.context.bankaccount.api.command.CreateBankAccountCommand;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {
  HoliBankApplication.class
})
@Testcontainers
@EnableAutoConfiguration
@AutoConfigureMockMvc
public class HoliBankApplicationRestITest {
  @Container
  public static final AxonServerContainer AXON = AxonServerContainer
    .builder()
    .dockerImageVersion("4.6.2-dev")
    .enableDevMode()
    .build();

  @DynamicPropertySource
  public static void axonProperties(final DynamicPropertyRegistry registry) {
    AxonServerContainerSpring.addDynamicProperties(AXON, registry);
  }

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockMvc rest;

  @AfterAll
  static void afterAll() {
    AXON.stop();
  }

  @Test
  void should_create_account() throws Exception {
    rest
      .perform(
        post("/rest/command/create-bank-account")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(
            new CreateBankAccountCommand("2", 100)
          ))
      ).andExpect(
        status().isCreated()
      ).andExpect(
        header()
          .string("Location", "http://localhost/rest/query/current-balance/2")
      );
  }

  @Test
  void should_withdraw_money() throws Exception {

  }

  @Test
  void should_deposit_money() throws Exception {

  }

}
