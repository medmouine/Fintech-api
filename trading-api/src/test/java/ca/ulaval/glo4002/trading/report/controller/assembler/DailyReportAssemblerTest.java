package ca.ulaval.glo4002.trading.report.controller.assembler;

import ca.ulaval.glo4002.trading.accounts.controller.assembler.AccountNumberAssembler;
import ca.ulaval.glo4002.trading.accounts.domain.AccountNumber;
import ca.ulaval.glo4002.trading.common.infrastructure.providers.DateTimeProvider;
import ca.ulaval.glo4002.trading.report.controller.dto.DailyTransactionReportRequestDTO;
import ca.ulaval.glo4002.trading.report.controller.response.DailyTransactionReportResponse;
import ca.ulaval.glo4002.trading.report.domain.generator.request.DailyTransactionReportRequest;
import ca.ulaval.glo4002.trading.report.domain.reports.DailyTransactionReport;
import ca.ulaval.glo4002.trading.transactions.controller.assembler.TransactionAssembler;
import ca.ulaval.glo4002.trading.transactions.controller.response.TransactionResponse;
import ca.ulaval.glo4002.trading.transactions.domain.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DailyReportAssemblerTest {

  private final static String ACCOUNT_NUMBER = "AA-1";
  private final static AccountNumber ACCOUNT_NUMBER_ENTITY = new AccountNumber(ACCOUNT_NUMBER);
  private final static ZoneOffset ZONE_OFFSET = ZoneOffset.ofHours(1);
  private final static String DATE_TIME = "2007-12-02T23:00:00Z";
  private final static String DATE = "2007-12-03";
  private final static Instant INSTANT = Instant.parse(DATE_TIME);
  private final static DailyTransactionReportRequest DAILY_TRANSACTION_REPORT_REQUEST = new DailyTransactionReportRequest(INSTANT, ACCOUNT_NUMBER_ENTITY);
  private final static DailyTransactionReportRequestDTO DAILY_TRANSACTION_REPORT_REQUEST_DTO = new DailyTransactionReportRequestDTO(DATE, ACCOUNT_NUMBER);
  private static TransactionResponse TRANSACTION_RESPONSE;
  private static Transaction TRANSACTION;
  private final static DailyTransactionReport DAILY_TRANSACTION_REPORT = new DailyTransactionReport(INSTANT, Arrays.asList(TRANSACTION));
  private final static DailyTransactionReportResponse DAILY_TRANSACTION_REPORT_RESPONSE = new DailyTransactionReportResponse(
          DAILY_TRANSACTION_REPORT.getDate().atZone(ZONE_OFFSET),
          Arrays.asList(TRANSACTION_RESPONSE));
  @Mock
  private AccountNumberAssembler accountNumberAssembler;
  @Mock
  private TransactionAssembler transactionAssembler;
  @Mock
  private DateTimeProvider dateTimeProvider;

  @InjectMocks
  private DailyReportAssembler dailyReportAssembler;

  @Before
  public void setup() {
    when(this.dateTimeProvider.getCurrentZoneOffset()).thenReturn(ZONE_OFFSET);
    when(this.accountNumberAssembler.toEntity(ACCOUNT_NUMBER)).thenReturn(ACCOUNT_NUMBER_ENTITY);
    when(this.transactionAssembler.toTransactionResponse(TRANSACTION)).thenReturn(TRANSACTION_RESPONSE);
  }

  @Test
  public void givenDailyTransactionReport_whenToReportResponse_thenReportResponseHasValidDate() {
    final DailyTransactionReportResponse expected = this.dailyReportAssembler.toReportResponse(DAILY_TRANSACTION_REPORT);

    assertEquals(expected.getDate(), DAILY_TRANSACTION_REPORT_RESPONSE.getDate());
  }

  @Test
  public void givenDailyTransactionReport_whenToReportResponse_thenReportResponseHasValidTransactions() {
    final DailyTransactionReportResponse expected = this.dailyReportAssembler.toReportResponse(DAILY_TRANSACTION_REPORT);

    assertEquals(expected.getTransactions(), DAILY_TRANSACTION_REPORT_RESPONSE.getTransactions());
  }

  @Test
  public void givenDailyTransactionReportRequestDTO_whenToReportRequest_thenReportRequestHasValidAccountNumber() {
    final DailyTransactionReportRequest expected = this.dailyReportAssembler.toReportRequest(DAILY_TRANSACTION_REPORT_REQUEST_DTO);

    assertEquals(expected.getAccountNumber(), DAILY_TRANSACTION_REPORT_REQUEST.getAccountNumber());
  }

  @Test
  public void givenDailyTransactionReportRequestDTO_whenToReportRequest_thenReportRequestHasValidDate() {
    final DailyTransactionReportRequest expected = this.dailyReportAssembler.toReportRequest(DAILY_TRANSACTION_REPORT_REQUEST_DTO);

    assertEquals(expected.getDate(), DAILY_TRANSACTION_REPORT_REQUEST.getDate());
  }
}