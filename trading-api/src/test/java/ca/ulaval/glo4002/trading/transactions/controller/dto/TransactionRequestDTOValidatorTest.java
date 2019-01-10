package ca.ulaval.glo4002.trading.transactions.controller.dto;

import ca.ulaval.glo4002.trading.common.controller.dto.errors.APIErrorDTO;
import ca.ulaval.glo4002.trading.stocks.controller.dto.StockDTO;
import ca.ulaval.glo4002.trading.transactions.controller.dto.errors.InvalidQuantityError;
import ca.ulaval.glo4002.trading.transactions.controller.dto.errors.UnsupportedTransactionTypeError;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.Instant;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class TransactionRequestDTOValidatorTest {
  private  static final Long VALID_TRANSACTION_QUANTITY = 10L;
  private  static final Long INVALID_TRANSACTION_QUANTITY = -1L;
  private  static final String INVALID_TRANSACTION_TYPE = "INVALID_TRANSACTION_TYPE";
  private static final String A_MARKET_SYMBOL = "NASDAQ";
  private static final String A_MARKET_NAME = "marketName";
  private static final StockDTO STOCK_DTO = new StockDTO(A_MARKET_NAME, A_MARKET_SYMBOL);

  @InjectMocks
  private TransactionRequestDTOValidator validator;

  @Test
  public void givenAValidTransactionRequestDTO_thenItShouldReturnNoError() {
    final TransactionRequestDTO transactionRequestDTO = new TransactionRequestDTO(TransactionType.BUY, Instant.now(), STOCK_DTO, null, VALID_TRANSACTION_QUANTITY);
    assertEquals(0, this.validator.findErrors(transactionRequestDTO).size());
  }

  @Test
  public void givenATransactionRequestDTOWithAnInvalidTransactionType_thenThereShouldBeAnInvalidTransactionTypeError() {
    final APIErrorDTO expectedError = new UnsupportedTransactionTypeError(INVALID_TRANSACTION_TYPE);
    final TransactionRequestDTO transactionRequestDTO = new TransactionRequestDTO(INVALID_TRANSACTION_TYPE, Instant.now(), STOCK_DTO, null, VALID_TRANSACTION_QUANTITY);

    assertTrue(this.validator.findErrors(transactionRequestDTO).contains(expectedError));
  }

  @Test
  public void givenATransactionRequestDTOWithAnInvalidStockQuantity_thenThereShouldBeAnInvalidStocksQuantityError() {
    final TransactionRequestDTO transactionRequestDTO = new TransactionRequestDTO(TransactionType.BUY, Instant.now(), STOCK_DTO, null, INVALID_TRANSACTION_QUANTITY);
    final APIErrorDTO expectedError = new InvalidQuantityError();

    assertTrue(this.validator.findErrors(transactionRequestDTO).contains(expectedError));
  }
}
