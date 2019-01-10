package ca.ulaval.glo4002.trading.report.controller;

import ca.ulaval.glo4002.trading.common.controller.TradingAdvice;
import ca.ulaval.glo4002.trading.report.common.exceptions.ReportException;
import ca.ulaval.glo4002.trading.report.controller.dto.errors.ReportAPIErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.Collections;
import java.util.List;

@ControllerAdvice
public class ReportAdvice extends TradingAdvice {

  private ResponseEntity<List<ReportAPIErrorDTO>> handleReportException(final ReportException ex, final HttpStatus status) {
    final ReportAPIErrorDTO reportAPIErrorDTO = new ReportAPIErrorDTO(ex.getError(), ex.getDescription());
    final List<ReportAPIErrorDTO> reportAPIErrorDTOList = Collections.singletonList(reportAPIErrorDTO);
    return new ResponseEntity<>(reportAPIErrorDTOList, status);
  }
}
