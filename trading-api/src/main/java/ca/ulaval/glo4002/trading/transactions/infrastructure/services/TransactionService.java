package ca.ulaval.glo4002.trading.transactions.infrastructure.services;

import ca.ulaval.glo4002.trading.accounts.domain.Account;
import ca.ulaval.glo4002.trading.accounts.domain.AccountNumber;
import ca.ulaval.glo4002.trading.accounts.infrastructure.services.AccountService;
import ca.ulaval.glo4002.trading.transactions.domain.Transaction;
import ca.ulaval.glo4002.trading.transactions.domain.TransactionFactory;
import ca.ulaval.glo4002.trading.transactions.domain.TransactionRepository;
import ca.ulaval.glo4002.trading.transactions.domain.TransactionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TransactionService {

  private final AccountService accountService;
  private final TransactionRepository transactionRepository;
  private final TransactionFactory transactionFactory;

  @Autowired
  public TransactionService(
          final TransactionRepository transactionRepository,
          final AccountService accountService,
          final TransactionFactory transactionFactory) {
    this.transactionRepository = transactionRepository;
    this.accountService = accountService;
    this.transactionFactory = transactionFactory;
  }

  public UUID makeTransaction(final AccountNumber accountNumber, final TransactionRequest transactionRequest) {
    final Account account = this.accountService.findByAccountNumber(accountNumber);
    final Transaction transaction = this.transactionFactory.create(accountNumber, transactionRequest);

    account.performTransaction(transaction);
    this.transactionRepository.save(transaction);
    this.accountService.save(account);
    return transaction.getTransactionNumber();
  }

  public Transaction getTransaction(final UUID transactionNumber) {
    return this.transactionRepository.findByTransactionNumber(transactionNumber);
  }
}
