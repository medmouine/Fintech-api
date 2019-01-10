package ca.ulaval.glo4002.trading.common.infrastructure.providers;

import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Component
public class DateTimeProvider {
  public ZoneOffset getCurrentZoneOffset() {
    return OffsetDateTime.now().getOffset();
  }
}
