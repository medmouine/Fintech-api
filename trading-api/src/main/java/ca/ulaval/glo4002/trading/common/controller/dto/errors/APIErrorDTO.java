package ca.ulaval.glo4002.trading.common.controller.dto.errors;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@AllArgsConstructor
@Getter
public class APIErrorDTO {
  private String error;
  private String description;
}
