package ca.ulaval.glo4002.trading.transactions.controller.dto.errors;

import ca.ulaval.glo4002.trading.common.controller.dto.errors.APIErrorDTO;

public class UnsupportedTransactionTypeError extends APIErrorDTO {
  public UnsupportedTransactionTypeError(final String type) {
    super("UNSUPPORTED_TRANSACTION_TYPE", String.format("transaction '%s' is not supported", type));
  }
}
