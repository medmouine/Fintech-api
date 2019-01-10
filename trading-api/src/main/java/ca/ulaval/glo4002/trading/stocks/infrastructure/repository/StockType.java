package ca.ulaval.glo4002.trading.stocks.infrastructure.repository;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

enum StockType {
  COMMON("common"), PREFERRED("preferred");

  private final String stringValue;

  StockType(final String type) {
    this.stringValue = type;
  }

  public static StockType fromString(final String type) {
    return Arrays.stream(StockType.values())
            .filter(stockType -> stockType.stringValue.equalsIgnoreCase(type))
            .findFirst()
            .orElse(null);
  }

  @JsonValue
  public String getStringValue() {
    return this.stringValue;
  }
}
