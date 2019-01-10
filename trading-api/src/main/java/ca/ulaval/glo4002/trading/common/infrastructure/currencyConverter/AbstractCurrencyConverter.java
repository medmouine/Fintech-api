package ca.ulaval.glo4002.trading.common.infrastructure.currencyConverter;

import ca.ulaval.glo4002.trading.common.infrastructure.currencyConverter.exceptions.InvalidCurrencyException;
import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;

import javax.money.CurrencyUnit;

public abstract class AbstractCurrencyConverter implements CurrencyConverter {
  public static final String CAD = "CAD";
  protected final CurrencyConverterFactory currencyConverterFactory;

  @Autowired
  protected AbstractCurrencyConverter(final CurrencyConverterFactory currencyConverterFactory) {
    this.currencyConverterFactory = currencyConverterFactory;
  }

  protected static void validateIsCAD(final Money CADMoney) {
    if (!CADMoney.getCurrency().getCurrencyCode().equals(CAD)) {
      throw new InvalidCurrencyException(CAD, CADMoney.getCurrency().getCurrencyCode());
    }
  }

  protected abstract String getCurrencyCode();

  @Override
  public Money convertTo(final Money money, final CurrencyUnit currencyUnit) {
    if (currencyUnit.getCurrencyCode().equals(this.getCurrencyCode())) {
      return money;
    }
    final Money CADMoney = this.convertToCAD(money);
    return this.currencyConverterFactory.getInstance(currencyUnit).convertFromCAD(CADMoney);
  }

  @Override
  public Money convertFrom(final Money money) {
    final CurrencyUnit currencyUnit = money.getCurrency();
    if (currencyUnit.getCurrencyCode().equals(this.getCurrencyCode())) {
      return money;
    }
    final Money CADMoney = this.currencyConverterFactory.getInstance(currencyUnit).convertToCAD(money);
    return this.convertFromCAD(CADMoney);
  }
}
