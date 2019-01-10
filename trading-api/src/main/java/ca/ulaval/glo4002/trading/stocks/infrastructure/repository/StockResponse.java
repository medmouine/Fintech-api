package ca.ulaval.glo4002.trading.stocks.infrastructure.repository;

import ca.ulaval.glo4002.trading.transactions.controller.exceptions.InvalidDateException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
class StockResponse {
  private String market;
  private String symbol;
  private StockType type;
  private List<Price> prices;

  float getPriceAtDate(final Instant date) {
    for (final Price price : this.prices) {
      if (price.isOnDay(date)) {
        return price.getPrice();
      }
    }
    throw new InvalidDateException();
  }
}
