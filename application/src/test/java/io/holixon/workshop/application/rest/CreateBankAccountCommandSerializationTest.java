package io.holixon.workshop.application.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.holixon.workshop.context.bankaccount.api.command.CreateBankAccountCommand;
import io.holixon.workshop.infrastructure.jackson.HoliBankObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CreateBankAccountCommandSerializationTest {

  private final ObjectMapper objectMapper = new HoliBankObjectMapper();

  @Test
  public void should_deserialize() throws JsonProcessingException {
    var command = objectMapper.readValue("{\"accountId\":\"2\",\"initialBalance\":100}", CreateBankAccountCommand.class);
    assertThat(command.accountId()).isEqualTo("2");
    assertThat(command.initialBalance()).isEqualTo(100);
  }
}
