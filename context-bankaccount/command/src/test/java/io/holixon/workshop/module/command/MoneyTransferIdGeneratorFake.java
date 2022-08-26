package io.holixon.workshop.module.command;

public record MoneyTransferIdGeneratorFake(
  String id
) implements MoneyTransferIdGenerator {

  @Override
  public String get() {
    return id;
  }
}
