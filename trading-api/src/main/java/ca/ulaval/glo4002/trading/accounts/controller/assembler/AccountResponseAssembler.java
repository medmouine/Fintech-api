package ca.ulaval.glo4002.trading.accounts.controller.assembler;

import ca.ulaval.glo4002.trading.accounts.controller.response.AccountResponse;
import ca.ulaval.glo4002.trading.accounts.domain.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountResponseAssembler {

  private final InvestorProfileAssembler investorProfileAssembler;
  private final CreditAssembler creditAssembler;

  @Autowired
  public AccountResponseAssembler(final InvestorProfileAssembler investorProfileAssembler,
                                  final CreditAssembler creditAssembler) {
    this.investorProfileAssembler = investorProfileAssembler;
    this.creditAssembler = creditAssembler;
  }

  public AccountResponse toAccountResponse(final Account account) {

    return new AccountResponse(account.getInvestorId().getValue(),
            this.creditAssembler.ToDTOList(account.getWallet().getCredits()),
            account.getAccountNumber().getValue(),
            this.investorProfileAssembler.toDTO(account.getInvestorProfile()),
            account.getWallet().getCreditsSumInCAD().getNumberStripped().floatValue());
  }
}
