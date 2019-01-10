package ca.ulaval.glo4002.trading.accounts.controller.exceptions;

import ca.ulaval.glo4002.trading.accounts.domain.AccountNumber;
import ca.ulaval.glo4002.trading.common.exceptions.TradingException;

public class AccountNotFoundException extends TradingException {
  private static final long serialVersionUID = -238687347502089206L;

  public AccountNotFoundException(AccountNumber accountNumber) {
    super("ACCOUNT_NOT_FOUND", String.format("account with number %s not found", accountNumber.getValue()));
  }
}
