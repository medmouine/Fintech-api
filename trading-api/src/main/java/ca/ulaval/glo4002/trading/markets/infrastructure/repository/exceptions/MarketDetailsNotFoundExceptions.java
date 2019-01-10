package ca.ulaval.glo4002.trading.markets.infrastructure.repository.exceptions;

import ca.ulaval.glo4002.trading.markets.domain.Market;

public class MarketDetailsNotFoundExceptions extends MarketException {
  public MarketDetailsNotFoundExceptions(final String marketSymbol) {
    super("MARKET_DETAILS_NOT_FOUND", String.format("Could not find details for market '%s'", marketSymbol));
  }
}
