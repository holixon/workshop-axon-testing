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

  @Test
  void decrease_value() {
    assertThat(new BalanceModel(10, 0, 100)
                 .decrease(5).value()
    ).isEqualTo(5);
  }

  @Test
  void can_decrease_value() {
    var model = new BalanceModel(10, 0, 100);
    assertThat(model.canDecreaseBalance(11, 0)).isEqualTo(false);
    assertThat(model.canDecreaseBalance(8, 3)).isEqualTo(false);
    assertThat(model.canDecreaseBalance(8, 2)).isEqualTo(true);
    assertThat(model.canDecreaseBalance(8, 0)).isEqualTo(true);
  }

  @Test
  void can_increase_value() {
    var model = new BalanceModel(10, 0, 100);
    assertThat(model.canIncreaseBalance(91)).isEqualTo(false);
    assertThat(model.canIncreaseBalance(90)).isEqualTo(true);
    assertThat(model.canIncreaseBalance(80)).isEqualTo(true);


    // use strong validation in your domain model types!
    // assertThat(model.canIncreaseBalance(-100)).isEqualTo(false);
  }

}
