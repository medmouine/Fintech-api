package ca.ulaval.glo4002.trading.stocks.infrastructure.repository;

import ca.ulaval.glo4002.trading.markets.domain.Market;
import ca.ulaval.glo4002.trading.stocks.domain.Stock;
import ca.ulaval.glo4002.trading.stocks.domain.StocksRepository;
import ca.ulaval.glo4002.trading.transactions.controller.exceptions.StockNotFoundException;
import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import javax.money.CurrencyUnit;
import java.time.Instant;

@Repository
public class StocksAPIRepository implements StocksRepository {

  private static final String repositoryUrl = "http://localhost:8080/stocks/%s/%s";
  private final RestTemplate restTemplate;

  @Autowired
  public StocksAPIRepository(final RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @Override
  public Money findPriceBy(final Stock stock, final Instant date) {
    final String url = String.format(repositoryUrl, stock.getMarket(), stock.getSymbol());
    final StockResponse response;
    try {
      response = this.restTemplate.getForObject(url, StockResponse.class);
    } catch (final Exception e) {
      throw new StockNotFoundException(stock);
    }
    if (response == null) {
      throw new StockNotFoundException(stock);
    }
    final float priceAmount = response.getPriceAtDate(date);
    final CurrencyUnit currency = Market.valueOf(response.getMarket()).getCurrencyUnit();
    final Money price = Money.of(priceAmount, currency);
    return price;
  }
}

