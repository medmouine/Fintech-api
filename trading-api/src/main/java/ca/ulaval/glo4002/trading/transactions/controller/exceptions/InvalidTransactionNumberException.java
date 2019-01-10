package ca.ulaval.glo4002.trading.transactions.controller.exceptions;

import ca.ulaval.glo4002.trading.transactions.common.exceptions.TransactionException;

import java.util.UUID;

public class InvalidTransactionNumberException extends TransactionException {
  private static final long serialVersionUID = -3729359262947574258L;

  public InvalidTransactionNumberException(UUID transactionNumber) {
    super("INVALID_TRANSACTION_NUMBER", "the transactions number is invalid", transactionNumber);
  }
}
