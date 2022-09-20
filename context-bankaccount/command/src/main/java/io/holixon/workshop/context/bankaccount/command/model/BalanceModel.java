package io.holixon.workshop.context.bankaccount.command.model;

public record BalanceModel(
  int value,
  int minimum,
  int maximum
) {

  public boolean canIncreaseBalance(int amount) {
    return value + amount <= maximum;
  }

  public boolean canDecreaseBalance(int amount, int reservedAmount) {
    return value - reservedAmount - amount >= minimum;
  }

  public BalanceModel increase(int amount) {
    return new BalanceModel(value + amount, minimum, maximum);
  }

  public BalanceModel decrease(int amount) {
    return new BalanceModel(value - amount, minimum, maximum);
  }

}
