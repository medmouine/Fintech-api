package ca.ulaval.glo4002.trading.report.infrastructure.services;

import ca.ulaval.glo4002.trading.report.domain.generator.DailyTransactionReportGenerator;
import ca.ulaval.glo4002.trading.report.domain.generator.request.DailyTransactionReportRequest;
import ca.ulaval.glo4002.trading.report.domain.reports.DailyTransactionReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportService {

  private final DailyTransactionReportGenerator dailyTransactionReportGenerator;

  @Autowired
  public ReportService(final DailyTransactionReportGenerator dailyTransactionReportGenerator) {
    this.dailyTransactionReportGenerator = dailyTransactionReportGenerator;
  }

  public DailyTransactionReport generateDailyTransactionReport(final DailyTransactionReportRequest reportRequest) {
    return this.dailyTransactionReportGenerator.generateReport(reportRequest);
  }
}