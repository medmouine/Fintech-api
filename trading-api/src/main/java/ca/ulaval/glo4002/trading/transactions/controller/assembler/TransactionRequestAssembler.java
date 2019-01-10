package ca.ulaval.glo4002.trading.transactions.controller.assembler;

import ca.ulaval.glo4002.trading.markets.domain.Market;
import ca.ulaval.glo4002.trading.stocks.controller.assembler.StockAssembler;
import ca.ulaval.glo4002.trading.transactions.controller.dto.TransactionRequestDTO;
import ca.ulaval.glo4002.trading.transactions.domain.TransactionRequest;
import ca.ulaval.glo4002.trading.transactions.domain.TransactionRequestType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.money.CurrencyUnit;

@Component
public class TransactionRequestAssembler {

  private final StockAssembler stockAssembler;

  @Autowired
  public TransactionRequestAssembler(final StockAssembler stockAssembler) {
    this.stockAssembler = stockAssembler;
  }

  public TransactionRequest toEntity(final TransactionRequestDTO transactionRequestDTO) {
    final Market marketName = Market.valueOf(transactionRequestDTO.getStock().getMarket());
    final CurrencyUnit currency = marketName.getCurrencyUnit();
    return new TransactionRequest(
            TransactionRequestType.valueOf(transactionRequestDTO.getType()),
            transactionRequestDTO.getDate(),
            this.stockAssembler.toEntity(transactionRequestDTO.getStock()),
            transactionRequestDTO.getTransactionNumber(),
            transactionRequestDTO.getQuantity(),
            currency);
  }
}
