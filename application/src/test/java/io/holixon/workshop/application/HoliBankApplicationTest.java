package io.holixon.workshop.application;

import static org.assertj.core.api.Assertions.assertThat;

import io.holixon.axon.testcontainer.AxonServerContainer;
import io.holixon.axon.testcontainer.spring.AxonServerContainerSpring;
import io.holixon.workshop.context.bankaccount.api.command.CreateBankAccountCommand;
import io.holixon.workshop.context.bankaccount.api.command.atm.DepositMoneyCommand;
import io.holixon.workshop.context.bankaccount.api.command.atm.WithdrawMoneyCommand;
import io.holixon.workshop.context.bankaccount.api.command.transfer.RequestMoneyTransferCommand;
import io.holixon.workshop.context.bankaccount.api.query.CurrentBalanceQuery;
import io.holixon.workshop.context.bankaccount.api.query.CurrentBalanceResponse;
import io.holixon.workshop.application.HoliBankApplicationTest.HoliBankRestClient;
import java.util.Map;
import org.awaitility.Awaitility;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = {
  HoliBankApplication.class,
  HoliBankRestClient.class
})
@Testcontainers
@EnableAutoConfiguration
class HoliBankApplicationTest {
  private static final Logger logger = LoggerFactory.getLogger(HoliBankApplicationTest.class);

  @Container
  public static final AxonServerContainer AXON = AxonServerContainer.builder()
    .dockerImageVersion("4.6.2-dev")
    .enableDevMode()
    .build();

  @DynamicPropertySource
  public static void axonProperties(final DynamicPropertyRegistry registry) {
    AxonServerContainerSpring.addDynamicProperties(AXON, registry);
  }

  @Autowired
  private CommandGateway commandGateway;

  @Autowired
  private QueryGateway queryGateway;

  @Autowired
  private HoliBankRestClient client;

  @Test
  void start_axonServer_run_command_and_query() {
    commandGateway.sendAndWait(new CreateBankAccountCommand("1", 100));

    Awaitility.await().untilAsserted(() -> {
      assertThat(queryGateway.query(new CurrentBalanceQuery("1"), CurrentBalanceQuery.RESPONSE_TYPE).join())
        .hasValue(new CurrentBalanceResponse("1", 100));
    });

  }

  @Test
  void create_via_rest() {
    client.createBankAccount(new CreateBankAccountCommand("2", 100));
  }

  @AfterAll
  static void afterAll() {
    AXON.stop();
  }

  @Service
  public static class HoliBankRestClient {

    private final RestTemplate restTemplate;
    private final String rootUrl;
    public HoliBankRestClient(RestTemplateBuilder restTemplateBuilder, ServletWebServerApplicationContext ctx) {

      restTemplate = restTemplateBuilder
        .build();
      rootUrl = "http://localhost:" + ctx.getWebServer().getPort();
    }

    public void createBankAccount(CreateBankAccountCommand cmd) {
      restTemplate.postForEntity(rootUrl + "/command/create-bank-account", cmd, Void.class);
    }

    public void withdrawMoney(WithdrawMoneyCommand cmd) {
      restTemplate.put(rootUrl + "/command/withdraw-money", cmd);
    }

    public void depositMoney(DepositMoneyCommand cmd) {
      restTemplate.put(rootUrl + "/command/deposit-money", cmd);
    }

    public void transferMoney(RequestMoneyTransferCommand cmd) {
      restTemplate.put(rootUrl + "/command/request-money-transfer", cmd);
    }

    public ResponseEntity<CurrentBalanceResponse> findCurrentBalance(String accountId) {
      return restTemplate.getForEntity(
        rootUrl + "/current-balance/{accountId}",
        CurrentBalanceResponse.class,
        Map.of("accountId", accountId)
      );
    }
  }
}
