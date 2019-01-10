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
public class CHFCurrencyConverterTest {


  private static final CurrencyUnit CHF_CURRENCY = Monetary.getCurrency("CHF");
  private static final CurrencyUnit CAD_CURRENCY = Monetary.getCurrency("CAD");
  private static final CurrencyUnit JPY_CURRENCY = Monetary.getCurrency("JPY");
  private static final Money CAD_MONEY = Money.of(1.45, CAD_CURRENCY);
  private static final Money CAD_MONEY_TO_CHF = Money.of(1, CHF_CURRENCY);
  private static final Money CAD_MONEY_TO_JPY = Money.of(145, JPY_CURRENCY);
  private static final double DELTA = 0.01;

  @Mock
  private CurrencyConverterFactory currencyConverterFactory;

  @Mock
  private CurrencyConverter currencyConverter;

  private CHFCurrencyConverter CHFCurrencyConverter;

  @Before
  public void setup () {
    this.CHFCurrencyConverter = new CHFCurrencyConverter(this.currencyConverterFactory);
  }

  @Test
  public void givenMoneyInCHF_whenConvertedToCAD_thenConversionIsValid() {
    final Money expectedAmountInCAD = this.CHFCurrencyConverter.convertToCAD(CAD_MONEY_TO_CHF);

    assertEquals(expectedAmountInCAD.getNumberStripped().floatValue(), CAD_MONEY.getNumberStripped().floatValue(), DELTA);
    assertEquals(expectedAmountInCAD.getCurrency(), CAD_CURRENCY);
  }

  @Test
  public void givenMoneyInCAD_whenConvertedToCHF_thenConversionIsValid() {
    final Money expectedAmountInCHF = this.CHFCurrencyConverter.convertFromCAD(CAD_MONEY);

    assertEquals(expectedAmountInCHF.getNumberStripped().floatValue(), CAD_MONEY_TO_CHF.getNumberStripped().floatValue(), DELTA);
    assertEquals(expectedAmountInCHF.getCurrency(), CHF_CURRENCY);
  }

  @Test
  public void givenMoneyInAnyCurrency_whenConvertedFromCurrencyToCHF_thenConversionIsValid() {
    when(this.currencyConverterFactory.getInstance(JPY_CURRENCY)).thenReturn(this.currencyConverter);
    when(this.currencyConverter.convertToCAD(CAD_MONEY_TO_JPY)).thenReturn(CAD_MONEY);

    final Money expectedAmountInCHF = this.CHFCurrencyConverter.convertFrom(CAD_MONEY_TO_JPY);

    assertEquals(expectedAmountInCHF.getNumberStripped().floatValue(), CAD_MONEY_TO_CHF.getNumberStripped().floatValue(), DELTA);
    assertEquals(expectedAmountInCHF.getCurrency(), CHF_CURRENCY);
  }

  @Test
  public void givenMoneyInAnyCurrency_whenConvertedFromCHFToCurrency_thenConversionIsValid() {
    when(this.currencyConverterFactory.getInstance(JPY_CURRENCY)).thenReturn(this.currencyConverter);
    when(this.currencyConverter.convertFromCAD(CAD_MONEY)).thenReturn(CAD_MONEY_TO_JPY);

    final Money expectedAmountInOtherCurrency = this.CHFCurrencyConverter.convertTo(CAD_MONEY_TO_CHF, JPY_CURRENCY);

    assertEquals(expectedAmountInOtherCurrency.getNumberStripped().floatValue(), CAD_MONEY_TO_JPY.getNumberStripped().floatValue(), DELTA);
    assertEquals(expectedAmountInOtherCurrency.getCurrency(), JPY_CURRENCY);
  }
}
