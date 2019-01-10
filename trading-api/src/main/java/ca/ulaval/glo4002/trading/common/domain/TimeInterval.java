package ca.ulaval.glo4002.trading.common.domain;

import lombok.Getter;

import java.time.LocalTime;

@Getter
public class TimeInterval {
  private final LocalTime intervalStart;
  private final LocalTime intervalEnd;

  public TimeInterval(final LocalTime intervalStart, final LocalTime intervalEnd) {
    this.intervalStart = intervalStart;
    this.intervalEnd = intervalEnd;
  }

  public static TimeInterval of(final String timeInterval) {
    final String[] timeStrings = timeInterval.split("-");

    final LocalTime intervalStart = parseTimeString(timeStrings[0]);
    final LocalTime intervalEnd = parseTimeString(timeStrings[1]);

    return new TimeInterval(intervalStart, intervalEnd);
  }

  private static LocalTime parseTimeString(final String timeString) {
    final String formattedTimeString = String.format("%5s", timeString).replace(' ', '0');
    return LocalTime.parse(formattedTimeString);
  }

  public boolean isInInterval(final LocalTime time) {
    return time.isAfter(this.intervalStart) && time.isBefore(this.intervalEnd);
  }
}
