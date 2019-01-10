package ca.ulaval.glo4002.trading.transactions.infrastructure.repository;

import ca.ulaval.glo4002.trading.accounts.domain.AccountNumber;
import ca.ulaval.glo4002.trading.markets.domain.Market;
import ca.ulaval.glo4002.trading.stocks.domain.Stock;
import ca.ulaval.glo4002.trading.transactions.controller.dto.TransactionType;
import ca.ulaval.glo4002.trading.transactions.domain.BuyTransaction;
import ca.ulaval.glo4002.trading.transactions.domain.SellTransaction;
import ca.ulaval.glo4002.trading.transactions.domain.Transaction;
import ca.ulaval.glo4002.trading.transactions.domain.fees.FeesCalculator;
import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.money.CurrencyUnit;
import java.util.ArrayList;
import java.util.List;

@Component
public class TransactionHibernateEntityAssembler {

  private final FeesCalculator feesCalculator;

  @Autowired
  public TransactionHibernateEntityAssembler(final FeesCalculator feesCalculator) {
    this.feesCalculator = feesCalculator;
  }

  public TransactionHibernateEntity from(final Transaction transaction) {
    if (transaction instanceof BuyTransaction) {
      return this.assignBuyTransactionFields((BuyTransaction) transaction);
    } else if (transaction instanceof SellTransaction) {
      return this.assignSellTransactionFields((SellTransaction) transaction);
    } else {
      throw new NotImplementedException();
    }
  }

  private TransactionHibernateEntity assignSellTransactionFields(final SellTransaction transaction) {
    return new TransactionHibernateEntity(
            transaction.getTransactionNumber(),
            transaction.getAccountNumber().getValue(),
            transaction.getDate(),
            transaction.getQuantity(),
            transaction.getStockUnitPrice().getNumberStripped(),
            transaction.getFees().getNumberStripped(),
            transaction.getMarket().toString(),
            transaction.getStock().getSymbol(),
            TransactionType.SELL,
            transaction.getAssociatedBuyTransactionNumber()
    );
  }

  private TransactionHibernateEntity assignBuyTransactionFields(final BuyTransaction transaction) {
    return new TransactionHibernateEntity(
            transaction.getTransactionNumber(),
            transaction.getAccountNumber().getValue(),
            transaction.getDate(),
            transaction.getQuantity(),
            transaction.getStockUnitPrice().getNumberStripped(),
            transaction.getFees().getNumberStripped(),
            transaction.getMarket().toString(),
            transaction.getStock().getSymbol(),
            TransactionType.BUY,
            null
    );
  }

  public List<Transaction> from(final List<TransactionHibernateEntity> entities) {
    final List<Transaction> transactions = new ArrayList<>();

    for (final TransactionHibernateEntity entity : entities) {
      transactions.add(this.from(entity));
    }

    return transactions;
  }

  public Transaction from(final TransactionHibernateEntity entity) {
    if (entity.getType().equals(TransactionType.SELL)) {
      return this.toSellTransaction(entity);
    } else if (entity.getType().equals(TransactionType.BUY)) {
      return this.toBuyTransaction(entity);
    }
    throw new NotImplementedException();
  }

  private Transaction toBuyTransaction(final TransactionHibernateEntity entity) {
    final Market market = Market.valueOf(entity.getStockMarket());
    final CurrencyUnit currencyUnit = market.getCurrencyUnit();
    return new BuyTransaction(
            entity.getTransactionNumber(),
            new AccountNumber(entity.getAccountNumber()),
            entity.getDate(),
            new Stock(market, entity.getStockSymbol()),
            entity.getQuantity(),
            Money.of(entity.getStockUnitPrice(), currencyUnit),
            this.feesCalculator);
  }

  private Transaction toSellTransaction(final TransactionHibernateEntity entity) {
    final Market market = Market.valueOf(entity.getStockMarket());
    final CurrencyUnit currencyUnit = market.getCurrencyUnit();
    return new SellTransaction(
            entity.getTransactionNumber(),
            new AccountNumber(entity.getAccountNumber()),
            entity.getDate(),
            new Stock(market, entity.getStockSymbol()),
            entity.getQuantity(),
            Money.of(entity.getStockUnitPrice(), currencyUnit),
            this.feesCalculator,
            entity.getAssociatedBuyTransactionNumber());
  }
}
