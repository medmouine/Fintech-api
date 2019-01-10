package ca.ulaval.glo4002.trading.transactions.controller.exceptions;

import ca.ulaval.glo4002.trading.stocks.domain.Stock;
import ca.ulaval.glo4002.trading.transactions.common.exceptions.TransactionException;
import lombok.Getter;

import java.util.UUID;

@Getter
public class NotEnoughStockException extends TransactionException {
  private static final long serialVersionUID = 3681738507233356751L;

  public NotEnoughStockException(final UUID transactionNumber, Stock stock) {
    super("NOT_ENOUGH_STOCK",
            String.format("not enough stock '%s:%s'", stock.getSymbol(), stock.getMarket()),
            transactionNumber);
  }
}
