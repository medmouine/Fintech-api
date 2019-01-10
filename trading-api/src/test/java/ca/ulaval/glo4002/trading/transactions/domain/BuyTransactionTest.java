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
public class BuyTransactionTest {
  private static final UUID BUY_TRANSACTION_NUMBER = UUID.randomUUID();
  private static final Long A_TRANSACTION_QUANTITY = 10L;
  private static final String USD = "USD";
  private static final Money STOCK_UNIT_PRICE = Money.of(10, USD);
  private static final AccountNumber AN_ACCOUNT_NUMBER = new AccountNumber("VR-123");
  private static final Market MARKET = Market.NASDAQ;
  private static final Stock A_STOCK = new Stock(MARKET, "GOOG");
  private static final Money FEES_AMOUNT = Money.of(5, USD);

  @Mock
  private StockWallet stockWallet;

  @Mock
  private FeesCalculator feesCalculator;

  private BuyTransaction buyTransaction;

  @Before
  public void setUp() {
    this.buyTransaction = new BuyTransaction(BUY_TRANSACTION_NUMBER, AN_ACCOUNT_NUMBER, Instant.now(), A_STOCK, A_TRANSACTION_QUANTITY, STOCK_UNIT_PRICE, this.feesCalculator);
    when(this.feesCalculator.calculateTransactionFees(STOCK_UNIT_PRICE, A_TRANSACTION_QUANTITY)).thenReturn(FEES_AMOUNT);
  }

  @Test
  public void whenExecutingTheTransaction_thenTheTransactionIsExecutedOnTheStockWallet() {
    this.buyTransaction.execute(this.stockWallet);

    verify(this.stockWallet, times(1)).buyStocks(this.buyTransaction.getTransactionNumber(), this.buyTransaction.getTotalPrice());
  }

  @Test
  public void whenExecutingTheTransaction_thenTheTotalPriceIncludesFees() {
    final Money priceWithoutFees = this.buyTransaction.getStockUnitPrice().multiply(this.buyTransaction.getQuantity());

    this.buyTransaction.execute(this.stockWallet);

    assertEquals(priceWithoutFees.add(FEES_AMOUNT), this.buyTransaction.getTotalPrice());
  }
}