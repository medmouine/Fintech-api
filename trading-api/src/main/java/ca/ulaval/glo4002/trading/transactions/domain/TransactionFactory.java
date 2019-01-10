package ca.ulaval.glo4002.trading.transactions.domain;

import ca.ulaval.glo4002.trading.accounts.domain.AccountNumber;
import ca.ulaval.glo4002.trading.markets.domain.MarketOpenedValidator;
import ca.ulaval.glo4002.trading.stocks.domain.StocksRepository;
import ca.ulaval.glo4002.trading.transactions.domain.exceptions.MarketClosedException;
import ca.ulaval.glo4002.trading.transactions.domain.fees.FeesCalculator;
import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.UUID;

@Component
public class TransactionFactory {

  private final StocksRepository stocksRepository;
  private final MarketOpenedValidator marketOpenedValidator;
  private final FeesCalculator feesCalculator;

  @Autowired
  public TransactionFactory(final StocksRepository stocksRepository, final MarketOpenedValidator marketOpenedValidator, final FeesCalculator feesCalculator) {
    this.stocksRepository = stocksRepository;
    this.marketOpenedValidator = marketOpenedValidator;
    this.feesCalculator = feesCalculator;
  }

  public Transaction create(final AccountNumber accountNumber, final TransactionRequest transactionRequest) {
    final UUID transactionId = UUID.randomUUID();
    this.ensureMarketOpened(transactionRequest, transactionId);

    switch (transactionRequest.getType()) {
      case BUY:
        return this.createBuyTransaction(transactionId, accountNumber, transactionRequest);

      case SELL:
        return this.createSellTransaction(transactionId, accountNumber, transactionRequest);

      default:
        throw new NotImplementedException();
    }
  }

  private void ensureMarketOpened(final TransactionRequest transactionRequest, final UUID transactionId) {
    if (!this.marketOpenedValidator.validateAtDate(transactionRequest.getMarket(), transactionRequest.getDate())) {
      throw new MarketClosedException(transactionRequest.getMarket(), transactionId);
    }
  }

  private SellTransaction createSellTransaction(final UUID transactionId, final AccountNumber accountNumber, final TransactionRequest transactionRequest) {
    final Money stockUnitPrice = this.getStockUnitPrice(transactionRequest);
    return new SellTransaction(transactionId,
            accountNumber,
            transactionRequest.getDate(),
            transactionRequest.getStock(),
            transactionRequest.getQuantity(),
            stockUnitPrice,
            this.feesCalculator,
            transactionRequest.getTransactionNumber());
  }

  private BuyTransaction createBuyTransaction(final UUID transactionId, final AccountNumber accountNumber, final TransactionRequest transactionRequest) {
    final Money stockUnitPrice = this.getStockUnitPrice(transactionRequest);
    return new BuyTransaction(transactionId,
            accountNumber,
            transactionRequest.getDate(),
            transactionRequest.getStock(),
            transactionRequest.getQuantity(),
            stockUnitPrice,
            this.feesCalculator);
  }

  private Money getStockUnitPrice(final TransactionRequest transactionRequest) {
    return this.stocksRepository.findPriceBy(transactionRequest.getStock(), transactionRequest.getDate());
  }
}
