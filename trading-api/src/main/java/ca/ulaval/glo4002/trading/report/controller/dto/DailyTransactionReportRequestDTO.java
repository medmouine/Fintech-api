package ca.ulaval.glo4002.trading.report.controller.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class DailyTransactionReportRequestDTO {

  private final String date;
  private final String accountNumber;

  public DailyTransactionReportRequestDTO(String date, String accountNumber) {
    this.accountNumber = accountNumber;
    this.date = date;
  }
}
