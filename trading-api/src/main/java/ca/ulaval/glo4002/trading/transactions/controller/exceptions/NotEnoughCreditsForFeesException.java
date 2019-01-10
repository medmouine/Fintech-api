package ca.ulaval.glo4002.trading.transactions.controller.exceptions;

import ca.ulaval.glo4002.trading.transactions.common.exceptions.TransactionException;

import java.util.UUID;

public class NotEnoughCreditsForFeesException extends TransactionException {
  private static final long serialVersionUID = -648566252455197275L;

  public NotEnoughCreditsForFeesException(final UUID transactionNumber) {
    super("NOT_ENOUGH_CREDITS", "Not enough credits to pay the transaction fee", transactionNumber);
  }
}
