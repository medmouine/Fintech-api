package ca.ulaval.glo4002.trading.accounts.domain.exceptions;

import ca.ulaval.glo4002.trading.transactions.common.exceptions.TransactionException;
import lombok.Getter;

import java.util.UUID;

@Getter
public class TransactionNotFoundException extends TransactionException {

  private static final long serialVersionUID = -6580198629223297800L;

  public TransactionNotFoundException(final UUID transactionNumber) {
    super("TRANSACTION_NOT_FOUND", String.format("transaction with number %s not found", transactionNumber), transactionNumber);
  }
}
