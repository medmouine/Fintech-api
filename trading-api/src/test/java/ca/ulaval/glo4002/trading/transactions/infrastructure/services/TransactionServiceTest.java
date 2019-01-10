package ca.ulaval.glo4002.trading.transactions.infrastructure.services;

import ca.ulaval.glo4002.trading.accounts.domain.Account;
import ca.ulaval.glo4002.trading.accounts.domain.AccountNumber;
import ca.ulaval.glo4002.trading.accounts.infrastructure.services.AccountService;
import ca.ulaval.glo4002.trading.markets.domain.Market;
import ca.ulaval.glo4002.trading.stocks.domain.Stock;
import ca.ulaval.glo4002.trading.stocks.domain.StocksRepository;
import ca.ulaval.glo4002.trading.transactions.domain.Transaction;
import ca.ulaval.glo4002.trading.transactions.domain.TransactionFactory;
import ca.ulaval.glo4002.trading.transactions.domain.TransactionRepository;
import ca.ulaval.glo4002.trading.transactions.domain.TransactionRequest;
import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.Instant;
import java.util.UUID;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceTest {

  private static final String USD = "USD";
  private static final AccountNumber ACCOUNT_NUMBER = new AccountNumber("VR-123");
  private static final Instant TRANSACTION_DATE = Instant.now();
  private static final UUID TRANSACTION_UUID = new UUID(12L, 1232);
  private static final float AMOUNT = 15.5f;
  private static final Money STOCK_UNIT_PRICE = Money.of(AMOUNT, USD);
  private static final Stock STOCK = new Stock(Market.NASDAQ, "symbol");

  @Mock
  private TransactionRepository transactionRepository;
  @Mock
  private AccountService accountService;
  @Mock
  private StocksRepository stocksRepository;
  @Mock
  private Account account;
  @Mock
  private TransactionRequest transactionRequest;
  @Mock
  private Transaction transaction;
  @Mock
  private TransactionFactory transactionFactory;
  @InjectMocks
  private TransactionService transactionService;

  @Before
  public void setUp() {
    when(this.accountService.findByAccountNumber(ACCOUNT_NUMBER)).thenReturn(this.account);
    when(this.transactionRequest.getStock()).thenReturn(STOCK);
    when(this.transactionRequest.getDate()).thenReturn(TRANSACTION_DATE);
    when(this.transactionRequest.getTransactionNumber()).thenReturn(TRANSACTION_UUID);
    when(this.stocksRepository.findPriceBy(STOCK, TRANSACTION_DATE)).thenReturn(STOCK_UNIT_PRICE);
    when(this.transaction.getTransactionNumber()).thenReturn(TRANSACTION_UUID);
    when(this.transactionFactory.create(ACCOUNT_NUMBER, this.transactionRequest)).thenReturn(this.transaction);
  }

  @Test
  public void whenAccountExists_thenReturnTheTransactionId() {
    assertEquals(this.transaction.getTransactionNumber(), this.transactionService.makeTransaction(ACCOUNT_NUMBER, this.transactionRequest));
  }

  @Test
  public void whenMakingATransaction_thenTheTransactionIsSavedInTheRepository() {
    this.transactionService.makeTransaction(ACCOUNT_NUMBER, this.transactionRequest);

    verify(this.transactionRepository, times(1)).save(this.transaction);
  }

  @Test
  public void whenMakingATransaction_thenTheAccountIsSavedInTheRepository() {
    this.transactionService.makeTransaction(ACCOUNT_NUMBER, this.transactionRequest);

    verify(this.accountService, times(1)).save(this.account);
  }

  @Test
  public void whenMakingTheTransaction_thenAccountPerformsTheTransaction() {
    this.transactionService.makeTransaction(ACCOUNT_NUMBER, this.transactionRequest);

    verify(this.account, times(1)).performTransaction(this.transaction);
  }

  @Test
  public void givenValidTransactionNumber_whenTransactionExists_thenTheTransactionIsReturned() {
    when(this.transactionRepository.findByTransactionNumber(TRANSACTION_UUID)).thenReturn(this.transaction);

    assertEquals(this.transaction, this.transactionService.getTransaction(TRANSACTION_UUID));
  }
}

