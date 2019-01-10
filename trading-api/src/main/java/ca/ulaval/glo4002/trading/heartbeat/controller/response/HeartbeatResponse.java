package ca.ulaval.glo4002.trading.heartbeat.controller.response;

import java.time.OffsetDateTime;

public class HeartbeatResponse {
  public final String token;
  public final OffsetDateTime time;

  public HeartbeatResponse(final String token) {
    this.token = token;
    this.time = OffsetDateTime.now();
  }
}
