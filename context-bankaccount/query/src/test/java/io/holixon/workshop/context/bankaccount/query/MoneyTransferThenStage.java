package io.holixon.workshop.context.bankaccount.query;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import io.holixon.workshop.context.bankaccount.api.query.MoneyTransfer;
import io.holixon.workshop.context.bankaccount.api.query.MoneyTransferByIdQuery;
import io.holixon.workshop.context.bankaccount.api.query.MoneyTransfersResponse;

import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

public class MoneyTransferThenStage extends Stage<MoneyTransferThenStage> {

  @ExpectedScenarioState
  private MoneyTransferProjection projection;


  @ProvidedScenarioState
  private AtomicReference<MoneyTransfersResponse> found;


  public MoneyTransferThenStage no_transfers_exist() {
    assertThat(projection.findAll().moneyTransfers()).isEmpty();

    return self();
  }

  public MoneyTransferThenStage transfer_exists_with_state_$(MoneyTransfer expected) {
    MoneyTransfer moneyTransfer = projection.query(new MoneyTransferByIdQuery(expected.moneyTransferId()))
                                            .orElseThrow(() -> new IllegalStateException("no transfer found for id=" + expected.moneyTransferId()));

    assertThat(moneyTransfer).isEqualTo(expected);

    return self();
  }

  public MoneyTransferThenStage query_response_contains(MoneyTransfer... expected) {

    assertThat(found.get().moneyTransfers()).containsExactlyInAnyOrder(expected);

    return self();
  }

}
