package ca.ulaval.glo4002.trading.accounts.controller.assembler;

import ca.ulaval.glo4002.trading.accounts.domain.AccountNumber;
import org.springframework.stereotype.Component;

@Component
public class AccountNumberAssembler {

  public AccountNumber toEntity(final String accountNumber) {
    return new AccountNumber(accountNumber);
  }
}
