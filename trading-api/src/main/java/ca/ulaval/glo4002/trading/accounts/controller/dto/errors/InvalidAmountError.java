package ca.ulaval.glo4002.trading.accounts.controller.dto.errors;

import ca.ulaval.glo4002.trading.common.controller.dto.errors.APIErrorDTO;

public class InvalidAmountError extends APIErrorDTO {
  public InvalidAmountError() {
    super("INVALID_AMOUNT", "credit amount cannot be lower than or equal to zero");
  }
}
