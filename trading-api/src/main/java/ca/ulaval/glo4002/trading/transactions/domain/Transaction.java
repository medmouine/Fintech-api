package ca.ulaval.glo4002.trading.transactions.domain;

import ca.ulaval.glo4002.trading.accounts.domain.AccountNumber;
import ca.ulaval.glo4002.trading.accounts.domain.StockWallet;
import ca.ulaval.glo4002.trading.markets.domain.Market;
import ca.ulaval.glo4002.trading.stocks.domain.Stock;
import ca.ulaval.glo4002.trading.transactions.domain.fees.FeesCalculator;
import ca.ulaval.glo4002.trading.transactions.domain.fees.RegularFeesCalculator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.javamoney.moneta.Money;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
public abstract class Transaction {

  private final UUID transactionNumber;
  private final AccountNumber accountNumber;
  private final Instant date;
  private final Stock stock;
  private final long quantity;
  private final Money stockUnitPrice;
  private final FeesCalculator feesCalculator;

  public abstract void execute(final StockWallet wallet);

  public abstract Money getTotalPrice();

  public boolean isWithStock(final Stock otherStock) {
    return this.stock.equals(otherStock);
  }

  public Market getMarket() {
    return this.stock.getMarket();
  }

  public Money getFees() {
    return this.feesCalculator.calculateTransactionFees(this.stockUnitPrice, this.quantity);
  }
  public float getFeesFloatValue() {
    return this.getFees().getNumberStripped().floatValue();
  }

  public float getStockUnitPriceFloatValue() {
    return this.stockUnitPrice.getNumberStripped().floatValue();
  }
}
