package ca.ulaval.glo4002.trading.transactions.domain;

import ca.ulaval.glo4002.trading.accounts.domain.AccountNumber;
import ca.ulaval.glo4002.trading.accounts.domain.StockWallet;
import ca.ulaval.glo4002.trading.markets.domain.Market;
import ca.ulaval.glo4002.trading.stocks.domain.Stock;
import ca.ulaval.glo4002.trading.transactions.domain.fees.FeesCalculator;
import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.Instant;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SellTransactionTest {

  private static final Market A_MARKET = Market.NASDAQ;
  private static final String USD = "USD";
  private static final UUID BUY_TRANSACTION_NUMBER = UUID.randomUUID();
  private static final Money STOCK_UNIT_PRICE = Money.of(10, USD);
  private static final Long A_TRANSACTION_QUANTITY = 10L;
  private static final UUID SELL_TRANSACTION_NUMBER = UUID.randomUUID();
  private static final AccountNumber AN_ACCOUNT_NUMBER = new AccountNumber("VR-123");
  private static final Money FEES_AMOUNT = Money.of(5, USD);

  @Mock
  private FeesCalculator feesCalculator;

  @Mock
  private StockWallet stockWallet;

  private static final Stock A_STOCK = new Stock(A_MARKET, "GOOG");
  private SellTransaction sellTransaction;

  @Before
  public void setup () {
    this.sellTransaction = new SellTransaction(SELL_TRANSACTION_NUMBER, AN_ACCOUNT_NUMBER,
            Instant.now(),
            A_STOCK, A_TRANSACTION_QUANTITY,
            STOCK_UNIT_PRICE,
            this.feesCalculator,
            BUY_TRANSACTION_NUMBER);
    when(this.feesCalculator.calculateTransactionFees(STOCK_UNIT_PRICE, A_TRANSACTION_QUANTITY)).thenReturn(FEES_AMOUNT);

  }

  @Test
  public void whenExecutingTheTransaction_thenTheTransactionIsExecutedOnTheStockWallet() {
    this.sellTransaction.execute(this.stockWallet);

    verify(this.stockWallet, times(1)).sellStocks(this.sellTransaction.getAssociatedBuyTransactionNumber(), this.sellTransaction.getQuantity(),
            this.sellTransaction.getTotalPrice(), this.sellTransaction.getStock(), this.sellTransaction.getFees());
  }

  @Test
  public void whenExecutingTheTransaction_thenTheTotalPriceSubtractsFees() {
    final Money priceWithoutFees = this.sellTransaction.getStockUnitPrice().multiply(this.sellTransaction.getQuantity());

    this.sellTransaction.execute(this.stockWallet);

    assertEquals(priceWithoutFees.subtract(FEES_AMOUNT), this.sellTransaction.getTotalPrice());
  }
}
