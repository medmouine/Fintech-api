package ca.ulaval.glo4002.trading.transactions.infrastructure.repository;

import ca.ulaval.glo4002.trading.accounts.domain.AccountNumber;
import ca.ulaval.glo4002.trading.accounts.domain.exceptions.TransactionNotFoundException;
import ca.ulaval.glo4002.trading.transactions.domain.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SQLTransactionRepositoryTest {

  private static final UUID A_TRANSACTION_NUMBER = new UUID(12L, 1232);
  private static final AccountNumber AN_ACCOUNT_NUMBER = new AccountNumber("VR-123");
  private static final Instant A_START_DATE = Instant.parse("2018-09-09T00:00:00.000Z");
  private static final Instant A_END_DATE = Instant.parse("2018-09-10T00:00:00.000Z");

  @Mock
  private Transaction transaction;
  private final List<Transaction> TRANSACTION_LIST = Collections.singletonList(this.transaction);
  @Mock
  private TransactionHibernateEntity transactionHibernateEntity;
  private final List<TransactionHibernateEntity> TRANSACTION_HIBERNATE_ENTITY_LIST = Collections.singletonList(this.transactionHibernateEntity);
  @Mock
  private TransactionHibernateEntity sellTransactionHibernateEntity;
  @Mock
  private HibernateTransactionRepository hibernateTransactionRepository;
  @Mock
  private TransactionHibernateEntityAssembler transactionHibernateEntityAssembler;
  @InjectMocks
  private SQLTransactionRepository transactionRepository;

  @Before
  public void setUp() {
    when(this.transactionHibernateEntityAssembler.from(this.transactionHibernateEntity)).thenReturn(this.transaction);
  }

  @Test
  public void givenAnExistingTransaction_whenFindingByTransactionNumber_thenTransactionIsReturned() {
    when(this.hibernateTransactionRepository.findByTransactionNumber(A_TRANSACTION_NUMBER)).thenReturn(this.transactionHibernateEntity);

    assertEquals(this.transaction, this.transactionRepository.findByTransactionNumber(A_TRANSACTION_NUMBER));
  }

  @Test(expected = TransactionNotFoundException.class)
  public void givenANonExistingTransaction_whenFindingByTransactionNumber_thenTransactionNotFoundIsThrown() {
    this.transactionRepository.findByTransactionNumber(A_TRANSACTION_NUMBER);
  }

  @Test
  public void givenAnExistingSellTransactionAssociatedToABuyTransaction_whenGettingStockQuantityLeft_thenQuantityShouldBeValid() {
    final long A_BUY_TRANSACTION_QUANTITY = 2;
    final long A_SELL_TRANSACTION_QUANTITY = 1;

    when(this.hibernateTransactionRepository.findByTransactionNumber(A_TRANSACTION_NUMBER)).thenReturn(this.transactionHibernateEntity);
    when(this.hibernateTransactionRepository.findByAssociatedBuyTransactionNumber(A_TRANSACTION_NUMBER)).thenReturn(Arrays.asList(this.sellTransactionHibernateEntity));
    when(this.transaction.getQuantity()).thenReturn(A_BUY_TRANSACTION_QUANTITY);
    when(this.sellTransactionHibernateEntity.getQuantity()).thenReturn(A_SELL_TRANSACTION_QUANTITY);

    final long quantityLeft = this.transactionRepository.getStockQuantityLeftForTransaction(A_TRANSACTION_NUMBER);

    assertEquals((A_BUY_TRANSACTION_QUANTITY - A_SELL_TRANSACTION_QUANTITY), quantityLeft);
  }

  @Test
  public void whenSavingATransaction_thenTheTransactionIsSavedIntoTheHibernateEntity() {
    when(this.transactionHibernateEntityAssembler.from(this.transaction)).thenReturn(this.transactionHibernateEntity);

    this.transactionRepository.save(this.transaction);

    verify(this.hibernateTransactionRepository, times(1)).save(this.transactionHibernateEntity);
  }

  @Test
  public void givenAnAccountNumberWithAStartAndEndDate_whenFindingAccountTransactionsForAccountWithSomeTransactions_thenTheAccountTransactionsBetweenTheDatesCorrectlyReturned() {
    when(this.hibernateTransactionRepository.findByAccountNumberAndDateBetween(AN_ACCOUNT_NUMBER.getValue(), A_START_DATE, A_END_DATE)).thenReturn(this.TRANSACTION_HIBERNATE_ENTITY_LIST);
    when(this.transactionHibernateEntityAssembler.from(this.TRANSACTION_HIBERNATE_ENTITY_LIST)).thenReturn(this.TRANSACTION_LIST);

    final List<Transaction> transactions = this.transactionRepository.findAllTransactionForAccountInTimeRange(AN_ACCOUNT_NUMBER, A_START_DATE, A_END_DATE);

    assertEquals(this.TRANSACTION_LIST, transactions);
  }
}
