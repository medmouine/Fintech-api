package ca.ulaval.glo4002.trading.accounts.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreditDTO {
  private final float amount;
  private final String currency;
}
