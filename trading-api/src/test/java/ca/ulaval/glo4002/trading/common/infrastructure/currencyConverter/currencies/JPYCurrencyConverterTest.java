package ca.ulaval.glo4002.trading.common.infrastructure.currencyConverter.currencies;

import ca.ulaval.glo4002.trading.common.infrastructure.currencyConverter.CurrencyConverter;
import ca.ulaval.glo4002.trading.common.infrastructure.currencyConverter.CurrencyConverterFactory;
import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JPYCurrencyConverterTest {

  private static final CurrencyUnit JPY_CURRENCY = Monetary.getCurrency("JPY");
  private static final CurrencyUnit CAD_CURRENCY = Monetary.getCurrency("CAD");
  private static final CurrencyUnit CHF_CURRENCY = Monetary.getCurrency("CHF");
  private static final Money CAD_MONEY = Money.of(1.45, CAD_CURRENCY);
  private static final Money CAD_MONEY_TO_JPY = Money.of(145, JPY_CURRENCY);
  private static final Money CAD_MONEY_TO_CHF = Money.of(1, CHF_CURRENCY);
  private static final double DELTA = 0.01;

  @Mock
  private CurrencyConverterFactory currencyConverterFactory;

  @Mock
  private CurrencyConverter currencyConverter;

  private JPYCurrencyConverter JPYCurrencyConverter;

  @Before
  public void setup() {
    this.JPYCurrencyConverter = new JPYCurrencyConverter(this.currencyConverterFactory);
  }

  @Test
  public void givenMoneyInJPY_whenConvertedToCAD_thenConversionIsValid() {
    final Money expectedAmountInCAD = this.JPYCurrencyConverter.convertToCAD(CAD_MONEY_TO_JPY);

    assertEquals(expectedAmountInCAD.getNumberStripped().floatValue(), CAD_MONEY.getNumberStripped().floatValue(), DELTA);
    assertEquals(expectedAmountInCAD.getCurrency(), CAD_CURRENCY);
  }

  @Test
  public void givenMoneyInCAD_whenConvertedToJPY_thenConversionIsValid() {
    final Money expectedAmountInJPY = this.JPYCurrencyConverter.convertFromCAD(CAD_MONEY);

    assertEquals(expectedAmountInJPY.getNumberStripped().floatValue(), CAD_MONEY_TO_JPY.getNumberStripped().floatValue(), DELTA);
    assertEquals(expectedAmountInJPY.getCurrency(), JPY_CURRENCY);
  }

  @Test
  public void givenMoneyInAnyCurrency_whenConvertedFromCurrencyToJPY_thenConversionIsValid() {
    when(this.currencyConverterFactory.getInstance(CHF_CURRENCY)).thenReturn(this.currencyConverter);
    when(this.currencyConverter.convertToCAD(CAD_MONEY_TO_CHF)).thenReturn(CAD_MONEY);

    final Money expectedAmountInJPY = this.JPYCurrencyConverter.convertFrom(CAD_MONEY_TO_CHF);

    assertEquals(expectedAmountInJPY.getNumberStripped().floatValue(), CAD_MONEY_TO_JPY.getNumberStripped().floatValue(), DELTA);
    assertEquals(expectedAmountInJPY.getCurrency(), JPY_CURRENCY);
  }

  @Test
  public void givenMoneyInAnyCurrency_whenConvertedFromJPYToCurrency_thenConversionIsValid() {
    when(this.currencyConverterFactory.getInstance(CHF_CURRENCY)).thenReturn(this.currencyConverter);
    when(this.currencyConverter.convertFromCAD(CAD_MONEY)).thenReturn(CAD_MONEY_TO_CHF);

    final Money expectedAmountInOtherCurrency = this.JPYCurrencyConverter.convertTo(CAD_MONEY_TO_JPY, CHF_CURRENCY);

    assertEquals(expectedAmountInOtherCurrency.getNumberStripped().floatValue(), CAD_MONEY_TO_CHF.getNumberStripped().floatValue(), DELTA);
    assertEquals(expectedAmountInOtherCurrency.getCurrency(), CHF_CURRENCY);
  }
}
