package ca.ulaval.glo4002.trading.common.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public abstract class TradingException extends RuntimeException {
  private static final long serialVersionUID = 8100277043790887735L;

  private final String error;
  private final String description;
}
