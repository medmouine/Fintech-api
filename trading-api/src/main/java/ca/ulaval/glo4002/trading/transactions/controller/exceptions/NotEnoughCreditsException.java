package ca.ulaval.glo4002.trading.transactions.controller.exceptions;

import ca.ulaval.glo4002.trading.transactions.common.exceptions.TransactionException;

import java.util.UUID;

public class NotEnoughCreditsException extends TransactionException {
  private static final long serialVersionUID = -648566252455197275L;

  public NotEnoughCreditsException(final UUID transactionNumber) {
    super("NOT_ENOUGH_CREDITS", "not enough credits in wallet", transactionNumber);
  }
}
