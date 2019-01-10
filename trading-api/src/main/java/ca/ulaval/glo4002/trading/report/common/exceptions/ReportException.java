package ca.ulaval.glo4002.trading.report.common.exceptions;

import ca.ulaval.glo4002.trading.common.exceptions.TradingException;
import lombok.Getter;

@Getter
public abstract class ReportException extends TradingException {

  private static final long serialVersionUID = -8483249322582203727L;

  public ReportException(final String error, final String description) {
    super(error, description);
  }
}

