package ca.ulaval.glo4002.trading.report.domain.reports;

import ca.ulaval.glo4002.trading.transactions.domain.Transaction;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Getter
public class DailyTransactionReport {
  private final Instant date;
  private final List<Transaction> transactions;

  public DailyTransactionReport(final Instant date, final List<Transaction> transactions) {
    this.date = date;
    this.transactions = transactions;
  }
}
