package ca.ulaval.glo4002.trading.transactions.controller;

import ca.ulaval.glo4002.trading.accounts.domain.exceptions.TransactionNotFoundException;
import ca.ulaval.glo4002.trading.common.controller.dto.errors.APIErrorDTO;
import ca.ulaval.glo4002.trading.common.controller.dto.errors.TransactionAPIErrorDTO;
import ca.ulaval.glo4002.trading.markets.domain.Market;
import ca.ulaval.glo4002.trading.stocks.domain.Stock;
import ca.ulaval.glo4002.trading.transactions.controller.exceptions.*;
import ca.ulaval.glo4002.trading.transactions.domain.exceptions.MarketClosedException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class TransactionAdviceTest {

  private static final Market A_MARKET = Market.NASDAQ;
  private static final Stock A_STOCK = new Stock(A_MARKET, "GOOG");
  private final UUID A_TRANSACTION_NUMBER = new UUID(23131, 12312);

  private TransactionAdvice transactionAdvice;

  @Before
  public void setUp() {
    this.transactionAdvice = new TransactionAdvice();
  }

  @Test
  public void givenAnInvalidTransactionNumberException_whenHandlingTheException_thenHttpResultIsBadRequest() {
    final InvalidTransactionNumberException exception = new InvalidTransactionNumberException(this.A_TRANSACTION_NUMBER);
    final ResponseEntity<List<TransactionAPIErrorDTO>> handlingResult = this.transactionAdvice.handleInvalidTransactionNumber(exception);

    assertEquals(HttpStatus.BAD_REQUEST, handlingResult.getStatusCode());
  }

  @Test
  public void givenAnInvalidTransactionNumberException_whenHandlingTheException_thenThereIsOnlyOneError() {
    final InvalidTransactionNumberException exception = new InvalidTransactionNumberException(this.A_TRANSACTION_NUMBER);
    final ResponseEntity<List<TransactionAPIErrorDTO>> handlingResult = this.transactionAdvice.handleInvalidTransactionNumber(exception);

    assertEquals(1, handlingResult.getBody().size());
  }

  @Test
  public void givenAnInvalidTransactionNumberException_whenHandlingTheException_thenTheErrorHasTheRightType() {
    final InvalidTransactionNumberException exception = new InvalidTransactionNumberException(this.A_TRANSACTION_NUMBER);
    final ResponseEntity<List<TransactionAPIErrorDTO>> handlingResult = this.transactionAdvice.handleInvalidTransactionNumber(exception);

    assertEquals(exception.getError(), this.getFirstTransactionError(handlingResult).getError());
  }

  @Test
  public void givenAnInvalidTransactionNumberException_whenHandlingTheException_thenTheDescriptionIsAsExpected() {
    final InvalidTransactionNumberException exception = new InvalidTransactionNumberException(this.A_TRANSACTION_NUMBER);
    final ResponseEntity<List<TransactionAPIErrorDTO>> handlingResult = this.transactionAdvice.handleInvalidTransactionNumber(exception);

    assertEquals(exception.getDescription(), this.getFirstTransactionError(handlingResult).getDescription());
  }

  @Test
  public void givenAnInvalidTransactionNumberException_whenHandlingTheException_thenTheTransactionNumberIsSet() {
    final InvalidTransactionNumberException exception = new InvalidTransactionNumberException(this.A_TRANSACTION_NUMBER);
    final ResponseEntity<List<TransactionAPIErrorDTO>> handlingResult = this.transactionAdvice.handleInvalidTransactionNumber(exception);

    assertEquals(this.A_TRANSACTION_NUMBER, this.getFirstTransactionError(handlingResult).getTransactionNumber());
  }

  @Test
  public void givenATransactionNotFoundException_whenHandlingTheException_thenTheErrorHasTheRightType() {
    final TransactionNotFoundException exception = new TransactionNotFoundException(this.A_TRANSACTION_NUMBER);
    final ResponseEntity<List<APIErrorDTO>> handlingResult = this.transactionAdvice.handleTransactionNotFound(exception);

    assertEquals(exception.getError(), this.getFirstApiError(handlingResult).getError());
  }

  @Test
  public void givenATransactionNotFoundException_whenHandlingTheException_thenTheDescriptionIsAsExpected() {
    final TransactionNotFoundException exception = new TransactionNotFoundException(this.A_TRANSACTION_NUMBER);
    final ResponseEntity<List<APIErrorDTO>> handlingResult = this.transactionAdvice.handleTransactionNotFound(exception);

    assertEquals(exception.getDescription(), this.getFirstApiError(handlingResult).getDescription());
  }

  @Test
  public void givenANotEnoughCreditsException_whenHandlingTheException_thenTheErrorHasTheRightType() {
    final NotEnoughCreditsException exception = new NotEnoughCreditsException(this.A_TRANSACTION_NUMBER);
    final ResponseEntity<List<TransactionAPIErrorDTO>> handlingResult = this.transactionAdvice.handleNotEnoughCredits(exception);

    assertEquals(exception.getError(), this.getFirstTransactionError(handlingResult).getError());
  }

  @Test
  public void givenANotEnoughCreditsException_whenHandlingTheException_thenTheDescriptionIsAsExpected() {
    final NotEnoughCreditsException exception = new NotEnoughCreditsException(this.A_TRANSACTION_NUMBER);
    final ResponseEntity<List<TransactionAPIErrorDTO>> handlingResult = this.transactionAdvice.handleNotEnoughCredits(exception);

    assertEquals(exception.getDescription(), this.getFirstTransactionError(handlingResult).getDescription());
  }

  @Test
  public void givenAnInvalidDateException_whenHandlingTheException_thenTheErrorHasTheRightType() {
    final InvalidDateException exception = new InvalidDateException();
    final ResponseEntity<List<APIErrorDTO>> handlingResult = this.transactionAdvice.handleInvalidDate(exception);

    assertEquals(exception.getError(), this.getFirstApiError(handlingResult).getError());
  }

  @Test
  public void givenAnInvalidDateException_whenHandlingTheException_thenTheDescriptionIsAsExpected() {
    final InvalidDateException exception = new InvalidDateException();
    final ResponseEntity<List<APIErrorDTO>> handlingResult = this.transactionAdvice.handleInvalidDate(exception);

    assertEquals(exception.getDescription(), this.getFirstApiError(handlingResult).getDescription());
  }

  @Test
  public void givenANotEnoughStockException_whenHandlingTheException_thenTheErrorHasTheRightType() {
    final NotEnoughStockException exception = new NotEnoughStockException(this.A_TRANSACTION_NUMBER, A_STOCK);
    final ResponseEntity<List<TransactionAPIErrorDTO>> handlingResult = this.transactionAdvice.handleNotEnoughStock(exception);

    assertEquals(exception.getError(), this.getFirstTransactionError(handlingResult).getError());
  }

  @Test
  public void givenANotEnoughStockException_whenHandlingTheException_thenTheDescriptionIsAsExpected() {
    final NotEnoughStockException exception = new NotEnoughStockException(this.A_TRANSACTION_NUMBER, A_STOCK);
    final ResponseEntity<List<TransactionAPIErrorDTO>> handlingResult = this.transactionAdvice.handleNotEnoughStock(exception);

    assertEquals(exception.getDescription(), this.getFirstTransactionError(handlingResult).getDescription());
  }

  @Test
  public void givenAStockNotFoundException_whenHandlingTheException_thenTheErrorHasTheRightType() {
    final StockNotFoundException exception = new StockNotFoundException(A_STOCK);
    final ResponseEntity<List<APIErrorDTO>> handlingResult = this.transactionAdvice.handleStockNotFound(exception);

    assertEquals(exception.getError(), this.getFirstApiError(handlingResult).getError());
  }

  @Test
  public void givenAStockNotFoundException_whenHandlingTheException_thenTheDescriptionIsAsExpected() {
    final StockNotFoundException exception = new StockNotFoundException(A_STOCK);
    final ResponseEntity<List<APIErrorDTO>> handlingResult = this.transactionAdvice.handleStockNotFound(exception);

    assertEquals(exception.getDescription(), this.getFirstApiError(handlingResult).getDescription());
  }

  @Test
  public void givenAStockParametersDontMatchException_whenHandlingTheException_thenHttpResultIsBadRequest() {
    final StockParametersDontMatchException exception = new StockParametersDontMatchException(this.A_TRANSACTION_NUMBER);
    final ResponseEntity<List<TransactionAPIErrorDTO>> handlingResult = this.transactionAdvice.handleStockParametersDontMatch(exception);

    assertEquals(HttpStatus.BAD_REQUEST, handlingResult.getStatusCode());
  }

  @Test
  public void givenAStockParametersDontMatchException_whenHandlingTheException_thenThereIsOnlyOneError() {
    final StockParametersDontMatchException exception = new StockParametersDontMatchException(this.A_TRANSACTION_NUMBER);
    final ResponseEntity<List<TransactionAPIErrorDTO>> handlingResult = this.transactionAdvice.handleStockParametersDontMatch(exception);

    assertEquals(1, handlingResult.getBody().size());
  }

  @Test
  public void givenAStockParametersDontMatchException_whenHandlingTheException_thenTheErrorHasTheRightType() {
    final StockParametersDontMatchException exception = new StockParametersDontMatchException(this.A_TRANSACTION_NUMBER);
    final ResponseEntity<List<TransactionAPIErrorDTO>> handlingResult = this.transactionAdvice.handleStockParametersDontMatch(exception);

    assertEquals(exception.getError(), this.getFirstTransactionError(handlingResult).getError());
  }

  @Test
  public void givenAStockParametersDontMatchException_whenHandlingTheException_thenTheDescriptionIsAsExpected() {
    final StockParametersDontMatchException exception = new StockParametersDontMatchException(this.A_TRANSACTION_NUMBER);
    final ResponseEntity<List<TransactionAPIErrorDTO>> handlingResult = this.transactionAdvice.handleStockParametersDontMatch(exception);

    assertEquals(exception.getDescription(), this.getFirstTransactionError(handlingResult).getDescription());
  }

  @Test
  public void whenHandlingAMarketClosedException_thenHttpResultIsBadRequest() {
    final MarketClosedException exception = new MarketClosedException(A_MARKET, this.A_TRANSACTION_NUMBER);
    final ResponseEntity<List<TransactionAPIErrorDTO>> handlingResult = this.transactionAdvice.handleMarketClosed(exception);

    assertEquals(HttpStatus.BAD_REQUEST, handlingResult.getStatusCode());
  }

  @Test
  public void whenHandlingAMarketClosedException_thenThereIsOnlyOneError() {
    final MarketClosedException exception = new MarketClosedException(A_MARKET, this.A_TRANSACTION_NUMBER);
    final ResponseEntity<List<TransactionAPIErrorDTO>> handlingResult = this.transactionAdvice.handleMarketClosed(exception);

    assertEquals(1, handlingResult.getBody().size());
  }

  @Test
  public void whenHandlingAMarketClosedException_thenTheErrorHasTheRightType() {
    final MarketClosedException exception = new MarketClosedException(A_MARKET, this.A_TRANSACTION_NUMBER);
    final ResponseEntity<List<TransactionAPIErrorDTO>> handlingResult = this.transactionAdvice.handleMarketClosed(exception);

    assertEquals(exception.getError(), this.getFirstTransactionError(handlingResult).getError());
  }

  @Test
  public void whenHandlingAMarketClosedException_thenTheDescriptionIsAsExpected() {
    final MarketClosedException exception = new MarketClosedException(A_MARKET, this.A_TRANSACTION_NUMBER);
    final ResponseEntity<List<TransactionAPIErrorDTO>> handlingResult = this.transactionAdvice.handleMarketClosed(exception);

    assertEquals(exception.getDescription(), this.getFirstTransactionError(handlingResult).getDescription());
  }

  @Test
  public void whenHandlingANotEnoughCreditsForFeesException_thenHttpResultIsBadRequest() {
    final NotEnoughCreditsForFeesException exception = new NotEnoughCreditsForFeesException(this.A_TRANSACTION_NUMBER);
    final ResponseEntity<List<TransactionAPIErrorDTO>> handlingResult = this.transactionAdvice.handleNotEnoughCreditsForFeesException(exception);

    assertEquals(HttpStatus.BAD_REQUEST, handlingResult.getStatusCode());
  }

  @Test
  public void whenHandlingANotEnoughCreditsForFeesException_thenThereIsOnlyOneError() {
    final NotEnoughCreditsForFeesException exception = new NotEnoughCreditsForFeesException(this.A_TRANSACTION_NUMBER);
    final ResponseEntity<List<TransactionAPIErrorDTO>> handlingResult = this.transactionAdvice.handleNotEnoughCreditsForFeesException(exception);

    assertEquals(1, handlingResult.getBody().size());
  }

  @Test
  public void whenHandlingANotEnoughCreditsForFeesException_thenTheErrorHasTheRightType() {
    final NotEnoughCreditsForFeesException exception = new NotEnoughCreditsForFeesException(this.A_TRANSACTION_NUMBER);
    final ResponseEntity<List<TransactionAPIErrorDTO>> handlingResult = this.transactionAdvice.handleNotEnoughCreditsForFeesException(exception);

    assertEquals(exception.getError(), this.getFirstTransactionError(handlingResult).getError());
  }

  @Test
  public void whenHandlingANotEnoughCreditsForFeesException_thenTheDescriptionIsAsExpected() {
    final NotEnoughCreditsForFeesException exception = new NotEnoughCreditsForFeesException(this.A_TRANSACTION_NUMBER);
    final ResponseEntity<List<TransactionAPIErrorDTO>> handlingResult = this.transactionAdvice.handleNotEnoughCreditsForFeesException(exception);

    assertEquals(exception.getDescription(), this.getFirstTransactionError(handlingResult).getDescription());
  }

  private APIErrorDTO getFirstApiError(final ResponseEntity<List<APIErrorDTO>> handlingResult) {
    return handlingResult.getBody().get(0);
  }

  private TransactionAPIErrorDTO getFirstTransactionError(final ResponseEntity<List<TransactionAPIErrorDTO>> handlingResult) {
    return handlingResult.getBody().get(0);
  }
}
