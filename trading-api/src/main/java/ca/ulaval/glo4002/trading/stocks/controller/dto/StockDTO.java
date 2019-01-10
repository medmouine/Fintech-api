package ca.ulaval.glo4002.trading.stocks.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StockDTO {

  private String market;

  private String symbol;
}
