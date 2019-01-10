package ca.ulaval.glo4002.trading.accounts.domain;

import ca.ulaval.glo4002.trading.accounts.domain.exceptions.TransactionNotFoundException;
import ca.ulaval.glo4002.trading.common.infrastructure.currencyConverter.CurrencyConverter;
import ca.ulaval.glo4002.trading.common.infrastructure.currencyConverter.CurrencyConverterFactory;
import ca.ulaval.glo4002.trading.stocks.domain.Stock;
import ca.ulaval.glo4002.trading.transactions.controller.exceptions.*;
import ca.ulaval.glo4002.trading.transactions.domain.Transaction;
import ca.ulaval.glo4002.trading.transactions.domain.TransactionRepository;
import lombok.Data;
import org.javamoney.moneta.Money;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Data
public class StockWallet {

  public static final String CAD = "CAD";
  private final TransactionRepository transactionRepository;
  private final List<Money> credits;
  private final CurrencyConverterFactory currencyConverterFactory;

  public StockWallet(final List<Money> credits,
                     final TransactionRepository transactionRepository,
                     final CurrencyConverterFactory currencyConverterFactory) {
    this.credits = credits;
    this.transactionRepository = transactionRepository;
    this.currencyConverterFactory = currencyConverterFactory;
  }

  public Money getCreditsSumInCAD() {
    return this.credits.stream()
            .reduce(Money.zero(Monetary.getCurrency(CAD)), this::sumMoney);
  }

  private Money sumMoney(final Money firstMoney, final Money secondMoney) {
    return this.convertMoneyToCAD(firstMoney).add(this.convertMoneyToCAD(secondMoney));
  }

  private Money convertMoneyToCAD(final Money money) {
    final CurrencyConverter currencyConverter = this.currencyConverterFactory.getInstance(money.getCurrency());
    return currencyConverter.convertToCAD(money);
  }

  public void buyStocks(final UUID buyTransactionNumber, final Money totalPrice) {
    final CurrencyUnit currencyUnit = totalPrice.getCurrency();
    final Money creditForSpecificCurrency = this.getCreditForSpecificCurrency(currencyUnit);
    if (totalPrice.isGreaterThan(creditForSpecificCurrency)) {
      throw new NotEnoughCreditsException(buyTransactionNumber);
    }
    Collections.replaceAll(this.credits, creditForSpecificCurrency, creditForSpecificCurrency.subtract(totalPrice));
  }

  public Money getCreditForSpecificCurrency(final CurrencyUnit currencyUnit) {
    return this.credits.stream()
            .filter(credit -> credit.getCurrency().equals(currencyUnit))
            .findAny()
            .orElse(Money.zero(currencyUnit));
  }

  public void sellStocks(final UUID sellTransactionNumber, final Long quantity, final Money totalPrice, final Stock stock, final Money fees) {
    try {
      this.ensureCanPerformTransaction(sellTransactionNumber, quantity, stock, fees);
      final CurrencyUnit currencyUnit = totalPrice.getCurrency();
      final Money creditForSpecificCurrency = this.getCreditForSpecificCurrency(currencyUnit);

      Collections.replaceAll(this.credits, creditForSpecificCurrency,
              creditForSpecificCurrency.add(totalPrice).subtract(fees));
    } catch (final TransactionNotFoundException ex) {
      throw new InvalidTransactionNumberException(sellTransactionNumber);
    }
  }

  private void ensureCanPerformTransaction(final UUID buyTransactionNumber, final Long quantity, final Stock stock, final Money fees) {
    this.ensureStockMatchesTransaction(buyTransactionNumber, stock);
    this.ensureHasEnoughCreditsForFees(buyTransactionNumber, fees);
    this.ensureHasEnoughStock(buyTransactionNumber, quantity, stock);
  }

  private void ensureHasEnoughStock(final UUID buyTransactionNumber, final Long quantity, final Stock stock) {
    final Long quantityLeft = this.transactionRepository.getStockQuantityLeftForTransaction(buyTransactionNumber);

    if (quantity > quantityLeft) {
      throw new NotEnoughStockException(buyTransactionNumber, stock);
    }
  }

  private void ensureStockMatchesTransaction(final UUID transactionNumber, final Stock stock) {
    final Transaction transaction = this.transactionRepository.findByTransactionNumber(transactionNumber);

    if (!transaction.isWithStock(stock)) {
      throw new StockParametersDontMatchException(transaction.getTransactionNumber());
    }
  }

  private void ensureHasEnoughCreditsForFees(final UUID transactionNumber, final Money fees) {
    final CurrencyUnit currencyUnit = fees.getCurrency();
    final Money creditForSpecificCurrency = this.getCreditForSpecificCurrency(currencyUnit);
    if (creditForSpecificCurrency.isLessThan(fees)) {
      throw new NotEnoughCreditsForFeesException(transactionNumber);
    }
  }
}
