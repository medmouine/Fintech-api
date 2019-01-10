package ca.ulaval.glo4002.trading.common.infrastructure.currencyConverter.exceptions;

import ca.ulaval.glo4002.trading.common.exceptions.TradingException;

public class InvalidCurrencyException extends TradingException {
  public InvalidCurrencyException(final String expectedCurrency, final String actualCurrency) {
    super("INVALID_CURRENCY_EXCEPTION", String.format("Expected currency to be %s but got %s", expectedCurrency, actualCurrency));
  }
}
