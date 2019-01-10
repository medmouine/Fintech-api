package ca.ulaval.glo4002.trading.markets.domain.response;

import ca.ulaval.glo4002.trading.common.domain.TimeInterval;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MarketDetailsResponse implements Serializable {
  public static final String GMT = "GMT";
  public static final String UTC = "UTC";
  private static final long serialVersionUID = 5410381125753738826L;
  private final List<TimeInterval> openHours;
  private TimeZone timezone;

  public MarketDetailsResponse() {
    this.openHours = new ArrayList<>();
  }

  public static TimeZone parseTimeZone(final String timeZone) {
    return TimeZone.getTimeZone(timeZone.replace(UTC, GMT));
  }

  public static MarketDetailsResponse of(final String timeZone, final List<String> openHours) {
    final MarketDetailsResponse marketDetailsResponse = new MarketDetailsResponse();
    marketDetailsResponse.setTimezone(parseTimeZone(timeZone));
    marketDetailsResponse.setOpenHours(openHours);
    return marketDetailsResponse;
  }

  public void setTimezone(final TimeZone timezone) {
    this.timezone = timezone;
  }

  public boolean isOpenAtDate(final Instant date) {
    if (this.isOnWeekend(date)) {
      return false;
    }
    return this.openHours.stream()
            .anyMatch(openHour -> openHour.isInInterval(LocalTime.from(date.atZone(this.timezone.toZoneId()))));
  }

  private boolean isOnWeekend(final Instant date) {
    final LocalDate localDate = LocalDate.from(date.atZone(this.timezone.toZoneId()));
    return localDate.getDayOfWeek() == DayOfWeek.SATURDAY || localDate.getDayOfWeek() == DayOfWeek.SUNDAY;
  }

  public void setTimezone(final String timezone) {
    this.timezone = TimeZone.getTimeZone(timezone.replace(UTC, GMT));
  }

  public void setOpenHours(final List<String> timeIntervalStrings) {
    for (final String timeIntervalString : timeIntervalStrings) {
      final TimeInterval timeInterval = TimeInterval.of(timeIntervalString);
      this.openHours.add(timeInterval);
    }
  }

}
