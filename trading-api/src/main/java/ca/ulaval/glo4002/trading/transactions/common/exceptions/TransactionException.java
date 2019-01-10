package ca.ulaval.glo4002.trading.transactions.common.exceptions;

import ca.ulaval.glo4002.trading.common.exceptions.TradingException;
import lombok.Getter;

import java.util.UUID;

@Getter
public abstract class TransactionException extends TradingException {
  private final UUID transactionNumber;

  public TransactionException(final String error, final String description, UUID transactionNumber) {
    super(error, description);
    this.transactionNumber = transactionNumber;
  }
}
