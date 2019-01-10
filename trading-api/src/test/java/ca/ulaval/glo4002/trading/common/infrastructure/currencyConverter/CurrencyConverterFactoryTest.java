package ca.ulaval.glo4002.trading.common.infrastructure.currencyConverter;

import ca.ulaval.glo4002.trading.common.infrastructure.currencyConverter.currencies.CADCurrencyConverter;
import ca.ulaval.glo4002.trading.common.infrastructure.currencyConverter.currencies.CHFCurrencyConverter;
import ca.ulaval.glo4002.trading.common.infrastructure.currencyConverter.currencies.JPYCurrencyConverter;
import ca.ulaval.glo4002.trading.common.infrastructure.currencyConverter.currencies.USDCurrencyConverter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class CurrencyConverterFactoryTest {

  private static final CurrencyUnit CADCurrency = Monetary.getCurrency("CAD");
  private static final CurrencyUnit USDCurrency = Monetary.getCurrency("USD");
  private static final CurrencyUnit JPYCurrency = Monetary.getCurrency("JPY");
  private static final CurrencyUnit CHFCurrency = Monetary.getCurrency("CHF");
  private static final CurrencyUnit EURCurrency = Monetary.getCurrency("EUR");

  private CurrencyConverterFactory currencyConverterFactory = new CurrencyConverterFactory();

  @Test
  public void givenCADCurrency_whenCreatedIsCalled_ThenReturnCADCurrencyConverter() {
    final CurrencyConverter expectedCurrencyConverter = this.currencyConverterFactory.getInstance(CADCurrency);

    assertTrue(expectedCurrencyConverter instanceof CADCurrencyConverter);
  }

  @Test
  public void givenUSDCurrency_whenCreatedIsCalled_ThenReturnUSDCurrencyConverter() {
    final CurrencyConverter expectedCurrencyConverter = this.currencyConverterFactory.getInstance(USDCurrency);

    assertTrue(expectedCurrencyConverter instanceof USDCurrencyConverter);
  }

  @Test
  public void givenJPYCurrency_whenCreatedIsCalled_ThenReturnJPYCurrencyConverter() {
    final CurrencyConverter expectedCurrencyConverter = this.currencyConverterFactory.getInstance(JPYCurrency);

    assertTrue(expectedCurrencyConverter instanceof JPYCurrencyConverter);
  }

  @Test
  public void givenCHFCurrency_whenCreatedIsCalled_ThenReturnCHFCurrencyConverter() {
    final CurrencyConverter expectedCurrencyConverter = this.currencyConverterFactory.getInstance(CHFCurrency);

    assertTrue(expectedCurrencyConverter instanceof CHFCurrencyConverter);
  }

  @Test(expected = NotImplementedException.class)
  public void givenNotImplementedCurrency_whenCreatedIsCalled_ThenThrowNotImplementedException() {
    this.currencyConverterFactory.getInstance(EURCurrency);
  }
}
