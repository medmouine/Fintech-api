package ca.ulaval.glo4002.trading.markets.domain;

import ca.ulaval.glo4002.trading.markets.domain.response.MarketDetailsResponse;
import ca.ulaval.glo4002.trading.markets.infrastructure.repository.exceptions.MarketDetailsNotFoundExceptions;

public interface MarketDetailsRepository {
  MarketDetailsResponse findMarketDetailsBySymbol(String marketSymbol) throws MarketDetailsNotFoundExceptions;
}
