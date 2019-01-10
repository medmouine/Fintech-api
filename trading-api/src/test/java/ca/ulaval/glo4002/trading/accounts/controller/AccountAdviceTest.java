package ca.ulaval.glo4002.trading.accounts.controller;

import ca.ulaval.glo4002.trading.accounts.controller.exceptions.AccountAlreadyOpenException;
import ca.ulaval.glo4002.trading.accounts.controller.exceptions.AccountNotFoundException;
import ca.ulaval.glo4002.trading.accounts.domain.AccountNumber;
import ca.ulaval.glo4002.trading.accounts.domain.InvestorId;
import ca.ulaval.glo4002.trading.common.controller.dto.errors.APIErrorDTO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RunWith(JUnit4.class)
public class AccountAdviceTest {

  private static final AccountNumber ACCOUNT_NUMBER = new AccountNumber("123");
  private static final InvestorId INVESTOR_ID = new InvestorId(123L);
  private AccountAdvice accountAdvice;

  @Before
  public void setUp() {
    this.accountAdvice = new AccountAdvice();
  }

  @Test
  public void givenAnAccountNotFoundException_whenHandlingTheException_thenHttpResultIsNotFound() {
    final ResponseEntity<List<APIErrorDTO>> handlingResult = this.accountAdvice.handleAccountNotFound(this.createAccountNotFoundException());

    Assert.assertEquals(HttpStatus.NOT_FOUND, handlingResult.getStatusCode());
  }

  @Test
  public void givenAnAccountNotFoundException_whenHandlingTheException_thenThereIsOnlyOneError() {
    final ResponseEntity<List<APIErrorDTO>> handlingResult = this.accountAdvice.handleAccountNotFound(this.createAccountNotFoundException());

    Assert.assertEquals(1, handlingResult.getBody().size());
  }

  @Test
  public void givenAnAccountNotFoundException_whenHandlingTheException_thenTheErrorHasTheRightType() {
    AccountNotFoundException exception = this.createAccountNotFoundException();
    final ResponseEntity<List<APIErrorDTO>> handlingResult = this.accountAdvice.handleAccountNotFound(exception);

    Assert.assertEquals(exception.getError(), this.getFirstError(handlingResult).getError());
  }

  @Test
  public void givenAnAccountNotFoundException_whenHandlingTheException_thenTheDescriptionIsAsExpected() {
    AccountNotFoundException exception = this.createAccountNotFoundException();
    final ResponseEntity<List<APIErrorDTO>> handlingResult = this.accountAdvice.handleAccountNotFound(exception);

    Assert.assertEquals(exception.getDescription(), this.getFirstError(handlingResult).getDescription());
  }

  @Test
  public void givenAnAccountAlreadyOpenException_whenHandlingTheException_thenTheHttpStatusIsBadRequest() {
    final ResponseEntity<List<APIErrorDTO>> handlingResult = this.accountAdvice.handleAccountAlreadyOpen(this.createAccountAlreadyOpenException());

    Assert.assertEquals(HttpStatus.BAD_REQUEST, handlingResult.getStatusCode());
  }


  @Test
  public void givenAnAccountAlreadyOpenException_whenHandlingTheException_thenThereIsOnlyOneError() {
    final ResponseEntity<List<APIErrorDTO>> handlingResult = this.accountAdvice.handleAccountAlreadyOpen(this.createAccountAlreadyOpenException());

    Assert.assertEquals(1, handlingResult.getBody().size());
  }

  @Test
  public void givenAnAccountAlreadyOpenException_whenHandlingTheException_thenTheErrorHasTheRightType() {
    AccountAlreadyOpenException exception = this.createAccountAlreadyOpenException();
    final ResponseEntity<List<APIErrorDTO>> handlingResult = this.accountAdvice.handleAccountAlreadyOpen(exception);

    Assert.assertEquals(exception.getError(), this.getFirstError(handlingResult).getError());
  }

  @Test
  public void givenAnAccountAlreadyOpenException_whenHandlingTheException_thenTheDescriptionIsAsExpected() {
    AccountAlreadyOpenException exception = this.createAccountAlreadyOpenException();
    final ResponseEntity<List<APIErrorDTO>> handlingResult = this.accountAdvice.handleAccountAlreadyOpen(exception);

    Assert.assertEquals(exception.getDescription(), this.getFirstError(handlingResult).getDescription());
  }

  private APIErrorDTO getFirstError(final ResponseEntity<List<APIErrorDTO>> handlingResult) {
    return handlingResult.getBody().get(0);
  }

  private AccountNotFoundException createAccountNotFoundException() {
    return new AccountNotFoundException(ACCOUNT_NUMBER);
  }

  private AccountAlreadyOpenException createAccountAlreadyOpenException() {
    return new AccountAlreadyOpenException(INVESTOR_ID);
  }
}
