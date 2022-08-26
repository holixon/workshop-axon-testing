package io.holixon.workshop.context.bankaccount.command.exception;

import io.holixon.workshop.context.bankaccount.command.BankAccountAggregate;

public class InsufficientBalanceException extends RuntimeException{

  public InsufficientBalanceException(String accountId, int currentBalance, int withdrawAmount) {
    super(String.format("BankAccount[id=%s, currentBalance=%d]: Withdrawal of amount=%d not allowed, would subceed min. balance of %d",
      accountId,
      currentBalance,
      withdrawAmount,
      BankAccountAggregate.MIN_BALANCE
    ));
  }
}
