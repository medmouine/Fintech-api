package ca.ulaval.glo4002.trading.transactions.controller;

import ca.ulaval.glo4002.trading.accounts.domain.exceptions.TransactionNotFoundException;
import ca.ulaval.glo4002.trading.common.controller.TradingAdvice;
import ca.ulaval.glo4002.trading.common.controller.dto.errors.APIErrorDTO;
import ca.ulaval.glo4002.trading.common.controller.dto.errors.TransactionAPIErrorDTO;
import ca.ulaval.glo4002.trading.transactions.common.exceptions.TransactionException;
import ca.ulaval.glo4002.trading.transactions.controller.exceptions.*;
import ca.ulaval.glo4002.trading.transactions.domain.exceptions.MarketClosedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.List;

@ControllerAdvice
public class TransactionAdvice extends TradingAdvice {

  private ResponseEntity<List<TransactionAPIErrorDTO>> handleTransactionException(final TransactionException ex, final HttpStatus status) {
    final TransactionAPIErrorDTO transactionAPIErrorDTO = new TransactionAPIErrorDTO(ex.getError(), ex.getDescription(), ex.getTransactionNumber());
    final List<TransactionAPIErrorDTO> transactionAPIErrorDTOList = Collections.singletonList(transactionAPIErrorDTO);
    return new ResponseEntity<>(transactionAPIErrorDTOList, status);
  }

  @ExceptionHandler(NotEnoughCreditsForFeesException.class)
  protected ResponseEntity<List<TransactionAPIErrorDTO>> handleNotEnoughCreditsForFeesException(NotEnoughCreditsForFeesException ex) {
    return this.handleTransactionException(ex, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MarketClosedException.class)
  protected ResponseEntity<List<TransactionAPIErrorDTO>> handleMarketClosed(final MarketClosedException ex) {
    return this.handleTransactionException(ex, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(InvalidTransactionNumberException.class)
  protected ResponseEntity<List<TransactionAPIErrorDTO>> handleInvalidTransactionNumber(final InvalidTransactionNumberException ex) {
    return this.handleTransactionException(ex, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(TransactionNotFoundException.class)
  protected ResponseEntity<List<APIErrorDTO>> handleTransactionNotFound(final TransactionNotFoundException ex) {
    return this.handleTradingException(ex, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(NotEnoughCreditsException.class)
  protected ResponseEntity<List<TransactionAPIErrorDTO>> handleNotEnoughCredits(final NotEnoughCreditsException ex) {
    return this.handleTransactionException(ex, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(NotEnoughStockException.class)
  protected ResponseEntity<List<TransactionAPIErrorDTO>> handleNotEnoughStock(final NotEnoughStockException ex) {
    return this.handleTransactionException(ex, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(InvalidDateException.class)
  protected ResponseEntity<List<APIErrorDTO>> handleInvalidDate(final InvalidDateException ex) {
    return this.handleTradingException(ex, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(StockNotFoundException.class)
  public ResponseEntity<List<APIErrorDTO>> handleStockNotFound(final StockNotFoundException ex) {
    return this.handleTradingException(ex, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(StockParametersDontMatchException.class)
  public ResponseEntity<List<TransactionAPIErrorDTO>> handleStockParametersDontMatch(final StockParametersDontMatchException ex) {
    return this.handleTransactionException(ex, HttpStatus.BAD_REQUEST);
  }
}
