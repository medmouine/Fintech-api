package ca.ulaval.glo4002.trading.markets;

import ca.ulaval.glo4002.trading.common.domain.TimeInterval;
import org.junit.Test;

import java.time.LocalTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class TimeIntervalTest {

  @Test
  public void whenCreatingATimeInterval_thenIntervalStartIsParsed() {
    final TimeInterval timeInterval = TimeInterval.of("11:29-12:08");

    assertEquals(timeInterval.getIntervalStart().toString(), "11:29");
  }

  @Test
  public void whenCreatingATimeInterval_thenIntervalEndIsParsed() {
    final TimeInterval timeInterval = TimeInterval.of("11:29-12:08");

    assertEquals(timeInterval.getIntervalEnd().toString(), "12:08");
  }

  @Test
  public void givenATimeInterval_whenTimeIsNotInTheInterval_thenShouldReturnFalse() {
    final TimeInterval timeInterval = TimeInterval.of("11:14-12:03");

    assertFalse(timeInterval.isInInterval(LocalTime.parse("12:30")));
  }
}