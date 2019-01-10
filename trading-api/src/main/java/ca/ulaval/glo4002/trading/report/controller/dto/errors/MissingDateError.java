package ca.ulaval.glo4002.trading.report.controller.dto.errors;

public class MissingDateError extends ReportAPIErrorDTO {
  public MissingDateError() {
    super("MISSING_DATE", "date is missing");
  }
}
