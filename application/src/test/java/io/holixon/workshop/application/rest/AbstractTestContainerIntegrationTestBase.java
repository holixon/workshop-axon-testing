package io.holixon.workshop.application.rest;

import com.tngtech.jgiven.integration.spring.junit5.DualSpringScenarioTest;
import io.holixon.axon.testcontainer.AxonServerContainer;
import io.holixon.axon.testcontainer.spring.AxonServerContainerSpring;
import org.junit.jupiter.api.AfterAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class AbstractTestContainerIntegrationTestBase<ACTION, ASSERT> extends DualSpringScenarioTest<ACTION, ASSERT> {
  @Container
  public static final AxonServerContainer AXON = AxonServerContainer
    .builder()
    .dockerImageVersion("4.6.2-dev")
    .enableDevMode()
    .build();

  @AfterAll
  static void afterAll() {
    AXON.stop();
  }

  @DynamicPropertySource
  public static void axonProperties(final DynamicPropertyRegistry registry) {
    AxonServerContainerSpring.addDynamicProperties(AXON, registry);
  }

}
