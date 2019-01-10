package ca.ulaval.glo4002.trading.accounts.infrastructure.repository.hibernate;

import org.springframework.data.repository.Repository;
import org.springframework.data.rest.core.annotation.RestResource;

public interface HibernateAccountRepository extends Repository<AccountHibernateEntity, String> {
  @RestResource(exported = false)
  AccountHibernateEntity findAccountHibernateEntityByInvestorId(Long investorId);

  @RestResource(exported = false)
  AccountHibernateEntity findAccountHibernateEntityByAccountNumber(String accountNumber);

  @RestResource(exported = false)
  boolean existsAccountHibernateEntityByAccountNumber(String accountNumber);

  @RestResource(exported = false)
  long count();

  @RestResource(exported = false)
  void save(AccountHibernateEntity account);
}
