package io.holixon.workshop.context.bankaccount.command.model;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class BalanceModelTest {

  @Test
  void increase_value() {
    assertThat(new BalanceModel(10, 0, 100)
      .increase(10).value()
    ).isEqualTo(20);
  }
}
