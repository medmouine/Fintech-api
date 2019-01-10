package ca.ulaval.glo4002.trading.transactions.controller.exceptions;

import ca.ulaval.glo4002.trading.common.exceptions.TradingException;
import ca.ulaval.glo4002.trading.stocks.domain.Stock;

public class StockNotFoundException extends TradingException {
  private static final long serialVersionUID = -8253538390515802910L;

  public StockNotFoundException(final Stock stock) {
    super("STOCK_NOT_FOUND",
            String.format("stock '%s:%s' not found", stock.getSymbol(), stock.getMarket()));
  }
}
