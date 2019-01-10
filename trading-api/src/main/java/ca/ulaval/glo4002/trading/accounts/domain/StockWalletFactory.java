package ca.ulaval.glo4002.trading.accounts.domain;

import ca.ulaval.glo4002.trading.common.infrastructure.currencyConverter.CurrencyConverterFactory;
import ca.ulaval.glo4002.trading.transactions.domain.TransactionRepository;
import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StockWalletFactory {

  private final TransactionRepository transactionRepository;
  private final CurrencyConverterFactory currencyConverterFactory;

  @Autowired
  public StockWalletFactory(final TransactionRepository transactionRepository, final CurrencyConverterFactory currencyConverterFactory) {
    this.transactionRepository = transactionRepository;
    this.currencyConverterFactory = currencyConverterFactory;
  }

  public StockWallet create(final List<Money> credits) {
    return new StockWallet(credits, this.transactionRepository, this.currencyConverterFactory);
  }
}
