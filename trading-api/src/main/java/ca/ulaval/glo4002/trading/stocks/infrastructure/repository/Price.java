package ca.ulaval.glo4002.trading.stocks.infrastructure.repository;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@AllArgsConstructor
@Data
class Price {
  private Instant date;
  private float price;

  public boolean isOnDay(final Instant date) {
    if (date.isBefore(this.date)) {
      return false;
    }
    final long daysBetween = ChronoUnit.DAYS.between(this.date, date);
    return daysBetween < 1;
  }
}
