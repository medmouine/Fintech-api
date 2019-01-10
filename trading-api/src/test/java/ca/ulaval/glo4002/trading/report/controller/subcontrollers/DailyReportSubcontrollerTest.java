package ca.ulaval.glo4002.trading.report.controller.subcontrollers;

import ca.ulaval.glo4002.trading.accounts.domain.AccountNumber;
import ca.ulaval.glo4002.trading.common.controller.dto.Validator;
import ca.ulaval.glo4002.trading.common.controller.dto.errors.APIErrorDTO;
import ca.ulaval.glo4002.trading.report.controller.assembler.DailyReportAssembler;
import ca.ulaval.glo4002.trading.report.controller.dto.DailyTransactionReportRequestDTO;
import ca.ulaval.glo4002.trading.report.controller.response.DailyTransactionReportResponse;
import ca.ulaval.glo4002.trading.report.domain.generator.request.DailyTransactionReportRequest;
import ca.ulaval.glo4002.trading.report.domain.reports.DailyTransactionReport;
import ca.ulaval.glo4002.trading.report.infrastructure.services.ReportService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DailyReportSubcontrollerTest {

  private static final String AN_ACCOUNT_NUMBER_STRING = "DD-12312";
  private static final String A_DATE_STRING = "2018-09-09";

  private static final AccountNumber AN_ACCOUNT_NUMBER = new AccountNumber(AN_ACCOUNT_NUMBER_STRING);

  private static final APIErrorDTO AN_ERROR = new APIErrorDTO("An error", "A Description");
  private static final List<APIErrorDTO> ERROR_LIST = Collections.singletonList(AN_ERROR);

  private final DailyTransactionReportRequestDTO A_DAILY_REPORT_REQUEST_DTO = new DailyTransactionReportRequestDTO(A_DATE_STRING, AN_ACCOUNT_NUMBER_STRING);
  private final DailyTransactionReportRequest A_DAILY_REPORT_REQUEST = new DailyTransactionReportRequest(
          LocalDate.parse(A_DATE_STRING).atStartOfDay().toInstant(OffsetDateTime.now().getOffset()),
          AN_ACCOUNT_NUMBER);

  @Mock
  private ReportService reportService;
  @Mock
  private DailyReportAssembler dailyReportAssembler;
  @Mock
  private DailyTransactionReport dailyTransactionReport;
  @Mock
  private DailyTransactionReportResponse dailyTransactionReportResponse;
  @Mock
  private Validator<DailyTransactionReportRequestDTO> transactionReportRequestDTOValidator;
  @InjectMocks
  private DailyReportSubcontroller dailyReportSubcontroller;

  @Before
  public void setup() {
    when(this.reportService.generateDailyTransactionReport(this.A_DAILY_REPORT_REQUEST)).thenReturn(this.dailyTransactionReport);
    when(this.dailyReportAssembler.toReportRequest(this.A_DAILY_REPORT_REQUEST_DTO)).thenReturn(this.A_DAILY_REPORT_REQUEST);
    when(this.dailyReportAssembler.toReportResponse(this.dailyTransactionReport)).thenReturn(this.dailyTransactionReportResponse);
  }

  @Test
  public void givenARequestWithErrors_whenGettingATransactionReportDaily_thenTheErrorListIsTheResponseBody() {
    when(this.transactionReportRequestDTOValidator.findErrors(this.A_DAILY_REPORT_REQUEST_DTO)).thenReturn(ERROR_LIST);

    final ResponseEntity<?> response = this.dailyReportSubcontroller.getTransactionReport(AN_ACCOUNT_NUMBER_STRING, A_DATE_STRING);

    assertEquals(ERROR_LIST, response.getBody());
  }

  @Test
  public void givenARequestWithErrors_whenGettingATransactionReportDaily_thenTheHttpStatusIsBadRequest() {
    when(this.transactionReportRequestDTOValidator.findErrors(this.A_DAILY_REPORT_REQUEST_DTO)).thenReturn(ERROR_LIST);

    final ResponseEntity<?> response = this.dailyReportSubcontroller.getTransactionReport(AN_ACCOUNT_NUMBER_STRING, A_DATE_STRING);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  public void givenADailyTransactionReportRequest_whenGettingATransactionReport_thenTheHttpStatusIsOK() {
    final ResponseEntity<?> response = this.dailyReportSubcontroller.getTransactionReport(AN_ACCOUNT_NUMBER_STRING, A_DATE_STRING);

    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void givenADailyTransactionReportRequest_whenGettingATransactionReport_thenTheResponseBodyIsTheReportResponse() {
    final ResponseEntity<?> response = this.dailyReportSubcontroller.getTransactionReport(AN_ACCOUNT_NUMBER_STRING, A_DATE_STRING);

    assertEquals(this.dailyTransactionReportResponse, response.getBody());
  }
}