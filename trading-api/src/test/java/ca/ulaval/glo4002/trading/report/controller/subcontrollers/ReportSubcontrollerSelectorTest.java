package ca.ulaval.glo4002.trading.report.controller.subcontrollers;

import ca.ulaval.glo4002.trading.report.domain.reports.ReportSubcontroller;
import ca.ulaval.glo4002.trading.report.domain.reports.ReportType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReportSubcontrollerSelectorTest {

  private static final ReportType REPORT_TYPE = ReportType.DAILY;
  private ReportSubcontrollerSelector reportSubcontrollerSelector;
  @Mock
  private DailyReportSubcontroller dailyReportSubcontroller;

  @Before
  public void setup() {
    this.reportSubcontrollerSelector = new ReportSubcontrollerSelector(this.dailyReportSubcontroller);
    when(this.dailyReportSubcontroller.getReportType()).thenReturn(REPORT_TYPE);
  }

  @Test
  public void givenReportTypeMatchingSubcontroller_whenGetSubcontrollerCalled_thenReturnValidSubcontroller() {

    final ReportSubcontroller reportSubcontroller = this.reportSubcontrollerSelector.getSubcontroller(REPORT_TYPE);

    assertEquals(reportSubcontroller, this.dailyReportSubcontroller);
  }

  @Test(expected = NotImplementedException.class)
  public void givenNoReportTypeMatchingSubController_whenGetSubcontrollerCalled_thenThrowNotImplementedException() {
    this.reportSubcontrollerSelector.getSubcontroller(null);
  }
}
