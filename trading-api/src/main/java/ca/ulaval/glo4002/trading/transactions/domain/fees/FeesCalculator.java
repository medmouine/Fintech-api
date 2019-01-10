package ca.ulaval.glo4002.trading.transactions.domain.fees;

import ca.ulaval.glo4002.trading.transactions.domain.TransactionRequest;
import org.javamoney.moneta.Money;

public interface FeesCalculator {
  Money calculateTransactionFees(final Money stockUnitPrice, final long quantity);
}
