package ca.ulaval.glo4002.trading.accounts.domain;

import ca.ulaval.glo4002.trading.accounts.domain.investorProfile.InvestorProfile;
import ca.ulaval.glo4002.trading.transactions.domain.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Account {

  private AccountNumber accountNumber;
  private InvestorId investorId;
  private InvestorProfile investorProfile;
  private String investorName;
  private String email;
  private StockWallet wallet;

  public Account(final InvestorProfile investorProfile,
                 final InvestorId investorId,
                 final String investorName,
                 final String email,
                 final StockWallet wallet,
                 final AccountNumber accountNumber) {
    this.investorProfile = investorProfile;
    this.investorId = investorId;
    this.investorName = investorName;
    this.email = email;
    this.wallet = wallet;
    this.accountNumber = accountNumber;
  }

  public Account(final StockWallet wallet) {
    this.wallet = wallet;
  }

  public void performTransaction(final Transaction transaction) {
    transaction.execute(this.wallet);
  }

}
