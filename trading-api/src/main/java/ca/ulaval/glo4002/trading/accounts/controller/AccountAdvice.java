package ca.ulaval.glo4002.trading.accounts.controller;

import ca.ulaval.glo4002.trading.accounts.controller.exceptions.AccountAlreadyOpenException;
import ca.ulaval.glo4002.trading.accounts.controller.exceptions.AccountNotFoundException;
import ca.ulaval.glo4002.trading.common.controller.TradingAdvice;
import ca.ulaval.glo4002.trading.common.controller.dto.errors.APIErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class AccountAdvice extends TradingAdvice {

  @ExceptionHandler(AccountNotFoundException.class)
  protected ResponseEntity<List<APIErrorDTO>> handleAccountNotFound(final AccountNotFoundException ex) {
    return handleTradingException(ex, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(AccountAlreadyOpenException.class)
  protected ResponseEntity<List<APIErrorDTO>> handleAccountAlreadyOpen(final AccountAlreadyOpenException ex) {
    return handleTradingException(ex, HttpStatus.BAD_REQUEST);
  }
}
