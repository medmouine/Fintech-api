package ca.ulaval.glo4002.trading.transactions.domain;

import ca.ulaval.glo4002.trading.accounts.domain.AccountNumber;
import ca.ulaval.glo4002.trading.accounts.domain.StockWallet;
import ca.ulaval.glo4002.trading.stocks.domain.Stock;
import ca.ulaval.glo4002.trading.transactions.domain.fees.FeesCalculator;
import lombok.Getter;
import org.javamoney.moneta.Money;

import java.time.Instant;
import java.util.UUID;

@Getter
public class BuyTransaction extends Transaction {

  public BuyTransaction(
          final UUID transactionNumber,
          final AccountNumber accountNumber,
          final Instant date,
          final Stock stock,
          final long quantity,
          final Money stockUnitPrice,
          final FeesCalculator feesCalculator) {
    super(transactionNumber, accountNumber, date, stock, quantity, stockUnitPrice, feesCalculator);
  }

  @Override
  public void execute(final StockWallet wallet) {
    wallet.buyStocks(this.getTransactionNumber(), this.getTotalPrice());
  }

  @Override
  public Money getTotalPrice() {
    return (this.getStockUnitPrice().multiply(this.getQuantity())).add(this.getFees());
  }
}
