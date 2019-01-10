package ca.ulaval.glo4002.trading.report.controller.assembler;

import ca.ulaval.glo4002.trading.accounts.controller.assembler.AccountNumberAssembler;
import ca.ulaval.glo4002.trading.common.infrastructure.providers.DateTimeProvider;
import ca.ulaval.glo4002.trading.report.controller.dto.DailyTransactionReportRequestDTO;
import ca.ulaval.glo4002.trading.report.controller.response.DailyTransactionReportResponse;
import ca.ulaval.glo4002.trading.report.domain.generator.request.DailyTransactionReportRequest;
import ca.ulaval.glo4002.trading.report.domain.reports.DailyTransactionReport;
import ca.ulaval.glo4002.trading.transactions.controller.assembler.TransactionAssembler;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.stream.Collectors;

@Component
public class DailyReportAssembler {

  private final AccountNumberAssembler accountNumberAssembler;
  private final TransactionAssembler transactionAssembler;
  private final DateTimeProvider dateTimeProvider;


  public DailyReportAssembler(final AccountNumberAssembler accountNumberAssembler,
                              final TransactionAssembler transactionAssembler,
                              final DateTimeProvider dateTimeProvider) {
    this.accountNumberAssembler = accountNumberAssembler;
    this.transactionAssembler = transactionAssembler;
    this.dateTimeProvider = dateTimeProvider;
  }

  public DailyTransactionReportResponse toReportResponse(final DailyTransactionReport report) {
    return new DailyTransactionReportResponse(
            report.getDate().atZone(this.dateTimeProvider.getCurrentZoneOffset()),
            report.getTransactions().stream().map(this.transactionAssembler::toTransactionResponse).collect(Collectors.toList())
    );
  }

  public DailyTransactionReportRequest toReportRequest(final DailyTransactionReportRequestDTO dailyTransactionReportRequestDTO) {
    return new DailyTransactionReportRequest(
            LocalDate.parse(dailyTransactionReportRequestDTO.getDate()).atStartOfDay().toInstant(this.dateTimeProvider.getCurrentZoneOffset()),
            this.accountNumberAssembler.toEntity(dailyTransactionReportRequestDTO.getAccountNumber())
    );
  }
}