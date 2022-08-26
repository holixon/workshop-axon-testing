package io.holixon.workshop.module.command;

import java.util.UUID;
import java.util.function.Supplier;

public interface MoneyTransferIdGenerator extends Supplier<String> {

  MoneyTransferIdGenerator RANDOM = () -> UUID.randomUUID().toString();

}
