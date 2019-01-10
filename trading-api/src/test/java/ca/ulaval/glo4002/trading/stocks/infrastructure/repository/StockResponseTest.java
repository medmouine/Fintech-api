package ca.ulaval.glo4002.trading.stocks.infrastructure.repository;

import ca.ulaval.glo4002.trading.transactions.controller.exceptions.InvalidDateException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StockResponseTest {

  private static final String MARKET_NAME = "NASDAQ";
  private static final String SYMBOL_NAME = "";
  private static final StockType STOCK_TYPE = StockType.COMMON;
  private static final Instant A_DATE = Instant.parse("2018-09-10T00:00:00.000Z");
  private static final float PRICE_VALUE = 42f;


  @Mock
  private Price firstPrice;
  @Mock
  private Price secondPrice;

  private StockResponse stockResponse;

  @Before
  public void setUp() {
    this.stockResponse = new StockResponse();
    this.stockResponse.setMarket(MARKET_NAME);
    this.stockResponse.setSymbol(SYMBOL_NAME);
    this.stockResponse.setType(STOCK_TYPE);
    this.stockResponse.setPrices(Collections.unmodifiableList(Arrays.asList(this.firstPrice, this.secondPrice)));
  }

  @Test
  public void whenGettingAPriceAtADate_thenThePriceForTheRightDayIsReturned() {
    when(this.firstPrice.isOnDay(any())).thenReturn(true);
    when(this.firstPrice.getPrice()).thenReturn(PRICE_VALUE);

    final float actualPrice = this.stockResponse.getPriceAtDate(A_DATE);

    assertEquals(PRICE_VALUE, actualPrice);
  }

  @Test(expected = InvalidDateException.class)
  public void whenGettingPriceFromADateWithNoPrice_thenInvalidDateExceptionIsThrown() {
    when(this.firstPrice.isOnDay(any())).thenReturn(false);
    when(this.secondPrice.isOnDay(any())).thenReturn(false);

    this.stockResponse.getPriceAtDate(A_DATE);
  }
}