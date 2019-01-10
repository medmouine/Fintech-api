package ca.ulaval.glo4002.trading.transactions.domain;

import ca.ulaval.glo4002.trading.markets.domain.Market;
import ca.ulaval.glo4002.trading.stocks.domain.Stock;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.javamoney.moneta.Money;

import javax.money.CurrencyUnit;
import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
public class TransactionRequest {
  private final TransactionRequestType type;
  private final Instant date;
  private final Stock stock;
  private final UUID transactionNumber;
  private final long quantity;
  private final CurrencyUnit currencyUnit;

  public Money calculatePriceBeforeFees(final Money stockUnitPrice) {
    return stockUnitPrice.multiply(this.quantity);
  }

  public Market getMarket(){
    return this.stock.getMarket();
  }
}
