package ca.ulaval.glo4002.trading.transactions.domain;

import ca.ulaval.glo4002.trading.accounts.domain.AccountNumber;
import ca.ulaval.glo4002.trading.accounts.domain.StockWallet;
import ca.ulaval.glo4002.trading.stocks.domain.Stock;
import ca.ulaval.glo4002.trading.transactions.domain.fees.FeesCalculator;
import ca.ulaval.glo4002.trading.transactions.domain.fees.RegularFeesCalculator;
import lombok.Getter;
import org.javamoney.moneta.Money;

import java.time.Instant;
import java.util.UUID;

@Getter
public class SellTransaction extends Transaction {

  private final UUID associatedBuyTransactionNumber;

  public SellTransaction(final UUID transactionNumber,
                         final AccountNumber accountNumber,
                         final Instant date,
                         final Stock stock,
                         final long quantity,
                         final Money stockUnitPrice,
                         final FeesCalculator feesCalculator,
                         final UUID associatedBuyTransaction) {
    super(transactionNumber, accountNumber, date, stock, quantity, stockUnitPrice, feesCalculator);

    this.associatedBuyTransactionNumber = associatedBuyTransaction;
  }

  @Override
  public void execute(final StockWallet wallet) {
    wallet.sellStocks(this.associatedBuyTransactionNumber, this.getQuantity(), this.getTotalPrice(), this.getStock(), this.getFees());
  }

  @Override
  public Money getTotalPrice() {
    return (this.getStockUnitPrice().multiply(this.getQuantity())).subtract(this.getFees());
  }
}
