package io.holixon.workshop.context.bankaccount.command.exception;

import io.holixon.workshop.context.bankaccount.command.BankAccountAggregate;

public class MaximumBalanceExceededException extends RuntimeException {


  public MaximumBalanceExceededException(String accountId, int currentBalance, int depositAmount) {
    super(String.format("BankAccount[id=%s, currentBalance=%d]: Deposit of amount=%d not allowed, would exceed max. balance of %d",
      accountId,
      currentBalance,
      depositAmount,
      BankAccountAggregate.MAX_BALANCE
    ));
  }
}
