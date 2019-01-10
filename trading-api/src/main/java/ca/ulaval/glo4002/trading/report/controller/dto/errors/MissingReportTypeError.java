package ca.ulaval.glo4002.trading.report.controller.dto.errors;

import ca.ulaval.glo4002.trading.common.controller.dto.errors.APIErrorDTO;

public class MissingReportTypeError extends APIErrorDTO {

  public MissingReportTypeError() {
    super("MISSING_REPORT_TYPE", "report type is missing");
  }
}
