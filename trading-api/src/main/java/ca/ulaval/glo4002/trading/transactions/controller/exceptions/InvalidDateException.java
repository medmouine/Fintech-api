package ca.ulaval.glo4002.trading.transactions.controller.exceptions;

import ca.ulaval.glo4002.trading.common.exceptions.TradingException;

public class InvalidDateException extends TradingException {
  private static final long serialVersionUID = 2281448699906100542L;

  public InvalidDateException() {
    super("INVALID_DATE", "the transaction date is invalid");
  }
}
