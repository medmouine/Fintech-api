package ca.ulaval.glo4002.trading.transactions.controller.exceptions;

import ca.ulaval.glo4002.trading.transactions.common.exceptions.TransactionException;

import java.util.UUID;

public class StockParametersDontMatchException extends TransactionException {
  private static final long serialVersionUID = -1180526781109028587L;

  public StockParametersDontMatchException(final UUID transactionNumber) {
    super("STOCK_PARAMETERS_DONT_MATCH", "stock parameters don't match existing ones", transactionNumber);
  }
}
