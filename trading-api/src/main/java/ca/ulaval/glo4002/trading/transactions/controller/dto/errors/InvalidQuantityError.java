package ca.ulaval.glo4002.trading.transactions.controller.dto.errors;

import ca.ulaval.glo4002.trading.common.controller.dto.errors.APIErrorDTO;

public class InvalidQuantityError extends APIErrorDTO {
  public InvalidQuantityError() {
    super("INVALID_QUANTITY", "quantity is invalid");
  }
}
