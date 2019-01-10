package ca.ulaval.glo4002.trading.report.infrastructure.services;

import ca.ulaval.glo4002.trading.report.domain.generator.DailyTransactionReportGenerator;
import ca.ulaval.glo4002.trading.report.domain.reports.DailyTransactionReport;
import ca.ulaval.glo4002.trading.report.domain.generator.request.DailyTransactionReportRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReportServiceTest {
  @Mock
  private DailyTransactionReportGenerator dailyTransactionReportGenerator;
  @Mock
  private DailyTransactionReportRequest dailyTransactionReportRequest;
  @Mock
  private DailyTransactionReport dailyTransactionReport;
  @InjectMocks
  private ReportService reportService;

  @Test
  public void whenGettingAReport_thenShouldReturnTheReportForThisRequest() {
    when(this.dailyTransactionReportGenerator.generateReport(this.dailyTransactionReportRequest)).thenReturn(this.dailyTransactionReport);

    final DailyTransactionReport generatedReport = this.reportService.generateDailyTransactionReport(this.dailyTransactionReportRequest);

    assertEquals(this.dailyTransactionReport, generatedReport);
  }
}
