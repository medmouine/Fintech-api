package ca.ulaval.glo4002.trading.common.infrastructure.currencyConverter.currencies;

import ca.ulaval.glo4002.trading.common.infrastructure.currencyConverter.AbstractCurrencyConverter;
import ca.ulaval.glo4002.trading.common.infrastructure.currencyConverter.CurrencyConverter;
import ca.ulaval.glo4002.trading.common.infrastructure.currencyConverter.CurrencyConverterFactory;
import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class JPYCurrencyConverter extends AbstractCurrencyConverter implements CurrencyConverter {
  private static final float JPY_TO_CAD_EXCHANGE_RATE = 0.01F;
  private final String CURRENCY_CODE = "JPY";

  @Autowired
  public JPYCurrencyConverter(final CurrencyConverterFactory currencyConverterFactory) {
    super(currencyConverterFactory);
  }

  @Override
  protected String getCurrencyCode() {
    return this.CURRENCY_CODE;
  }

  @Override
  public Money convertToCAD(final Money JPYMoney) {
    final BigDecimal valueInCAD = JPYMoney.multiply(JPY_TO_CAD_EXCHANGE_RATE).getNumberStripped().setScale(2, RoundingMode.HALF_UP);
    return Money.of(valueInCAD, CAD);
  }

  @Override
  public Money convertFromCAD(final Money CADMoney) {
    validateIsCAD(CADMoney);
    final BigDecimal valueInJPY = CADMoney.divide(JPY_TO_CAD_EXCHANGE_RATE).getNumberStripped().setScale(2, RoundingMode.HALF_UP);
    return Money.of(valueInJPY, this.CURRENCY_CODE);
  }
}
