package ca.ulaval.glo4002.trading.transactions.infrastructure.repository;

import ca.ulaval.glo4002.trading.accounts.domain.AccountNumber;
import ca.ulaval.glo4002.trading.accounts.domain.exceptions.TransactionNotFoundException;
import ca.ulaval.glo4002.trading.transactions.domain.Transaction;
import ca.ulaval.glo4002.trading.transactions.domain.TransactionRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public class SQLTransactionRepository implements TransactionRepository {
  private final HibernateTransactionRepository hibernateTransactionRepository;
  private final TransactionHibernateEntityAssembler hibernateEntityAssembler;

  public SQLTransactionRepository(final HibernateTransactionRepository hibernateTransactionRepository, final TransactionHibernateEntityAssembler transactionHibernateEntityAssembler) {
    this.hibernateTransactionRepository = hibernateTransactionRepository;
    this.hibernateEntityAssembler = transactionHibernateEntityAssembler;
  }

  @Override
  public void save(final Transaction transaction) {
    this.hibernateTransactionRepository.save(this.hibernateEntityAssembler.from(transaction));
  }

  @Override
  public Transaction findByTransactionNumber(final UUID transactionNumber) {
    final TransactionHibernateEntity entity = this.hibernateTransactionRepository.findByTransactionNumber(transactionNumber);
    if (entity != null) {
      return this.hibernateEntityAssembler.from(entity);
    } else {
      throw new TransactionNotFoundException(transactionNumber);
    }
  }

  @Override
  public List<Transaction> findAllTransactionForAccountInTimeRange(final AccountNumber accountNumber, final Instant startDate, final Instant endDate) {
    final List<TransactionHibernateEntity> transactionsHibernate = this.hibernateTransactionRepository.findByAccountNumberAndDateBetween(accountNumber.getValue(), startDate, endDate);
    final List<Transaction> transactions = this.hibernateEntityAssembler.from(transactionsHibernate);
    return transactions;
  }

  @Override
  public Long getStockQuantityLeftForTransaction(final UUID transactionNumber) {
    long quantityLeft = this.findByTransactionNumber(transactionNumber).getQuantity();

    final List<TransactionHibernateEntity> sellTransactionEntities = this.hibernateTransactionRepository.findByAssociatedBuyTransactionNumber(transactionNumber);
    for (final TransactionHibernateEntity sellTransactionEntity : sellTransactionEntities) {
      quantityLeft -= sellTransactionEntity.getQuantity();
    }

    return quantityLeft;
  }
}
