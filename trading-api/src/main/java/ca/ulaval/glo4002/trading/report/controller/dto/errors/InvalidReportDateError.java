package ca.ulaval.glo4002.trading.report.controller.dto.errors;

public class InvalidReportDateError extends ReportAPIErrorDTO {
  public InvalidReportDateError(final String date) {
    super("INVALID_DATE", String.format("date '%s' is invalid", date));
  }
}
