package ca.ulaval.glo4002.trading.report.controller.dto.errors;

public class UnsupportedReportTypeError extends ReportAPIErrorDTO {
  public UnsupportedReportTypeError(final String reportType) {
    super("REPORT_TYPE_UNSUPPORTED", String.format("report '%s' is not supported", reportType));
  }
}
