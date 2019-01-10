package ca.ulaval.glo4002.trading.accounts.infrastructure.repository.hibernate;

import ca.ulaval.glo4002.trading.accounts.domain.Account;
import ca.ulaval.glo4002.trading.accounts.domain.AccountNumber;
import ca.ulaval.glo4002.trading.accounts.domain.InvestorId;
import ca.ulaval.glo4002.trading.accounts.domain.StockWalletFactory;
import ca.ulaval.glo4002.trading.accounts.domain.investorProfile.InvestorProfile;
import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.money.Monetary;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AccountHibernateEntityAssembler {

  private final StockWalletFactory stockWalletFactory;

  @Autowired
  public AccountHibernateEntityAssembler(final StockWalletFactory stockWalletFactory) {
    this.stockWalletFactory = stockWalletFactory;
  }

  public AccountHibernateEntity from(final Account account) {
    if (account == null) {
      return null;
    }

    final AccountHibernateEntity entity = new AccountHibernateEntity();
    entity.accountNumber = account.getAccountNumber().getValue();
    entity.investorId = account.getInvestorId().getValue();
    entity.investorType = account.getInvestorProfile().getInvestorType();
    entity.focusAreas = account.getInvestorProfile().getFocusAreas().stream().map(FocusAreaHibernateEntity::from).collect(Collectors.toList());
    entity.investorName = account.getInvestorName();
    entity.email = account.getEmail();
    entity.credits = this.buildCreditsHibernateEntityList(account.getWallet().getCredits(), entity);
    return entity;
  }

  private List<CreditsHibernateEntity> buildCreditsHibernateEntityList(final List<Money> credits, final AccountHibernateEntity accountHibernateEntity) {
    return credits.stream()
            .map(credit -> CreditsHibernateEntity.from(credit))
            .collect(Collectors.toList());
  }

  public Account from(final AccountHibernateEntity entity) {
    if (entity == null) {
      return null;
    }

    return new Account(
            new InvestorProfile(
                    entity.investorType,
                    entity.focusAreas.stream().map(FocusAreaHibernateEntity::from).collect(Collectors.toList())),
            new InvestorId(entity.investorId),
            entity.investorName,
            entity.email,
            this.stockWalletFactory.create(this.buildMoneyList(entity.credits)),
            new AccountNumber(entity.accountNumber)
    );
  }

  private List<Money> buildMoneyList(final List<CreditsHibernateEntity> credits) {
    return credits.stream()
            .map(credit -> Money.of(credit.getAmount(), Monetary.getCurrency(credit.getCurrency())))
            .collect(Collectors.toList());
  }
}
