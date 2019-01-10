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
public class USDCurrencyConverterTest {

  private static final CurrencyUnit USD_CURRENCY = Monetary.getCurrency("USD");
  private static final CurrencyUnit CAD_CURRENCY = Monetary.getCurrency("CAD");
  private static final CurrencyUnit JPY_CURRENCY = Monetary.getCurrency("JPY");
  private static final Money CAD_MONEY = Money.of(1.31, CAD_CURRENCY);
  private static final Money CAD_MONEY_TO_USD = Money.of(1, USD_CURRENCY);
  private static final Money CAD_MONEY_TO_JPY = Money.of(131, JPY_CURRENCY);
  private static final double DELTA = 0.01;

  @Mock
  private CurrencyConverterFactory currencyConverterFactory;
  @Mock
  private CurrencyConverter currencyConverter;

  private USDCurrencyConverter usdCurrencyConverter;

  @Before
  public void setup() {
    this.usdCurrencyConverter = new USDCurrencyConverter(this.currencyConverterFactory);
  }

  @Test
  public void givenMoneyInUSD_whenConvertedToCAD_thenConversionIsValid() {
    final Money expectedAmountInCAD = this.usdCurrencyConverter.convertToCAD(CAD_MONEY_TO_USD);

    assertEquals(expectedAmountInCAD.getNumberStripped().floatValue(), CAD_MONEY.getNumberStripped().floatValue(), DELTA);
    assertEquals(expectedAmountInCAD.getCurrency(), CAD_CURRENCY);
  }

  @Test
  public void givenMoneyInCAD_whenConvertedToUSD_thenConversionIsValid() {
    final Money expectedAmountInUSD = this.usdCurrencyConverter.convertFromCAD(CAD_MONEY);

    assertEquals(expectedAmountInUSD.getNumberStripped().floatValue(), CAD_MONEY_TO_USD.getNumberStripped().floatValue(), DELTA);
    assertEquals(expectedAmountInUSD.getCurrency(), USD_CURRENCY);
  }

  @Test
  public void givenMoneyInAnyCurrency_whenConvertedFromCurrencyToUSD_thenConversionIsValid() {
    when(this.currencyConverterFactory.getInstance(JPY_CURRENCY)).thenReturn(this.currencyConverter);
    when(this.currencyConverter.convertToCAD(CAD_MONEY_TO_JPY)).thenReturn(CAD_MONEY);

    final Money expectedAmountInUSD = this.usdCurrencyConverter.convertFrom(CAD_MONEY_TO_JPY);

    assertEquals(expectedAmountInUSD.getNumberStripped().floatValue(), CAD_MONEY_TO_USD.getNumberStripped().floatValue(), DELTA);
    assertEquals(expectedAmountInUSD.getCurrency(), USD_CURRENCY);
  }

  @Test
  public void givenMoneyInAnyCurrency_whenConvertedFromUSDToCurrency_thenConversionIsValid() {
    when(this.currencyConverterFactory.getInstance(JPY_CURRENCY)).thenReturn(this.currencyConverter);
    when(this.currencyConverter.convertFromCAD(CAD_MONEY)).thenReturn(CAD_MONEY_TO_JPY);

    final Money expectedAmountInOtherCurrency = this.usdCurrencyConverter.convertTo(CAD_MONEY_TO_USD, JPY_CURRENCY);

    assertEquals(expectedAmountInOtherCurrency.getNumberStripped().floatValue(), CAD_MONEY_TO_JPY.getNumberStripped().floatValue(), DELTA);
    assertEquals(expectedAmountInOtherCurrency.getCurrency(), JPY_CURRENCY);
  }
}
