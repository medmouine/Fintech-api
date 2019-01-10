package ca.ulaval.glo4002.trading.transactions.domain;

import ca.ulaval.glo4002.trading.accounts.domain.AccountNumber;
import ca.ulaval.glo4002.trading.accounts.domain.exceptions.TransactionNotFoundException;
import ca.ulaval.glo4002.trading.transactions.controller.exceptions.InvalidTransactionNumberException;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface TransactionRepository {

  void save(Transaction transaction);

  Transaction findByTransactionNumber(UUID transactionNumber) throws TransactionNotFoundException;

  List<Transaction> findAllTransactionForAccountInTimeRange(AccountNumber accountNumber, final Instant startDate, final Instant endDate);

  Long getStockQuantityLeftForTransaction(UUID transactionNumber) throws InvalidTransactionNumberException;
}
