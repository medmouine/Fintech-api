package ca.ulaval.glo4002.trading.transactions.controller;

import ca.ulaval.glo4002.trading.accounts.controller.assembler.AccountNumberAssembler;
import ca.ulaval.glo4002.trading.accounts.domain.AccountNumber;
import ca.ulaval.glo4002.trading.common.controller.dto.Validator;
import ca.ulaval.glo4002.trading.common.controller.dto.errors.APIErrorDTO;
import ca.ulaval.glo4002.trading.transactions.controller.assembler.TransactionAssembler;
import ca.ulaval.glo4002.trading.transactions.controller.assembler.TransactionRequestAssembler;
import ca.ulaval.glo4002.trading.transactions.controller.dto.TransactionRequestDTO;
import ca.ulaval.glo4002.trading.transactions.controller.response.TransactionResponse;
import ca.ulaval.glo4002.trading.transactions.domain.Transaction;
import ca.ulaval.glo4002.trading.transactions.domain.TransactionRequest;
import ca.ulaval.glo4002.trading.transactions.infrastructure.services.TransactionService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TransactionControllerTest {

  private static final UUID A_UUID = UUID.randomUUID();
  private static final APIErrorDTO AN_API_ERROR_DTO = new APIErrorDTO("An error", "A Description");
  private static final String STRING_ACCOUNT_NUMBER = "1337";
  private static final AccountNumber AN_ACCOUNT_NUMBER = new AccountNumber(STRING_ACCOUNT_NUMBER);
  private static TransactionRequestDTO A_TRANSACTION_REQUEST_DTO;
  private static Transaction A_TRANSACTION;
  private static TransactionRequest A_TRANSACTION_REQUEST;

  @Mock
  private AccountNumberAssembler accountNumberAssembler;
  @Mock
  private TransactionService transactionService;
  @Mock
  private TransactionRequestAssembler transactionRequestAssembler;
  @Mock
  private TransactionAssembler transactionAssembler;
  @Mock
  private Validator<TransactionRequestDTO> transactionRequestDTOValidator;
  @Mock
  private TransactionResponse transactionResponse;
  @InjectMocks
  private TransactionController transactionController;


  @Before
  public void setUp() {
    when(this.transactionRequestAssembler.toEntity(A_TRANSACTION_REQUEST_DTO)).thenReturn(A_TRANSACTION_REQUEST);
    when(this.transactionAssembler.toTransactionResponse(A_TRANSACTION)).thenReturn(this.transactionResponse);
    when(this.transactionService.makeTransaction(AN_ACCOUNT_NUMBER, A_TRANSACTION_REQUEST)).thenReturn(A_UUID);
    when(this.transactionService.getTransaction(A_UUID)).thenReturn(A_TRANSACTION);
    when(this.accountNumberAssembler.toEntity(STRING_ACCOUNT_NUMBER)).thenReturn(AN_ACCOUNT_NUMBER);
  }

  @Test
  public void givenAValidTransactionRequestDTO_whenCreatingATransaction_thenLocationHeaderShouldBeSetAsExpected() {
    when(this.transactionRequestDTOValidator.findErrors(A_TRANSACTION_REQUEST_DTO)).thenReturn(Collections.emptyList());

    final ResponseEntity<?> result = this.transactionController.createTransaction(STRING_ACCOUNT_NUMBER, A_TRANSACTION_REQUEST_DTO);
    Assert.assertEquals(String.format("/accounts/%s/transactions/%s", AN_ACCOUNT_NUMBER.getValue(), A_UUID.toString()), result.getHeaders().get(TransactionController.HEADER_LOCATION_ATTRIBUTE).get(0));
  }

  @Test
  public void givenAValidTransactionRequestDTO_whenCreatingATransaction_thenStatusCodeShouldBeAsExpected() {
    when(this.transactionRequestDTOValidator.findErrors(A_TRANSACTION_REQUEST_DTO)).thenReturn(new ArrayList<>());

    final ResponseEntity<?> result = this.transactionController.createTransaction(STRING_ACCOUNT_NUMBER, A_TRANSACTION_REQUEST_DTO);

    Assert.assertEquals(HttpStatus.CREATED, result.getStatusCode());
  }

  @Test
  public void givenAnInvalidTransaction_whenCreatingATransaction_thenTheBodyShouldContainAListWithTheErrors() {
    final List<APIErrorDTO> errors = Collections.singletonList(AN_API_ERROR_DTO);
    when(this.transactionRequestDTOValidator.findErrors(A_TRANSACTION_REQUEST_DTO)).thenReturn(errors);

    final ResponseEntity<?> result = this.transactionController.createTransaction(STRING_ACCOUNT_NUMBER, A_TRANSACTION_REQUEST_DTO);

    Assert.assertEquals(errors, result.getBody());
  }


  @Test
  public void givenAnInvalidTransaction_whenCreatingATransaction_thenStatusCodeShouldBeAsExpected() {
    when(this.transactionRequestDTOValidator.findErrors(A_TRANSACTION_REQUEST_DTO)).thenReturn(Collections.singletonList(AN_API_ERROR_DTO));

    final ResponseEntity<?> result = this.transactionController.createTransaction(STRING_ACCOUNT_NUMBER, A_TRANSACTION_REQUEST_DTO);

    Assert.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
  }


  @Test
  public void whenGettingATransaction_thenStatusCodeShouldBeAsExpected() {
    final ResponseEntity<?> result = this.transactionController.getTransaction(STRING_ACCOUNT_NUMBER, A_UUID);

    Assert.assertEquals(HttpStatus.OK, result.getStatusCode());
  }

  @Test
  public void whenGettingATransaction_thenTheResponseBodyIsTheTransactionResponseAssociatedToTheTransactionUuid() {
    final ResponseEntity<?> result = this.transactionController.getTransaction(STRING_ACCOUNT_NUMBER, A_UUID);

    Assert.assertEquals(this.transactionResponse, result.getBody());
  }
}
