package ca.ulaval.glo4002.trading.transactions.domain.exceptions;

import ca.ulaval.glo4002.trading.markets.domain.Market;
import ca.ulaval.glo4002.trading.transactions.common.exceptions.TransactionException;

import java.util.UUID;

public class MarketClosedException extends TransactionException {

  public MarketClosedException(final Market market, final UUID transactionNumber) {
    super("MARKET_CLOSED", String.format("market '%s' is closed", market.toString()), transactionNumber);
  }
}
