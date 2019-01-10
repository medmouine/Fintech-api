package ca.ulaval.glo4002.trading.report.controller;

import ca.ulaval.glo4002.trading.accounts.domain.AccountNumber;
import ca.ulaval.glo4002.trading.common.controller.dto.errors.APIErrorDTO;
import ca.ulaval.glo4002.trading.report.controller.dto.DailyTransactionReportRequestDTO;
import ca.ulaval.glo4002.trading.report.controller.dto.errors.MissingReportTypeError;
import ca.ulaval.glo4002.trading.report.controller.dto.errors.UnsupportedReportTypeError;
import ca.ulaval.glo4002.trading.report.controller.response.DailyTransactionReportResponse;
import ca.ulaval.glo4002.trading.report.controller.subcontrollers.ReportSubcontrollerSelector;
import ca.ulaval.glo4002.trading.report.domain.generator.request.DailyTransactionReportRequest;
import ca.ulaval.glo4002.trading.report.domain.reports.ReportSubcontroller;
import ca.ulaval.glo4002.trading.report.domain.reports.ReportType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReportControllerTest {
  private static final String AN_ACCOUNT_NUMBER_STRING = "DD-12312";
  private static final String A_DATE_STRING = "2018-09-09";
  private static final String INVALID_REPORT_TYPE = "AAAAAAAAAA";
  private static final String VALID_REPORT_TYPE = "DAILY";
  private static final AccountNumber AN_ACCOUNT_NUMBER = new AccountNumber(AN_ACCOUNT_NUMBER_STRING);

  @Mock
  private ResponseEntity response;
  @Mock
  private DailyTransactionReportResponse dailyTransactionReportResponse;
  @Mock
  private ReportSubcontrollerSelector reportSubcontrollerSelector;
  @Mock
  private ReportSubcontroller reportSubcontroller;
  private ReportController reportController;

  @Before
  public void setup() {
    this.reportController = new ReportController(this.reportSubcontrollerSelector);
  }

  @Test
  public void givenARequestWithAnUnsupportedReportType_whenGettingATransactionReport_thenTheResultIsAListWithAnUnsupportedReportTypeError() {
    final List<APIErrorDTO> expectedErrorList = Collections.singletonList(new UnsupportedReportTypeError(INVALID_REPORT_TYPE));

    final ResponseEntity<?> response = this.reportController.getReport(AN_ACCOUNT_NUMBER_STRING, INVALID_REPORT_TYPE, A_DATE_STRING);

    assertEquals(expectedErrorList, response.getBody());
  }

  @Test
  public void givenARequestWithEmptyReportType_whenGettingATransactionReport_thenTheResultIsAListWithAnMissingReportTypeError() {
    final List<APIErrorDTO> expectedErrorList = Collections.singletonList(new MissingReportTypeError());

    final ResponseEntity<?> response = this.reportController.getReport(AN_ACCOUNT_NUMBER_STRING, "", A_DATE_STRING);

    assertEquals(expectedErrorList, response.getBody());
  }

  @Test
  public void givenARequestReportRequest_whenGettingATransactionReport_thenResponseIsReturned() {
    when(this.reportSubcontrollerSelector.getSubcontroller(ReportType.DAILY)).thenReturn(this.reportSubcontroller);
    when(this.reportSubcontroller.getTransactionReport(AN_ACCOUNT_NUMBER_STRING, A_DATE_STRING)).thenReturn(response);

    final ResponseEntity<?> expected = this.reportController.getReport(AN_ACCOUNT_NUMBER_STRING, VALID_REPORT_TYPE, A_DATE_STRING);

    assertEquals(expected, response);
  }
}
