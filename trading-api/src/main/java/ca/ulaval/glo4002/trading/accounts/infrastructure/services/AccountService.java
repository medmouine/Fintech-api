package ca.ulaval.glo4002.trading.accounts.infrastructure.services;

import ca.ulaval.glo4002.trading.accounts.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

  private final AccountRepository accountRepository;
  private final AccountFactory accountFactory;

  @Autowired
  public AccountService(final AccountRepository accountRepository, final AccountFactory accountFactory) {
    this.accountRepository = accountRepository;
    this.accountFactory = accountFactory;
  }

  public Account openAccount(final AccountCreationRequest accountCreationRequest) {
    this.accountRepository.assertAccountDoesntExist(accountCreationRequest.getInvestorId());

    final Account account = this.accountFactory.create(accountCreationRequest);
    this.save(account);

    return account;
  }

  public Account findByAccountNumber(final AccountNumber accountNumber) {
    return this.accountRepository.findByAccountNumber(accountNumber);
  }

  public void save(final Account account) {
    this.accountRepository.save(account);
  }
}