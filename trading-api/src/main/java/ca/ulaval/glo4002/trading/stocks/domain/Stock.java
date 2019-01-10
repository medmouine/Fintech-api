package ca.ulaval.glo4002.trading.stocks.domain;

import ca.ulaval.glo4002.trading.markets.domain.Market;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class Stock {

  private Market market;

  private String symbol;
}