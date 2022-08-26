package io.holixon.workshop.context.bankaccount.command;

import java.util.UUID;
import java.util.function.Supplier;

public interface MoneyTransferIdGenerator extends Supplier<String> {

  MoneyTransferIdGenerator RANDOM = () -> UUID.randomUUID().toString();

}
