package ca.ulaval.glo4002.trading.common.infrastructure.currencyConverter.currencies;

import ca.ulaval.glo4002.trading.common.infrastructure.currencyConverter.AbstractCurrencyConverter;
import ca.ulaval.glo4002.trading.common.infrastructure.currencyConverter.CurrencyConverter;
import ca.ulaval.glo4002.trading.common.infrastructure.currencyConverter.CurrencyConverterFactory;
import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;

public class CADCurrencyConverter extends AbstractCurrencyConverter implements CurrencyConverter {
  private static final float CAD_TO_CAD_EXCHANGE_RATE = 1F;
  private final String CURRENCY_CODE = "CAD";

  @Autowired
  public CADCurrencyConverter(final CurrencyConverterFactory currencyConverterFactory) {
    super(currencyConverterFactory);
  }

  @Override
  protected String getCurrencyCode() {
    return this.CURRENCY_CODE;
  }

  @Override
  public Money convertToCAD(final Money money) {
    return money;
  }

  @Override
  public Money convertFromCAD(final Money money) {
    validateIsCAD(money);
    return money;
  }
}
