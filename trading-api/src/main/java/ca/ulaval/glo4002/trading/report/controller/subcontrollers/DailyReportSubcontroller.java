package ca.ulaval.glo4002.trading.report.controller.subcontrollers;

import ca.ulaval.glo4002.trading.common.controller.ControllerBase;
import ca.ulaval.glo4002.trading.common.controller.dto.Validator;
import ca.ulaval.glo4002.trading.common.controller.dto.errors.APIErrorDTO;
import ca.ulaval.glo4002.trading.report.controller.assembler.DailyReportAssembler;
import ca.ulaval.glo4002.trading.report.controller.dto.DailyTransactionReportRequestDTO;
import ca.ulaval.glo4002.trading.report.domain.reports.DailyTransactionReport;
import ca.ulaval.glo4002.trading.report.domain.reports.ReportSubcontroller;
import ca.ulaval.glo4002.trading.report.domain.reports.ReportType;
import ca.ulaval.glo4002.trading.report.infrastructure.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DailyReportSubcontroller extends ControllerBase implements ReportSubcontroller {

  private final Validator<DailyTransactionReportRequestDTO> transactionReportRequestDTOValidator;
  private final ReportService reportService;
  private final DailyReportAssembler reportAssembler;

  @Autowired
  public DailyReportSubcontroller(final Validator<DailyTransactionReportRequestDTO> transactionReportRequestDTOValidator,
                                  final ReportService reportService,
                                  final DailyReportAssembler reportAssembler) {
    this.transactionReportRequestDTOValidator = transactionReportRequestDTOValidator;
    this.reportService = reportService;
    this.reportAssembler = reportAssembler;
  }

  @Override
  public ReportType getReportType() {
    return ReportType.DAILY;
  }

  @Override
  public ResponseEntity<?> getTransactionReport(final String accountNumber, final String dateString) {
    final DailyTransactionReportRequestDTO reportRequestDTO = new DailyTransactionReportRequestDTO(dateString, accountNumber);

    final List<APIErrorDTO> errors = this.transactionReportRequestDTOValidator.findErrors(reportRequestDTO);
    if (!errors.isEmpty()) {
      return this.buildValidationErrorResponse(errors);
    }

    final DailyTransactionReport report = this.reportService.generateDailyTransactionReport(this.reportAssembler.toReportRequest(reportRequestDTO));
    return new ResponseEntity<>(this.reportAssembler.toReportResponse(report), HttpStatus.OK);
  }
}
