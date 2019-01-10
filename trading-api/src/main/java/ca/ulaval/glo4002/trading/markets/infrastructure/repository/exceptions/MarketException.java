package ca.ulaval.glo4002.trading.markets.infrastructure.repository.exceptions;

import ca.ulaval.glo4002.trading.common.exceptions.TradingException;

public abstract class MarketException extends TradingException {
  public MarketException(final String error, final String description) {
    super(error, description);
  }
}
