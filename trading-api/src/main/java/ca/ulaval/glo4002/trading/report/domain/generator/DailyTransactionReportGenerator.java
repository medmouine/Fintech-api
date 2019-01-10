package ca.ulaval.glo4002.trading.report.domain.generator;

import ca.ulaval.glo4002.trading.accounts.domain.AccountRepository;
import ca.ulaval.glo4002.trading.report.domain.reports.DailyTransactionReport;
import ca.ulaval.glo4002.trading.report.domain.generator.request.DailyTransactionReportRequest;
import ca.ulaval.glo4002.trading.transactions.domain.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class DailyTransactionReportGenerator {

  private final TransactionRepository transactionRepository;
  private final AccountRepository accountRepository;

  @Autowired
  public DailyTransactionReportGenerator(final TransactionRepository transactionRepository, final AccountRepository accountRepository) {
    this.transactionRepository = transactionRepository;
    this.accountRepository = accountRepository;
  }

  public DailyTransactionReport generateReport(final DailyTransactionReportRequest reportRequest) {
    this.accountRepository.exists(reportRequest.getAccountNumber());

    final Instant endDate = reportRequest.getEndDate();
    return new DailyTransactionReport(
            endDate,
            this.transactionRepository.findAllTransactionForAccountInTimeRange(
                    reportRequest.getAccountNumber(),
                    reportRequest.getDate(),
                    endDate));
  }
}
