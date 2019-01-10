package ca.ulaval.glo4002.trading.accounts.controller.exceptions;

import ca.ulaval.glo4002.trading.accounts.domain.InvestorId;
import ca.ulaval.glo4002.trading.common.exceptions.TradingException;

public class AccountAlreadyOpenException extends TradingException {
  private static final long serialVersionUID = -3552058509470929533L;

  public AccountAlreadyOpenException(InvestorId investorId) {
    super("ACCOUNT_ALREADY_OPEN", String.format("account already open for investor %d", investorId.getValue()));
  }
}
