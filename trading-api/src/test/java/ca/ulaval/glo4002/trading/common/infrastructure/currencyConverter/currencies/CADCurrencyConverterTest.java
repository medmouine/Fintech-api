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
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CADCurrencyConverterTest {

  private static final CurrencyUnit CAD_CURRENCY = Monetary.getCurrency("CAD");
  private static final CurrencyUnit JPY_CURRENCY = Monetary.getCurrency("JPY");
  private static final Money CAD_MONEY = Money.of(1.31, CAD_CURRENCY);
  private static final Money CAD_MONEY_TO_JPY = Money.of(131, JPY_CURRENCY);
  private static final double DELTA = 0.01;
  @Mock
  private CurrencyConverterFactory currencyConverterFactory;
  @Mock
  private CurrencyConverter currencyConverter;

  private CADCurrencyConverter CADCurrencyConverter;

  @Before
  public void setup() {
    this.CADCurrencyConverter = new CADCurrencyConverter(this.currencyConverterFactory);
  }

  @Test
  public void givenMoneyInCAD_whenConvertedToCAD_thenConversionIsValid() {
    final Money expectedAmountInCAD = this.CADCurrencyConverter.convertToCAD(CAD_MONEY);

    assertEquals(expectedAmountInCAD.getNumberStripped().floatValue(), CAD_MONEY.getNumberStripped().floatValue(), DELTA);
    assertEquals(expectedAmountInCAD.getCurrency(), CAD_CURRENCY);
  }

  @Test
  public void givenMoneyInCAD_whenConvertedFromCAD_thenConversionIsValid() {
    final Money expectedAmountInCAD = this.CADCurrencyConverter.convertFromCAD(CAD_MONEY);

    assertEquals(expectedAmountInCAD.getNumberStripped().floatValue(), CAD_MONEY.getNumberStripped().floatValue(), DELTA);
    assertEquals(expectedAmountInCAD.getCurrency(), CAD_CURRENCY);
  }

  @Test
  public void givenMoneyInAnyCurrency_whenConvertedFromCurrencyToCAD_thenConversionIsValid() {
    when(this.currencyConverterFactory.getInstance(JPY_CURRENCY)).thenReturn(this.currencyConverter);
    when(this.currencyConverter.convertToCAD(CAD_MONEY_TO_JPY)).thenReturn(CAD_MONEY);

    final Money expectedAmountInCAD = this.CADCurrencyConverter.convertFrom(CAD_MONEY_TO_JPY);

    assertEquals(expectedAmountInCAD.getNumberStripped().floatValue(), CAD_MONEY.getNumberStripped().floatValue(), DELTA);
    assertEquals(expectedAmountInCAD.getCurrency(), CAD_CURRENCY);
  }

  @Test
  public void givenMoneyInAnyCurrency_whenConvertedFromCADToCurrency_thenConversionIsValid() {
    when(this.currencyConverterFactory.getInstance(JPY_CURRENCY)).thenReturn(this.currencyConverter);
    when(this.currencyConverter.convertFromCAD(CAD_MONEY)).thenReturn(CAD_MONEY_TO_JPY);

    final Money expectedAmountInOtherCurrency = this.CADCurrencyConverter.convertTo(CAD_MONEY, JPY_CURRENCY);

    assertEquals(expectedAmountInOtherCurrency.getNumberStripped().floatValue(), CAD_MONEY_TO_JPY.getNumberStripped().floatValue(), DELTA);
    assertEquals(expectedAmountInOtherCurrency.getCurrency(), JPY_CURRENCY);
  }
}
