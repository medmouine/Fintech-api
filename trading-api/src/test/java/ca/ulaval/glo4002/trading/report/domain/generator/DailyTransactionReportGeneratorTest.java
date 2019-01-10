package ca.ulaval.glo4002.trading.report.domain.generator;

import ca.ulaval.glo4002.trading.accounts.domain.AccountNumber;
import ca.ulaval.glo4002.trading.accounts.domain.AccountRepository;
import ca.ulaval.glo4002.trading.report.domain.generator.request.DailyTransactionReportRequest;
import ca.ulaval.glo4002.trading.report.domain.reports.DailyTransactionReport;
import ca.ulaval.glo4002.trading.transactions.domain.Transaction;
import ca.ulaval.glo4002.trading.transactions.domain.TransactionRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DailyTransactionReportGeneratorTest {

  private final Instant A_DATE = LocalDate.parse("2018-09-09").atStartOfDay().toInstant(OffsetDateTime.now().getOffset());
  private final Instant A_END_DATE = this.A_DATE.plus(1, ChronoUnit.DAYS).minus(1, ChronoUnit.MILLIS);
  private final AccountNumber AN_ACCOUNT_NUMBER = new AccountNumber("VR-123");
  private final List<Transaction> A_TRANSACTION_LIST = Collections.singletonList(mock(Transaction.class));
  private final DailyTransactionReportRequest A_DAILY_TRANSACTION_REPORT_REQUEST = new DailyTransactionReportRequest(this.A_DATE, this.AN_ACCOUNT_NUMBER);

  @Mock
  private TransactionRepository transactionRepository;

  @Mock
  private AccountRepository accountRepository;

  @InjectMocks
  private DailyTransactionReportGenerator dailyTransactionReportGenerator;

  @Before
  public void setup() {
    when(this.transactionRepository.findAllTransactionForAccountInTimeRange(this.AN_ACCOUNT_NUMBER, this.A_DATE, this.A_END_DATE)).thenReturn(this.A_TRANSACTION_LIST);
  }

  @Test
  public void givenADailyTransactionRequest_whenGeneratingReport_thenDailyTransactionReportGenerated() {
    final DailyTransactionReport report = dailyTransactionReportGenerator.generateReport(this.A_DAILY_TRANSACTION_REPORT_REQUEST);
    assertEquals(this.A_END_DATE, report.getDate());
    assertEquals(this.A_TRANSACTION_LIST, report.getTransactions());
  }

  @Test
  public void givenADailyTransactionRequest_whenGeneratingReport_thenVerifyThatAccountExists() {
    dailyTransactionReportGenerator.generateReport(this.A_DAILY_TRANSACTION_REPORT_REQUEST);
    verify(this.accountRepository, times(1)).exists(this.AN_ACCOUNT_NUMBER);
  }
}