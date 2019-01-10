package ca.ulaval.glo4002.trading.common.controller.dto.errors;

import lombok.Getter;

import java.util.UUID;

@Getter
public class TransactionAPIErrorDTO extends APIErrorDTO {
  private final UUID transactionNumber;

  public TransactionAPIErrorDTO(final String error, final String description, final UUID transactionNumber) {
    super(error, description);
    this.transactionNumber = transactionNumber;
  }
}
