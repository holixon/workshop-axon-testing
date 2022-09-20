package io.holixon.workshop.context.bankaccount.command;

public record MoneyTransferIdGeneratorFake(
  String id
) implements MoneyTransferIdGenerator {

  @Override
  public String get() {
    return id;
  }
}
