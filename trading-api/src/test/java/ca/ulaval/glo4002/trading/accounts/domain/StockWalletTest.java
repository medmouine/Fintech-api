package ca.ulaval.glo4002.trading.accounts.domain;

import ca.ulaval.glo4002.trading.common.infrastructure.currencyConverter.CurrencyConverterFactory;
import ca.ulaval.glo4002.trading.markets.domain.Market;
import ca.ulaval.glo4002.trading.stocks.domain.Stock;
import ca.ulaval.glo4002.trading.transactions.controller.exceptions.InvalidTransactionNumberException;
import ca.ulaval.glo4002.trading.transactions.controller.exceptions.NotEnoughCreditsException;
import ca.ulaval.glo4002.trading.transactions.controller.exceptions.NotEnoughCreditsForFeesException;
import ca.ulaval.glo4002.trading.transactions.controller.exceptions.StockParametersDontMatchException;
import ca.ulaval.glo4002.trading.transactions.domain.Transaction;
import ca.ulaval.glo4002.trading.transactions.domain.TransactionRepository;
import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StockWalletTest {

  private static final long VALID_QUANTITY = 15L;

  private static final Market MARKET_1 = Market.NASDAQ;
  private static final Market MARKET_2 = Market.XSWX;
  private static final CurrencyUnit USD_CURRENCY = Monetary.getCurrency("USD");
  private static final Money INITIAL_WALLET_MONEY = Money.of(600, USD_CURRENCY);

  private static final Money A_BUY_PRICE = Money.of(500, USD_CURRENCY);
  private static final Money A_BUY_PRICE_TOO_HIGH = Money.of(1000, USD_CURRENCY);

  private static final Money A_SELL_PRICE = Money.of(400, USD_CURRENCY);

  private static final Money A_FEES_AMOUNT = Money.of(1, USD_CURRENCY);
  private static final Money A_FEES_AMOUNT_TOO_HIGH = Money.of(1000, USD_CURRENCY);

  private static final Stock A_STOCK = new Stock(MARKET_1, "symbol");
  private static final Stock A_NON_EQUAL_STOCK = new Stock(MARKET_2, "symbol2");
  private static final UUID TRANSACTION_NUMBER = UUID.randomUUID();

  @Mock
  private TransactionRepository transactionRepository;
  @Mock
  private Transaction transaction;
  @Mock
  private CurrencyConverterFactory currencyConverterFactory;

  private StockWallet stockWallet;

  @Before
  public void setUp() {
    final List<Money> credits = Arrays.asList(INITIAL_WALLET_MONEY);
    this.stockWallet = new StockWallet(credits, this.transactionRepository, this.currencyConverterFactory);
    when(this.transaction.isWithStock(A_STOCK)).thenReturn(true);
  }

  @Test(expected = InvalidTransactionNumberException.class)
  public void whenSellingStockFromInvalidBuyTransaction_thenInvalidTransactionNumberExceptionThrown() {
    when(this.transactionRepository.findByTransactionNumber(TRANSACTION_NUMBER)).thenThrow(new InvalidTransactionNumberException(TRANSACTION_NUMBER));
    when(this.transactionRepository.getStockQuantityLeftForTransaction(TRANSACTION_NUMBER)).thenThrow(new InvalidTransactionNumberException(TRANSACTION_NUMBER));

    this.stockWallet.sellStocks(TRANSACTION_NUMBER, VALID_QUANTITY, A_SELL_PRICE, A_STOCK, A_FEES_AMOUNT);
  }

  @Test(expected = NotEnoughCreditsForFeesException.class)
  public void whenSellingStocksAndResultingAmountIsSmallerThanZero_thenNotEnoughCreditsForFeesExceptionThrown() {
    when(this.transactionRepository.getStockQuantityLeftForTransaction(TRANSACTION_NUMBER)).thenReturn(VALID_QUANTITY);
    when(this.transactionRepository.findByTransactionNumber(TRANSACTION_NUMBER)).thenReturn(this.transaction);

    this.stockWallet.sellStocks(TRANSACTION_NUMBER, VALID_QUANTITY, A_SELL_PRICE, A_STOCK, A_FEES_AMOUNT_TOO_HIGH);
  }

  @Test(expected = StockParametersDontMatchException.class)
  public void givenAWrongStockParameter_whenSellingStocks_thenStockParametersDontMatchExceptionIsThrown() {
    when(this.transactionRepository.getStockQuantityLeftForTransaction(TRANSACTION_NUMBER)).thenReturn(VALID_QUANTITY);
    when(this.transactionRepository.findByTransactionNumber(TRANSACTION_NUMBER)).thenReturn(this.transaction);

    this.stockWallet.sellStocks(TRANSACTION_NUMBER, VALID_QUANTITY, A_SELL_PRICE, A_NON_EQUAL_STOCK, A_FEES_AMOUNT);
  }

  @Test
  public void givenEnoughStocks_whenSellingStocks_thenCreditsAreAddedToTotalCredits() {
    when(this.transactionRepository.getStockQuantityLeftForTransaction(TRANSACTION_NUMBER)).thenReturn(VALID_QUANTITY);
    when(this.transactionRepository.findByTransactionNumber(TRANSACTION_NUMBER)).thenReturn(this.transaction);

    this.stockWallet.sellStocks(TRANSACTION_NUMBER, VALID_QUANTITY, A_SELL_PRICE, A_STOCK, A_FEES_AMOUNT);

    assertEquals(INITIAL_WALLET_MONEY.add(A_SELL_PRICE).subtract(A_FEES_AMOUNT), this.stockWallet.getCreditForSpecificCurrency(USD_CURRENCY));
  }

  @Test
  public void whenBuyingStocks_thenCreditsAreSubtractedFromTheTotalCredits() {
    this.stockWallet.buyStocks(TRANSACTION_NUMBER, A_BUY_PRICE);

    assertEquals(INITIAL_WALLET_MONEY.subtract(A_BUY_PRICE), this.stockWallet.getCreditForSpecificCurrency(USD_CURRENCY));
  }

  @Test(expected = NotEnoughCreditsException.class)
  public void givenNotEnoughCredits_whenBuyingStocks_thenNotEnoughCreditsExceptionIsThrown() {
    this.stockWallet.buyStocks(TRANSACTION_NUMBER, A_BUY_PRICE_TOO_HIGH);
  }
}
