package ca.ulaval.glo4002.trading.markets.domain;

import lombok.Getter;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

public enum Market {
  NASDAQ(Monetary.getCurrency("USD")),
  XSWX(Monetary.getCurrency("CHF")),
  XTKS(Monetary.getCurrency("JPY")),
  NYSE(Monetary.getCurrency("USD"));

  @Getter
  private final CurrencyUnit currencyUnit;

  public String getCurrencyCode(){
    return this.getCurrencyUnit().getCurrencyCode();
  }

  Market(final CurrencyUnit currency) {
    this.currencyUnit = currency;
  }
}
