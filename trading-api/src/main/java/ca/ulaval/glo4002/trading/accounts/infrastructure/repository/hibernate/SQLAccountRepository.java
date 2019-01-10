package ca.ulaval.glo4002.trading.accounts.infrastructure.repository.hibernate;

import ca.ulaval.glo4002.trading.accounts.controller.exceptions.AccountAlreadyOpenException;
import ca.ulaval.glo4002.trading.accounts.controller.exceptions.AccountNotFoundException;
import ca.ulaval.glo4002.trading.accounts.domain.Account;
import ca.ulaval.glo4002.trading.accounts.domain.AccountNumber;
import ca.ulaval.glo4002.trading.accounts.domain.AccountRepository;
import ca.ulaval.glo4002.trading.accounts.domain.InvestorId;
import org.springframework.stereotype.Repository;

@Repository
public class SQLAccountRepository implements AccountRepository {
  private final HibernateAccountRepository hibernateAccountRepository;

  private final AccountHibernateEntityAssembler accountHibernateEntityAssembler;

  public SQLAccountRepository(final HibernateAccountRepository hibernateAccountRepository,
                              final AccountHibernateEntityAssembler accountHibernateEntityAssembler) {
    this.hibernateAccountRepository = hibernateAccountRepository;
    this.accountHibernateEntityAssembler = accountHibernateEntityAssembler;
  }

  @Override
  public Account findByAccountNumber(final AccountNumber accountNumber) throws AccountNotFoundException {
    final Account account = this.accountHibernateEntityAssembler.from(this.hibernateAccountRepository.findAccountHibernateEntityByAccountNumber(accountNumber.getValue()));

    if (account != null) {
      return account;
    } else {
      throw new AccountNotFoundException(accountNumber);
    }
  }

  @Override
  public void assertAccountDoesntExist(final InvestorId investorId) throws AccountAlreadyOpenException {
    final Account account = this.accountHibernateEntityAssembler.from(this.hibernateAccountRepository.findAccountHibernateEntityByInvestorId(investorId.getValue()));
    if (account != null) {
      throw new AccountAlreadyOpenException(investorId);
    }
  }

  @Override
  public int getNumberOfAccountOpened() {
    return (int) this.hibernateAccountRepository.count();
  }

  @Override
  public void save(final Account account) {
    this.hibernateAccountRepository.save(this.accountHibernateEntityAssembler.from(account));
  }

  @Override
  public void exists(final AccountNumber accountNumber) {
    if (!this.hibernateAccountRepository.existsAccountHibernateEntityByAccountNumber(accountNumber.getValue())) {
      throw new AccountNotFoundException(accountNumber);
    }
  }
}
