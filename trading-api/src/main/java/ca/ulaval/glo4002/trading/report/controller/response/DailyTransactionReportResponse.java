package ca.ulaval.glo4002.trading.report.controller.response;

import ca.ulaval.glo4002.trading.transactions.controller.response.TransactionResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class DailyTransactionReportResponse {
  private final ZonedDateTime date;
  private final List<TransactionResponse> transactions;
}