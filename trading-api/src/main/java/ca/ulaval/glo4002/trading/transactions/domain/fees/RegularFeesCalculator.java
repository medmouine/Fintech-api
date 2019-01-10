package ca.ulaval.glo4002.trading.transactions.domain.fees;

import ca.ulaval.glo4002.trading.common.infrastructure.currencyConverter.CurrencyConverter;
import ca.ulaval.glo4002.trading.common.infrastructure.currencyConverter.CurrencyConverterFactory;
import ca.ulaval.glo4002.trading.transactions.domain.TransactionRequest;
import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

@Component
public class RegularFeesCalculator implements FeesCalculator {

  public static final String CAD = "CAD";
  public static final CurrencyUnit CADCurrency = Monetary.getCurrency(CAD);
  public static final Money TRANSACTION_FEES_FOR_QUANTITY_LESS_THAN_THRESHOLD_IN_CAD = Money.of(0.25, CADCurrency);
  public static final Money TRANSACTION_FEES_FOR_QUANTITY_MORE_THAN_THRESHOLD_IN_CAD = Money.of(0.20, CADCurrency);
  public static final int QUANTITY_THRESHOLD = 100;
  public static final Money ADDITIONAL_FEES_TRANSACTION_PRICE_THRESHOLD_IN_CAD = Money.of(5000, CADCurrency);
  public static final double ADDITIONAL_FEES_PRICE_PERCENTAGE = 0.03;
  private CurrencyConverterFactory currencyConverterFactory;

  @Autowired
  public RegularFeesCalculator(final CurrencyConverterFactory currencyConverterFactory) {
    this.currencyConverterFactory = currencyConverterFactory;
  }

  @Override
  public Money calculateTransactionFees(final Money stockUnitPrice,
                                        final long quantity) {
    final CurrencyConverter currencyConverter = this.currencyConverterFactory.getInstance(stockUnitPrice.getCurrency());

    final Money baseFeesInCad = this.getBaseFees(quantity);
    final Money convertedBaseFees = currencyConverter.convertFromCAD(baseFeesInCad);
    return convertedBaseFees.add(this.getAdditionalFees(stockUnitPrice, quantity));
  }

  private Money getBaseFees(final long quantity) {
    if (quantity <= QUANTITY_THRESHOLD) {
      return TRANSACTION_FEES_FOR_QUANTITY_LESS_THAN_THRESHOLD_IN_CAD.multiply(quantity);
    }
    return TRANSACTION_FEES_FOR_QUANTITY_MORE_THAN_THRESHOLD_IN_CAD.multiply(quantity);
  }

  private Money getAdditionalFees(final Money stockUnitPrice, final long quantity) {
    final Money transactionPrice = this.calculatePriceBeforeFees(stockUnitPrice, quantity);
    final CurrencyConverter currencyConverter = this.currencyConverterFactory.getInstance(stockUnitPrice.getCurrency());

    final Money convertedAdditionalFees = currencyConverter.convertFromCAD(ADDITIONAL_FEES_TRANSACTION_PRICE_THRESHOLD_IN_CAD);
    if (transactionPrice.isGreaterThan(convertedAdditionalFees)) {
      return transactionPrice.multiply(ADDITIONAL_FEES_PRICE_PERCENTAGE);
    }

    return Money.zero(stockUnitPrice.getCurrency());
  }

  private Money calculatePriceBeforeFees(final Money stockUnitPrice, final long quantity) {
    return stockUnitPrice.multiply(quantity);
  }
}