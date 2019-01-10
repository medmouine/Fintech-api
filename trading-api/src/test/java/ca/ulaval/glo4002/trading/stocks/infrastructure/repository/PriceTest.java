package ca.ulaval.glo4002.trading.stocks.infrastructure.repository;

import org.junit.Before;
import org.junit.Test;

import java.time.Instant;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PriceTest {

  private static final Instant DATE_BEFORE = Instant.parse("2018-09-01T00:00:00.000Z");
  private static final Instant A_DATE = Instant.parse("2018-09-05T00:00:00.000Z");
  private static final Instant DATE_AFTER_ALMOST_NEXT_DAY = Instant.parse("2018-09-05T23:59:59.999Z");
  private static final Instant DATE_AFTER_ALMOST_SAME_DAY = Instant.parse("2018-09-06T00:00:00.000Z");
  private static final Instant DATE_AFTER_SAME_DAY = Instant.parse("2018-09-05T12:00:00.000Z");
  private static final Instant DATE_AFTER_OTHER_DAY = Instant.parse("2018-09-10T00:00:00.000Z");
  public static final String CAD = "CAD";
  private static final float PRICE_VALUE = 42f;
  private Price price;

  @Before
  public void setUp() {
    this.price = new Price(A_DATE, PRICE_VALUE);
  }

  @Test
  public void whenCheckingADateBefore_thenPriceIsNotAtDate() {
    assertFalse(this.price.isOnDay(DATE_BEFORE));
  }

  @Test
  public void whenCheckingADateAfterOnTheSameDay_thenPriceIsAtDate() {
    assertTrue(this.price.isOnDay(DATE_AFTER_SAME_DAY));
  }

  @Test
  public void whenCheckingADateAfterButFurtherThanADay_thenPriceIsNotAtDate() {
    assertFalse(this.price.isOnDay(DATE_AFTER_OTHER_DAY));
  }

  @Test
  public void whenCheckingADateAfterButAlmostOnTheSameDay_thenPriceIsNotAtDate() {
    assertFalse(this.price.isOnDay(DATE_AFTER_ALMOST_SAME_DAY));
  }

  @Test
  public void whenCheckingADateAfterAlmostNextDay_thenPriceIsAtDate() {
    assertTrue(this.price.isOnDay(DATE_AFTER_ALMOST_NEXT_DAY));
  }

}