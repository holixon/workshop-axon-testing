package io.holixon.workshop.module.query;

import static org.assertj.core.api.Assertions.assertThat;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.junit5.DualScenarioTest;
import io.holixon.workshop.api.event.transfer.MoneyTransferCancelledEvent;
import io.holixon.workshop.api.event.transfer.MoneyTransferCompletedEvent;
import io.holixon.workshop.api.event.transfer.MoneyTransferRequestedEvent;
import io.holixon.workshop.api.query.MoneyTransfer;
import io.holixon.workshop.api.query.MoneyTransferByIdQuery;
import io.holixon.workshop.api.query.MoneyTransfersQuery;
import io.holixon.workshop.api.query.MoneyTransfersResponse;
import io.holixon.workshop.module.query.MoneyTransferProjectionTest.GivenWhenStage;
import io.holixon.workshop.module.query.MoneyTransferProjectionTest.ThenStage;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;

class MoneyTransferProjectionTest extends DualScenarioTest<GivenWhenStage, ThenStage> {

  public static final String ACCOUNT_A = "11111111";
  public static final String ACCOUNT_B = "22222222";
  public static final String ACCOUNT_C = "33333333";


  @Test
  void initially_empty() {
    given()
      .no_prior_activity();

    then()
      .no_transfers_exist();
  }

  @Test
  void requested_transfer_can_be_found() {
    given()
      .no_prior_activity();

    when()
      .transfer_requested(new MoneyTransferRequestedEvent("1", ACCOUNT_A, ACCOUNT_B, 50));

    then()
      .transfer_exists_with_state_$(new MoneyTransfer("1", ACCOUNT_A, ACCOUNT_B, 50));
  }


  @Test
  void transfer_can_be_completed() {
    given()
      .transfer_requested(new MoneyTransferRequestedEvent("1", ACCOUNT_A, ACCOUNT_B, 50));


    when()
      .transfer_completed(new MoneyTransferCompletedEvent("1", ACCOUNT_A, 50));

    then()
      .transfer_exists_with_state_$(new MoneyTransfer("1", ACCOUNT_A, ACCOUNT_B, 50, true, null));
  }

  @Test
  void transfer_can_be_cancelled() {
    given()
      .transfer_requested(new MoneyTransferRequestedEvent("1", ACCOUNT_A, ACCOUNT_B, 50));

    when()
      .transfer_cancelled(new MoneyTransferCancelledEvent("1", "some error"));

    then()
      .transfer_exists_with_state_$(new MoneyTransfer("1", ACCOUNT_A, ACCOUNT_B, 50, false, "some error"));
  }

  @Test
  void can_query_transfers_for_account() {
    given()
      .transfer_requested(new MoneyTransferRequestedEvent("1", ACCOUNT_A, ACCOUNT_B, 50))
      .and()
      .transfer_requested(new MoneyTransferRequestedEvent("2", ACCOUNT_B, ACCOUNT_C, 10))
      .and()
      .transfer_completed(new MoneyTransferCompletedEvent("1", ACCOUNT_A, 50))
      .and()
      .transfer_completed(new MoneyTransferCompletedEvent("2", ACCOUNT_B, 10))
    ;

    when()
      .query_transfers_for_account(new MoneyTransfersQuery(ACCOUNT_A))
    ;

    then()
      .query_response_contains(
        new MoneyTransfer("1", ACCOUNT_A, ACCOUNT_B, 50, true, null)
      );
  }

  public static class GivenWhenStage extends Stage<GivenWhenStage> {

    @ProvidedScenarioState
    private final MoneyTransferProjection projection = new MoneyTransferProjection();

    @ProvidedScenarioState
    private final AtomicReference<MoneyTransfersResponse> found = new AtomicReference<>();

    public GivenWhenStage no_prior_activity() {
      return self();
    }

    public GivenWhenStage transfer_requested(MoneyTransferRequestedEvent evt) {
      projection.on(evt);
      return self();
    }

    public GivenWhenStage transfer_completed(MoneyTransferCompletedEvent evt) {
      projection.on(evt);
      return self();
    }

    public GivenWhenStage transfer_cancelled(MoneyTransferCancelledEvent evt) {
      projection.on(evt);
      return self();
    }

    public GivenWhenStage query_transfers_for_account(MoneyTransfersQuery query) {
      found.set(projection.query(query));
      return self();
    }


  }
  public static class ThenStage extends Stage<ThenStage> {

    @ExpectedScenarioState
    private  MoneyTransferProjection projection;


    @ProvidedScenarioState
    private AtomicReference<MoneyTransfersResponse> found;


    public ThenStage no_transfers_exist() {
      assertThat(projection.findAll().moneyTransfers()).isEmpty();

      return self();
    }

    public ThenStage transfer_exists_with_state_$(MoneyTransfer expected) {
      MoneyTransfer moneyTransfer = projection.query(new MoneyTransferByIdQuery(expected.moneyTransferId())).orElseThrow(() -> new IllegalStateException("no transfer found for id=" + expected.moneyTransferId()));

      assertThat(moneyTransfer).isEqualTo(expected);

      return self();
    }

    public ThenStage query_response_contains(MoneyTransfer... expected) {

      assertThat(found.get().moneyTransfers()).containsExactlyInAnyOrder(expected);

      return self();
    }

  }
}
