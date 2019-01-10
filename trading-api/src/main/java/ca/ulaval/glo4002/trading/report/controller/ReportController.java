package ca.ulaval.glo4002.trading.report.controller;


import ca.ulaval.glo4002.trading.common.controller.ControllerBase;
import ca.ulaval.glo4002.trading.report.controller.dto.errors.MissingReportTypeError;
import ca.ulaval.glo4002.trading.report.controller.dto.errors.UnsupportedReportTypeError;
import ca.ulaval.glo4002.trading.report.controller.subcontrollers.ReportSubcontrollerSelector;
import ca.ulaval.glo4002.trading.report.domain.reports.ReportSubcontroller;
import ca.ulaval.glo4002.trading.report.domain.reports.ReportType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.Optional;

@RepositoryRestController
@RequestMapping("/accounts/{accountNumber}/reports")
public class ReportController extends ControllerBase {

  private final ReportSubcontrollerSelector reportSubcontrollersSelector;

  @Autowired
  public ReportController(final ReportSubcontrollerSelector reportSubcontrollerSelector) {
    this.reportSubcontrollersSelector = reportSubcontrollerSelector;
  }

  @GetMapping
  public ResponseEntity<?> getReport(
          @PathVariable("accountNumber") final String accountNumber,
          @RequestParam(value = "type", required = false) final String type,
          @RequestParam(value = "date", required = false) final String dateString) {

    if (StringUtils.isEmpty(type)) {
      return this.buildValidationErrorResponse(Collections.singletonList(new MissingReportTypeError()));
    }

    if (!ReportType.contains(type)) {
      return this.buildValidationErrorResponse(Collections.singletonList(new UnsupportedReportTypeError(type)));
    }

    final ReportSubcontroller reportSubcontroller = this.reportSubcontrollersSelector.getSubcontroller(ReportType.valueOf(type));


    return reportSubcontroller.getTransactionReport(accountNumber, dateString);
  }
}
