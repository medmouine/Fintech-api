package ca.ulaval.glo4002.trading.transactions.domain;

import ca.ulaval.glo4002.trading.accounts.domain.AccountNumber;
import ca.ulaval.glo4002.trading.markets.domain.Market;
import ca.ulaval.glo4002.trading.markets.domain.MarketOpenedValidator;
import ca.ulaval.glo4002.trading.stocks.domain.Stock;
import ca.ulaval.glo4002.trading.stocks.domain.StocksRepository;
import ca.ulaval.glo4002.trading.transactions.controller.exceptions.InvalidDateException;
import ca.ulaval.glo4002.trading.transactions.controller.exceptions.StockNotFoundException;
import ca.ulaval.glo4002.trading.transactions.domain.exceptions.MarketClosedException;
import ca.ulaval.glo4002.trading.transactions.domain.fees.FeesCalculator;
import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.money.CurrencyUnit;
import java.time.Instant;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TransactionFactoryTest {

  private static final String CAD = "CAD";
  private static final Instant A_DATE = Instant.parse("2018-09-13T00:00:00.000Z");
  private static final Market MARKET = Market.NASDAQ;
  private static final Market ANOTHER_MARKET = Market.NYSE;
  private static final Stock A_STOCK = new Stock(MARKET, "symbol");
  private static final CurrencyUnit A_CURRENCY = MARKET.getCurrencyUnit();
  private static final Money A_STOCK_UNIT_PRICE = Money.of(10, CAD);
  private static final UUID A_TRANSACTION_NUMBER = UUID.randomUUID();
  private static final AccountNumber AN_ACCOUNT_NUMBER = new AccountNumber("VR-123");
  private static final Money A_FEE = Money.of(20, CAD);
  private static final long A_QUANTITY = 10;
  private final TransactionRequest BuyTransactionRequest = new TransactionRequest(TransactionRequestType.BUY, A_DATE, A_STOCK, A_TRANSACTION_NUMBER, A_QUANTITY, A_CURRENCY);
  private final TransactionRequest SellTransactionRequest = new TransactionRequest(TransactionRequestType.SELL, A_DATE, A_STOCK, UUID.randomUUID(), A_QUANTITY, A_CURRENCY);
  @Mock
  private StocksRepository stocksRepository;
  @Mock
  private FeesCalculator feesCalculator;
  @Mock
  private MarketOpenedValidator marketOpenedValidator;
  @InjectMocks
  private TransactionFactory transactionFactory;

  @Before
  public void setUp() {
    when(this.stocksRepository.findPriceBy(A_STOCK, A_DATE)).thenReturn(A_STOCK_UNIT_PRICE);
    when(this.marketOpenedValidator.validateAtDate(A_STOCK.getMarket(), A_DATE)).thenReturn(true);
    when(this.feesCalculator.calculateTransactionFees(A_STOCK_UNIT_PRICE, A_QUANTITY)).thenReturn(A_FEE);
  }

  @Test
  public void givenASellTransactionRequest_whenCreatingATransaction_thenItShouldReturnASellTransaction() {
    final Transaction transaction = this.transactionFactory.create(AN_ACCOUNT_NUMBER, this.SellTransactionRequest);

    assertTrue(transaction instanceof SellTransaction);
  }

  @Test
  public void givenABuyTransactionRequest_whenCreatingATransaction_thenItShouldReturnABuyTransaction() {
    final Transaction transaction = this.transactionFactory.create(AN_ACCOUNT_NUMBER, this.BuyTransactionRequest);

    assertTrue(transaction instanceof BuyTransaction);
  }

  @Test
  public void whenCreatingATransaction_thenTheStockUnitPriceIsFoundForTheRequest() {
    this.transactionFactory.create(AN_ACCOUNT_NUMBER, this.BuyTransactionRequest);

    verify(this.stocksRepository).findPriceBy(A_STOCK, A_DATE);
  }

  @Test
  public void whenCreatingATransaction_thenTheStockUnitPriceFoundForTheRequestIsValid() {
    final Transaction transaction = this.transactionFactory.create(AN_ACCOUNT_NUMBER, this.BuyTransactionRequest);

    assertEquals(transaction.getStockUnitPrice(), A_STOCK_UNIT_PRICE);
  }

  @Test(expected = StockNotFoundException.class)
  public void whenStockNotFoundInRepository_thenStockNotFoundExceptionIsThrown() {
    when(this.stocksRepository.findPriceBy(A_STOCK, A_DATE)).thenThrow(new StockNotFoundException(A_STOCK));

    this.transactionFactory.create(AN_ACCOUNT_NUMBER, this.BuyTransactionRequest);
  }

  @Test(expected = InvalidDateException.class)
  public void whenTransactionDateIsInvalid_thenInvalidDateExceptionIsThrown() {
    when(this.stocksRepository.findPriceBy(A_STOCK, A_DATE)).thenThrow(new InvalidDateException());

    this.transactionFactory.create(AN_ACCOUNT_NUMBER, this.BuyTransactionRequest);
  }

  @Test(expected = MarketClosedException.class)
  public void whenMarketIsClosed_thenMarketClosedExceptionIsThrownForTheMarket() {
    when(this.marketOpenedValidator.validateAtDate(A_STOCK.getMarket(), A_DATE)).thenReturn(false);

    this.transactionFactory.create(AN_ACCOUNT_NUMBER, this.BuyTransactionRequest);
  }

  @Test
  public void whenMarketIsClosed_thenExceptionHasValidMessage() {
    when(this.marketOpenedValidator.validateAtDate(A_STOCK.getMarket(), A_DATE)).thenReturn(false);

    MarketClosedException expectedError = new MarketClosedException(ANOTHER_MARKET, UUID.randomUUID());
    
    try {
      this.transactionFactory.create(AN_ACCOUNT_NUMBER, this.BuyTransactionRequest);
    } catch (final MarketClosedException e) {
      expectedError = e;
    }

    assertEquals(String.format("market '%s' is closed", MARKET), expectedError.getDescription());
  }
}
