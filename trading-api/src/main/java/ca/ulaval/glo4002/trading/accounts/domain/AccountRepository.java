package ca.ulaval.glo4002.trading.accounts.domain;

import ca.ulaval.glo4002.trading.accounts.controller.exceptions.AccountAlreadyOpenException;
import ca.ulaval.glo4002.trading.accounts.controller.exceptions.AccountNotFoundException;

public interface AccountRepository {

  Account findByAccountNumber(final AccountNumber accountNumber) throws AccountNotFoundException;

  void assertAccountDoesntExist(final InvestorId investorId) throws AccountAlreadyOpenException;

  int getNumberOfAccountOpened();

  void save(Account account);

  void exists(final AccountNumber accountNumber) throws AccountNotFoundException;
}
