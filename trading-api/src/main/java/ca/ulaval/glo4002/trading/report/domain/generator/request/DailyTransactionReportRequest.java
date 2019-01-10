package ca.ulaval.glo4002.trading.report.domain.generator.request;

import ca.ulaval.glo4002.trading.accounts.domain.AccountNumber;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Getter
@EqualsAndHashCode(callSuper = true)
public class DailyTransactionReportRequest extends ReportRequest {

  private final Instant date;
  private final AccountNumber accountNumber;

  public DailyTransactionReportRequest(final Instant date, final AccountNumber accountNumber) {
    this.accountNumber = accountNumber;
    this.date = date;
  }

  public Instant getEndDate() {
    return this.date.plus(1, ChronoUnit.DAYS).minus(1, ChronoUnit.MILLIS);
  }
}
