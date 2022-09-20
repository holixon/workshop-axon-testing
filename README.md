# workshop-axon-testing

Example application for axon testing workshop

## Domain/Context

The Holi-Bank is a very simple banking service.

The root aggregate is the BankAccountAggregate with identifier `accountId` and property: `currentBalance`.

Holi Bank does not believe in small coins, so all amount and balance are positive Integers.

We never lend money to anyone, so the min. balance of an account is `0`.
We also believe that no user should ever want nor need to own more than `1000` "money", so there is a hard max balance limit that should never be exceeded.

## Use cases

### Create Bank Account

* set accountId and initial balance

Rules:

* bank account must not exist by accountId

### ATM Deposit Money

* increase the balance of the account by given amount

Rules:

* cmd must have positive amount 
* the amount is added to the current balance
* the new current balance must not exceed 1000 or a MaxBalanceExceededException will be thrown.

### ATM Withdraw Money

* decreases the balance of the account by given amount

Rules:

* cmd must have positive amount
* the amount is subtracted from the current balance
* the new current balance must not subceed 0 or an InsufficientBalanceException will be thrown.

### Money transfer

* transfer an amount from one account to another

Rules:

* after the transfer ends successfully, the source account balance is decreased by the transferred amount
* after the transfer ends successfully, the target account balance is increased by the transferred amount
* the above min max rules apply for both accounts, so the transfer must not succeed when the target balance would exceed 1000 or the source balance would subceed 0
* the amount to be transferred is reserved for the source account, so while a transfer is in progress, the rules for withdrawal are applied to `balance - transferAmount`

## Classes

### Domain model tests

