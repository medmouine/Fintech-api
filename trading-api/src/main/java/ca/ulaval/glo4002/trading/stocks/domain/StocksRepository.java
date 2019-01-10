package ca.ulaval.glo4002.trading.stocks.domain;

import org.javamoney.moneta.Money;

import java.time.Instant;

public interface StocksRepository {
  Money findPriceBy(Stock stock, Instant date);
}
