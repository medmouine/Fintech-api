package ca.ulaval.glo4002.trading.transactions.domain.fees;

import ca.ulaval.glo4002.trading.common.infrastructure.currencyConverter.CurrencyConverter;
import ca.ulaval.glo4002.trading.common.infrastructure.currencyConverter.CurrencyConverterFactory;
import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static ca.ulaval.glo4002.trading.transactions.domain.fees.RegularFeesCalculator.ADDITIONAL_FEES_TRANSACTION_PRICE_THRESHOLD_IN_CAD;
import static ca.ulaval.glo4002.trading.transactions.domain.fees.RegularFeesCalculator.TRANSACTION_FEES_FOR_QUANTITY_MORE_THAN_THRESHOLD_IN_CAD;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class RegularFeesCalculatorTest {

  private static final String CAD = "CAD";
  private static final long A_QUANTITY_LOWER_THAN_THRESHOLD = 50;
  private static final long A_QUANTITY_ABOVE_THRESHOLD = 150;
  private static final Money A_STOCK_UNIT_PRICE_IN_CAD = Money.of(10, CAD);
  private static final Money BASE_FEES_FOR_QUANTITY_ABOVE_THRESHOLD_IN_CAD = Money.of(30, CAD);
  private static final Money BASE_FEES_FOR_QUANTITY_UNDER_THRESHOLD_IN_CAD = Money.of(12.5, CAD);
  private static final double DELTA = 0.1;

  @Mock
  private CurrencyConverterFactory currencyConverterFactory;
  @Mock
  private CurrencyConverter currencyConverter;
  private RegularFeesCalculator feesCalculator;

  @Before
  public void setup() {
    this.feesCalculator = new RegularFeesCalculator(this.currencyConverterFactory);
    when(this.currencyConverterFactory.getInstance(A_STOCK_UNIT_PRICE_IN_CAD.getCurrency())).thenReturn(this.currencyConverter);
    when(this.currencyConverter.convertFromCAD(A_STOCK_UNIT_PRICE_IN_CAD)).thenReturn(A_STOCK_UNIT_PRICE_IN_CAD);
    when(this.currencyConverter.convertToCAD(A_STOCK_UNIT_PRICE_IN_CAD)).thenReturn(A_STOCK_UNIT_PRICE_IN_CAD);
    when(this.currencyConverter.convertFromCAD(ADDITIONAL_FEES_TRANSACTION_PRICE_THRESHOLD_IN_CAD))
            .thenReturn(ADDITIONAL_FEES_TRANSACTION_PRICE_THRESHOLD_IN_CAD);
  }

  @Test
  public void givenATransactionWithQuantityUnderThreshold_whenCalculatingFees_thenPriceShouldBeRight() {
    when(this.currencyConverter.convertFromCAD(BASE_FEES_FOR_QUANTITY_UNDER_THRESHOLD_IN_CAD)).thenReturn(BASE_FEES_FOR_QUANTITY_UNDER_THRESHOLD_IN_CAD);
    when(this.currencyConverter.convertToCAD(BASE_FEES_FOR_QUANTITY_UNDER_THRESHOLD_IN_CAD)).thenReturn(BASE_FEES_FOR_QUANTITY_UNDER_THRESHOLD_IN_CAD);

    final Money expectedFees = RegularFeesCalculator.TRANSACTION_FEES_FOR_QUANTITY_LESS_THAN_THRESHOLD_IN_CAD.multiply(A_QUANTITY_LOWER_THAN_THRESHOLD);

    assertEquals(expectedFees, this.feesCalculator.calculateTransactionFees(A_STOCK_UNIT_PRICE_IN_CAD, A_QUANTITY_LOWER_THAN_THRESHOLD));
  }

  @Test
  public void givenATransactionWithQuantityOverThreshold_whenCalculatingFees_thenPriceShouldBeRight() {
    when(this.currencyConverter.convertFromCAD(BASE_FEES_FOR_QUANTITY_ABOVE_THRESHOLD_IN_CAD)).thenReturn(BASE_FEES_FOR_QUANTITY_ABOVE_THRESHOLD_IN_CAD);
    when(this.currencyConverter.convertToCAD(BASE_FEES_FOR_QUANTITY_ABOVE_THRESHOLD_IN_CAD)).thenReturn(BASE_FEES_FOR_QUANTITY_ABOVE_THRESHOLD_IN_CAD);

    final Money expectedFees = TRANSACTION_FEES_FOR_QUANTITY_MORE_THAN_THRESHOLD_IN_CAD.multiply(A_QUANTITY_ABOVE_THRESHOLD);

    assertEquals(expectedFees, this.feesCalculator.calculateTransactionFees(A_STOCK_UNIT_PRICE_IN_CAD, A_QUANTITY_ABOVE_THRESHOLD));
  }

  @Test
  public void givenATransactionWithTotalPriceOverAdditionalFeesThreshold_whenCalculatingFees_thenPriceShouldBeRight() {
    when(this.currencyConverter.convertFromCAD(BASE_FEES_FOR_QUANTITY_ABOVE_THRESHOLD_IN_CAD)).thenReturn(BASE_FEES_FOR_QUANTITY_ABOVE_THRESHOLD_IN_CAD);
    when(this.currencyConverter.convertToCAD(BASE_FEES_FOR_QUANTITY_ABOVE_THRESHOLD_IN_CAD)).thenReturn(BASE_FEES_FOR_QUANTITY_ABOVE_THRESHOLD_IN_CAD);
    final Money additionalFees = TRANSACTION_FEES_FOR_QUANTITY_MORE_THAN_THRESHOLD_IN_CAD.multiply(RegularFeesCalculator.ADDITIONAL_FEES_PRICE_PERCENTAGE);
    final Money expectedFees = (TRANSACTION_FEES_FOR_QUANTITY_MORE_THAN_THRESHOLD_IN_CAD.multiply(A_QUANTITY_ABOVE_THRESHOLD)).add(additionalFees);

    assertEquals(expectedFees.getNumberStripped().floatValue(), this.feesCalculator.calculateTransactionFees(A_STOCK_UNIT_PRICE_IN_CAD, A_QUANTITY_ABOVE_THRESHOLD).getNumberStripped().floatValue(), DELTA);
  }
}
