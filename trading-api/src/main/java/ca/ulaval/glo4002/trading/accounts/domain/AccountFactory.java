package ca.ulaval.glo4002.trading.accounts.domain;

import ca.ulaval.glo4002.trading.accounts.domain.investorProfile.InvestorProfile;
import ca.ulaval.glo4002.trading.accounts.domain.investorProfile.InvestorType;
import ca.ulaval.glo4002.trading.accounts.infrastructure.services.AccountCreationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class AccountFactory {

  private final StockWalletFactory stockWalletFactory;
  private final AccountRepository accountRepository;

  @Autowired
  public AccountFactory(final StockWalletFactory stockWalletFactory, final AccountRepository accountRepository) {
    this.stockWalletFactory = stockWalletFactory;
    this.accountRepository = accountRepository;
  }

  public Account create(final AccountCreationRequest accountCreationRequest) {
    final Account account = new Account(new InvestorProfile(InvestorType.CONSERVATIVE, Collections.EMPTY_LIST),
            accountCreationRequest.getInvestorId(),
            accountCreationRequest.getInvestorName(),
            accountCreationRequest.getEmail(),
            this.stockWalletFactory.create(accountCreationRequest.getCredits()),
            new AccountNumber(String.format("%s-%d", this.getInitials(accountCreationRequest.getInvestorName()), this.accountRepository.getNumberOfAccountOpened() + 1)));

    return account;
  }

  private String getInitials(final String investorName) {
    final StringBuilder initials = new StringBuilder();

    for (final String name : investorName.split(" ")) {
      initials.append(name.substring(0, 1).toUpperCase());
    }

    return initials.toString();
  }
}
