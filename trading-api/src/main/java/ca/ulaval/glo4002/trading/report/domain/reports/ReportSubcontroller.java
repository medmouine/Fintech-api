package ca.ulaval.glo4002.trading.report.domain.reports;

import org.springframework.http.ResponseEntity;

public interface ReportSubcontroller {
  ReportType getReportType();

  ResponseEntity<?> getTransactionReport(String accountNumber, String dateString);
}
