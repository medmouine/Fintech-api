package ca.ulaval.glo4002.trading.transactions.controller.assembler;

import ca.ulaval.glo4002.trading.stocks.controller.assembler.StockAssembler;
import ca.ulaval.glo4002.trading.stocks.controller.dto.StockDTO;
import ca.ulaval.glo4002.trading.transactions.controller.dto.TransactionType;
import ca.ulaval.glo4002.trading.transactions.controller.response.BuyTransactionResponse;
import ca.ulaval.glo4002.trading.transactions.controller.response.SellTransactionResponse;
import ca.ulaval.glo4002.trading.transactions.controller.response.TransactionResponse;
import ca.ulaval.glo4002.trading.transactions.domain.BuyTransaction;
import ca.ulaval.glo4002.trading.transactions.domain.SellTransaction;
import ca.ulaval.glo4002.trading.transactions.domain.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@Component
public class TransactionAssembler {

  private final StockAssembler stockAssembler;

  @Autowired
  public TransactionAssembler(final StockAssembler stockAssembler) {
    this.stockAssembler = stockAssembler;
  }

  public TransactionResponse toTransactionResponse(final Transaction transaction) {
    if (transaction instanceof BuyTransaction) {
      return this.toBuyTransactionResponse((BuyTransaction) transaction);
    } else if (transaction instanceof SellTransaction) {
      return this.toSellTransactionResponse((SellTransaction) transaction);
    }
    throw new NotImplementedException();
  }

  private SellTransactionResponse toSellTransactionResponse(final SellTransaction sellTransaction) {
    final StockDTO stockDTO = this.stockAssembler.toDTO(sellTransaction.getStock());
    return new SellTransactionResponse(sellTransaction.getFeesFloatValue(),
            TransactionType.SELL,
            sellTransaction.getDate(),
            stockDTO,
            sellTransaction.getTransactionNumber(),
            sellTransaction.getQuantity(),
            sellTransaction.getStockUnitPriceFloatValue());
  }

  private BuyTransactionResponse toBuyTransactionResponse(final BuyTransaction buyTransaction) {
    final StockDTO stockDTO = this.stockAssembler.toDTO(buyTransaction.getStock());
    return new BuyTransactionResponse(buyTransaction.getFeesFloatValue(),
            TransactionType.BUY,
            buyTransaction.getDate(),
            stockDTO,
            buyTransaction.getTransactionNumber(),
            buyTransaction.getQuantity(),
            buyTransaction.getStockUnitPriceFloatValue());
  }
}
