package ca.ulaval.glo4002.trading.transactions.domain;

import ca.ulaval.glo4002.trading.accounts.domain.AccountNumber;
import ca.ulaval.glo4002.trading.accounts.domain.StockWallet;
import ca.ulaval.glo4002.trading.markets.domain.Market;
import ca.ulaval.glo4002.trading.stocks.domain.Stock;
import ca.ulaval.glo4002.trading.transactions.domain.fees.FeesCalculator;
import org.javamoney.moneta.Money;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.Instant;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class TransactionTest {

  private static final UUID TRANSACTION_NUMBER = UUID.randomUUID();
  private static final Long A_TRANSACTION_QUANTITY = 10L;
  private static final String CAD = "CAD";
  private static final Money STOCK_UNIT_PRICE = Money.of(10, CAD);
  private static final AccountNumber AN_ACCOUNT_NUMBER = new AccountNumber("VR-123");
  private static final Market MARKET = Market.NASDAQ;
  private static final Stock A_STOCK = new Stock(MARKET, "GOOG");
  private static final Stock ANOTHER_STOCK = new Stock(MARKET, "GOOG");

  @Mock
  private FeesCalculator feesCalculator;

  private Transaction transaction = new Transaction(TRANSACTION_NUMBER,
          AN_ACCOUNT_NUMBER,
          Instant.now(),
          A_STOCK,
          A_TRANSACTION_QUANTITY,
          STOCK_UNIT_PRICE,
          this.feesCalculator) {
    @Override
    public void execute(final StockWallet wallet) {
    }

    @Override
    public Money getTotalPrice() {
      return null;
    }
  };

  @Test
  public void givenSameStock_whenValidationTransactionIsWithStock_thenReturnTrue() {
    assertTrue(transaction.isWithStock(A_STOCK));
  }

  @Test
  public void givenNotSameStock_whenValidationTransactionIsWithStock_thenReturnTrue() {
    assertTrue(transaction.isWithStock(ANOTHER_STOCK));
  }

  @Test
  public void whenGetMarketIsCalled_thenReturnMarket() {
    assertEquals(transaction.getMarket(), MARKET);
  }
}
