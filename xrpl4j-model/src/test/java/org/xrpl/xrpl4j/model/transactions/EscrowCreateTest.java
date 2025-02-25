package org.xrpl.xrpl4j.model.transactions;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.common.primitives.UnsignedInteger;
import com.google.common.primitives.UnsignedLong;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link EscrowCreate}.
 */
public class EscrowCreateTest {


  @Test
  public void testWithNeitherCancelNorFinish() {
    EscrowCreate.builder()
      .sequence(UnsignedInteger.ONE)
      .fee(XrpCurrencyAmount.ofDrops(1))
      .account(Address.of("account"))
      .amount(XrpCurrencyAmount.ofDrops(1))
      .destination(Address.of("destination"))
      .build();
  }

  @Test
  public void testCancelBeforeFinish() {
    assertThrows(
      IllegalStateException.class,
      () -> EscrowCreate.builder()
        .sequence(UnsignedInteger.ONE)
        .fee(XrpCurrencyAmount.ofDrops(1))
        .account(Address.of("account"))
        .amount(XrpCurrencyAmount.ofDrops(1))
        .destination(Address.of("destination"))
        .cancelAfter(UnsignedLong.ONE)
        .finishAfter(UnsignedLong.valueOf(2L))
        .build(),
      "If both CancelAfter and FinishAfter are specified, the FinishAfter time must be before the CancelAfter time."
    );
  }

  @Test
  public void testCancelAfterFinish() {
    EscrowCreate.builder()
      .sequence(UnsignedInteger.ONE)
      .fee(XrpCurrencyAmount.ofDrops(1))
      .account(Address.of("account"))
      .amount(XrpCurrencyAmount.ofDrops(1))
      .destination(Address.of("destination"))
      .cancelAfter(UnsignedLong.valueOf(2L))
      .finishAfter(UnsignedLong.ONE)
      .build();
  }

  @Test
  public void testCancelEqualsFinish() {
    assertThrows(
      IllegalStateException.class,
      () -> EscrowCreate.builder()
        .sequence(UnsignedInteger.ONE)
        .fee(XrpCurrencyAmount.ofDrops(1))
        .account(Address.of("account"))
        .amount(XrpCurrencyAmount.ofDrops(1))
        .destination(Address.of("destination"))
        .cancelAfter(UnsignedLong.ONE)
        .finishAfter(UnsignedLong.ONE)
        .build(),
      "The DepositPreAuth transaction must include either Authorize or Unauthorize, but not both."
    );
  }

}
