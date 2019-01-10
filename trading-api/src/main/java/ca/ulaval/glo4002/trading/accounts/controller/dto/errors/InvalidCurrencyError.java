package ca.ulaval.glo4002.trading.accounts.controller.dto.errors;

import ca.ulaval.glo4002.trading.common.controller.dto.errors.APIErrorDTO;

public class InvalidCurrencyError extends APIErrorDTO {
  public InvalidCurrencyError() {
    super("INVALID_CURRENCY", "Credits currency must be valid and part of the known markets");
  }
}
