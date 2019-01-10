package ca.ulaval.glo4002.trading.transactions.controller.response;

import ca.ulaval.glo4002.trading.stocks.controller.dto.StockDTO;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class SellTransactionResponse extends TransactionResponse {

  private final float priceSold;

  public SellTransactionResponse(
          final float fees,
          final String type,
          final Instant date,
          final StockDTO stock,
          final UUID transactionNumber,
          final long quantity,
          final float priceSold) {
    super(fees, type, date, stock, transactionNumber, quantity);

    this.priceSold = priceSold;
  }
}
