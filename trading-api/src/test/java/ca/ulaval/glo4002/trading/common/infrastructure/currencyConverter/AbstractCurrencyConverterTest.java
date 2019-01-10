package ca.ulaval.glo4002.trading.common.infrastructure.currencyConverter;

import ca.ulaval.glo4002.trading.common.infrastructure.currencyConverter.exceptions.InvalidCurrencyException;
import org.javamoney.moneta.Money;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class AbstractCurrencyConverterTest {

  private static final CurrencyUnit JPY_CURRENCY = Monetary.getCurrency("JPY");
  private final static Money NON_CAD_MONEY = Money.of(10, "USD");
  private final Money MONEY = Money.of(10, JPY_CURRENCY);

  @Mock
  private CurrencyConverterFactory currencyConverterFactory;

  private final CurrencyConverter currencyConverter = new AbstractCurrencyConverter(currencyConverterFactory) {
    final String currencyCode = JPY_CURRENCY.getCurrencyCode();

    @Override
    protected String getCurrencyCode() {
      return this.currencyCode;
    }

    @Override
    public Money convertToCAD(final Money money) {
      return null;
    }

    @Override
    public Money convertFromCAD(final Money money) throws InvalidCurrencyException {
      return null;
    }
  };

  @Test(expected = InvalidCurrencyException.class)
  public void givenNonCADMoney_whenConvertFromCADCalled_thenInvalidCurrencyExceptionIsCalled() {
    AbstractCurrencyConverter.validateIsCAD(NON_CAD_MONEY);
  }

  @Test
  public void givenAnyCurrency_whenConvertingToSameCurrency_thenSameValueIsReturned() {
    final Money expectedMoney = this.currencyConverter.convertFrom(this.MONEY);

    assertEquals(expectedMoney, this.MONEY);
  }
}
