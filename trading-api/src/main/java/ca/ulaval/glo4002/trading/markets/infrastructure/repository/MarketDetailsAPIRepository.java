package ca.ulaval.glo4002.trading.markets.infrastructure.repository;

import ca.ulaval.glo4002.trading.markets.domain.MarketDetailsRepository;
import ca.ulaval.glo4002.trading.markets.domain.response.MarketDetailsResponse;
import ca.ulaval.glo4002.trading.markets.infrastructure.repository.exceptions.MarketDetailsNotFoundExceptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MarketDetailsAPIRepository implements MarketDetailsRepository {

  private static final String marketBySymbolUrl = "http://localhost:8080/markets/%s";
  private final RestTemplate restTemplate;

  @Autowired
  MarketDetailsAPIRepository(RestTemplate restTemplate){
    this.restTemplate = restTemplate;
  }

  @Override
  public MarketDetailsResponse findMarketDetailsBySymbol(final String marketSymbol) throws MarketDetailsNotFoundExceptions{
    final String url = String.format(marketBySymbolUrl, marketSymbol);

    final MarketDetailsResponse market = this.restTemplate.getForObject(url, MarketDetailsResponse.class);

    if (market == null) {
      throw new MarketDetailsNotFoundExceptions(marketSymbol);
    }
    return market;
  }
}
