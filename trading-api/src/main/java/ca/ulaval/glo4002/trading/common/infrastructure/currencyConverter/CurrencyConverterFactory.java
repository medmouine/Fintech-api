package ca.ulaval.glo4002.trading.common.infrastructure.currencyConverter;

import ca.ulaval.glo4002.trading.common.infrastructure.currencyConverter.currencies.CADCurrencyConverter;
import ca.ulaval.glo4002.trading.common.infrastructure.currencyConverter.currencies.CHFCurrencyConverter;
import ca.ulaval.glo4002.trading.common.infrastructure.currencyConverter.currencies.JPYCurrencyConverter;
import ca.ulaval.glo4002.trading.common.infrastructure.currencyConverter.currencies.USDCurrencyConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.money.CurrencyUnit;
import java.util.Objects;

@Component
public class CurrencyConverterFactory {

  private static final String CAD = "CAD";
  private static final String JPY = "JPY";
  private static final String CHF = "CHF";
  private static final String USD = "USD";

  public CurrencyConverter getInstance(final CurrencyUnit currencyUnit) {
    Objects.requireNonNull(currencyUnit);
    if (currencyUnit.getCurrencyCode().equals(USD)) {
      return new USDCurrencyConverter(this);
    }
    if (currencyUnit.getCurrencyCode().equals(JPY)) {
      return new JPYCurrencyConverter(this);
    }
    if (currencyUnit.getCurrencyCode().equals(CHF)) {
      return new CHFCurrencyConverter(this);
    }
    if (currencyUnit.getCurrencyCode().equals(CAD)) {
      return new CADCurrencyConverter(this);
    }
    throw new NotImplementedException();
  }
}
