package ca.ulaval.glo4002.trading.transactions.infrastructure.repository;

import org.springframework.data.repository.Repository;
import org.springframework.data.rest.core.annotation.RestResource;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface HibernateTransactionRepository extends Repository<TransactionHibernateEntity, UUID> {
  @RestResource(exported = false)
  TransactionHibernateEntity findByTransactionNumber(UUID transactionNumber);

  @RestResource(exported = false)
  List<TransactionHibernateEntity> findByAssociatedBuyTransactionNumber(UUID transactionNumber);

  @RestResource(exported = false)
  List<TransactionHibernateEntity> findByAccountNumberAndDateBetween(String accountNumber, Instant startDate, Instant endDate);

  @RestResource(exported = false)
  void save(TransactionHibernateEntity transaction);
}
