package ca.ulaval.glo4002.trading.transactions.controller.response;

import ca.ulaval.glo4002.trading.stocks.controller.dto.StockDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class TransactionResponse {

  private final float fees;
  private final String type;
  private final Instant date;
  private final StockDTO stock;
  private final UUID transactionNumber;
  private final long quantity;
}
