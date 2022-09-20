package io.holixon.workshop.context.bankaccount.test.spring;

import com.tngtech.jgiven.integration.spring.EnableJGiven;
import com.tngtech.jgiven.integration.spring.junit5.DualSpringScenarioTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest(classes = TestConfiguration.class)
@EnableJGiven
public class BankAccountUseCasesSpringITest extends DualSpringScenarioTest<BankAccountActionStage, BankAccountAssertStage> {

  @Test
  void create_account_and_query_balance() {
    String account1 = accountId();

    given()
      .create_bankAccount(account1, 100);

    then()
      .account_has_balance(account1, 100);
  }

  @Test
  void withdraw_amount() {
    String account1 = accountId();

    given()
      .create_bankAccount(account1, 100);

    when()
      .withdraw(account1, 10);

    then()
      .account_has_balance(account1, 90);
  }

  @Test
  void deposit_amount() {
    String account1 = accountId();

    given()
      .create_bankAccount(account1, 100)
    ;

    when()
      .deposit(account1, 10)
    ;

    then()
      .account_has_balance(account1, 110)
    ;
  }

  @Test
  void transfer_10_from_source_to_target() {
    String account1 = accountId();
    String account2 = accountId();

    given()
      .create_bankAccount(account1, 100)
      .and()
      .create_bankAccount(account2, 0)
    ;
    when()
      .transfer(account1, account2, 30)
    ;

    then()
      .account_has_balance(account1, 70)
      .and()
      .account_has_balance(account2, 30)
    ;
  }

  @Test
  void transfer_fails_when_target_would_exceed_maxBalance() {
    String account1 = accountId();
    String account2 = accountId();

    given()
      .create_bankAccount(account1, 100)
      .and()
      .create_bankAccount(account2, 990)
    ;

    when()
      .transfer(account1, account2, 30)
    ;

    then()
      .account_has_balance(account1, 100)
      .and()
      .account_has_balance(account2, 990)
    ;
  }

  private String accountId() {
    return UUID.randomUUID().toString();
  }

}
