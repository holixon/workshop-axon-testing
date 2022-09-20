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

The following branches are available for you to simplify the execution of classes:

* `initial` Working application without any tests.
* `class/0-prepare` Preparation for TDD-style fixture-based command model implementation.
* `class/1-command-model` Solution for command model tests, preparation for query model tests.
* `class/2-query-model` Solution for query model tests, preparation for Spring Integration tests.
* `class/3-integration` Solution for Spring integration tests, preparation for MockMVC REST E2E tests.
* `class/4-rest-e2e` Solution for MockMVC REST E2E tests.

Use the command `git checkout <branch-name>` to switch the branches. The branch represents the solution for the class.
Check out the previous branch and work on the class. (For example, checkout `class/0-prepare` to execute the class 1 and then
check the `class/1-command-model` to see the solution).

