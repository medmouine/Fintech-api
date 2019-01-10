package ca.ulaval.glo4002.trading.common.infrastructure.currencyConverter.currencies;

import ca.ulaval.glo4002.trading.common.infrastructure.currencyConverter.AbstractCurrencyConverter;
import ca.ulaval.glo4002.trading.common.infrastructure.currencyConverter.CurrencyConverter;
import ca.ulaval.glo4002.trading.common.infrastructure.currencyConverter.CurrencyConverterFactory;
import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class USDCurrencyConverter extends AbstractCurrencyConverter implements CurrencyConverter {
  private static final float USD_TO_CAD_EXCHANGE_RATE = 1.31F;
  private final String CURRENCY_CODE = "USD";

  @Autowired
  public USDCurrencyConverter(final CurrencyConverterFactory currencyConverterFactory) {
    super(currencyConverterFactory);
  }

  @Override
  protected String getCurrencyCode() {
    return this.CURRENCY_CODE;
  }

  @Override
  public Money convertToCAD(final Money USDMoney) {
    final BigDecimal valueInCAD = USDMoney.multiply(USD_TO_CAD_EXCHANGE_RATE).getNumberStripped().setScale(2, RoundingMode.HALF_UP);
    return Money.of(valueInCAD, CAD);
  }

  @Override
  public Money convertFromCAD(final Money CADMoney) {
    validateIsCAD(CADMoney);
    final BigDecimal valueInUSD = CADMoney.divide(USD_TO_CAD_EXCHANGE_RATE).getNumberStripped().setScale(2, RoundingMode.HALF_UP);
    return Money.of(valueInUSD, this.CURRENCY_CODE);
  }
}
