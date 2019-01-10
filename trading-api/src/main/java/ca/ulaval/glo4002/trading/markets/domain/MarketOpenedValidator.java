package ca.ulaval.glo4002.trading.markets.domain;

import ca.ulaval.glo4002.trading.markets.domain.response.MarketDetailsResponse;
import ca.ulaval.glo4002.trading.markets.infrastructure.repository.exceptions.MarketDetailsNotFoundExceptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class MarketOpenedValidator {

  private final MarketDetailsRepository marketDetailsRepository;

  @Autowired
  public MarketOpenedValidator(final MarketDetailsRepository marketDetailsRepository) {
    this.marketDetailsRepository = marketDetailsRepository;
  }

  public boolean validateAtDate(final Market marketSymbol, final Instant date) {
    try {
      MarketDetailsResponse marketDetailsResponse = this.marketDetailsRepository.findMarketDetailsBySymbol(marketSymbol.toString());
      return marketDetailsResponse.isOpenAtDate(date);
    } catch (MarketDetailsNotFoundExceptions e){
      return false;
    }
  }
}
