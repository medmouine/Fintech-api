package ca.ulaval.glo4002.trading.accounts.controller;

import ca.ulaval.glo4002.trading.accounts.controller.assembler.AccountCreationRequestAssembler;
import ca.ulaval.glo4002.trading.accounts.controller.assembler.AccountNumberAssembler;
import ca.ulaval.glo4002.trading.accounts.controller.assembler.AccountResponseAssembler;
import ca.ulaval.glo4002.trading.accounts.controller.dto.AccountCreationRequestDTO;
import ca.ulaval.glo4002.trading.accounts.controller.dto.AccountCreationRequestDTOValidator;
import ca.ulaval.glo4002.trading.accounts.controller.dto.CreditDTO;
import ca.ulaval.glo4002.trading.accounts.controller.dto.errors.InvalidAmountError;
import ca.ulaval.glo4002.trading.accounts.controller.response.AccountResponse;
import ca.ulaval.glo4002.trading.accounts.domain.Account;
import ca.ulaval.glo4002.trading.accounts.infrastructure.services.AccountCreationRequest;
import ca.ulaval.glo4002.trading.accounts.domain.AccountNumber;
import ca.ulaval.glo4002.trading.accounts.domain.InvestorId;
import ca.ulaval.glo4002.trading.accounts.infrastructure.services.AccountService;
import ca.ulaval.glo4002.trading.common.controller.dto.errors.APIErrorDTO;
import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AccountControllerTest {

  private static final String CAD = "CAD";
  private static final float AMOUNT = 15.5F;
  private static final Money SOME_MONEY = Money.of(AMOUNT, CAD);
  private static final CreditDTO SOME_CREDIT_DTO = new CreditDTO(AMOUNT, CAD);
  private static final List<Money> CREDITS_LIST = Arrays.asList(SOME_MONEY);
  private static final List<CreditDTO> CREDIT_DTO_LIST = Arrays.asList(SOME_CREDIT_DTO);
  private static final AccountCreationRequestDTO AN_ACCOUNT_CREATION_REQUEST_DTO = new AccountCreationRequestDTO(1234L,
          "John Doe",
          "jd@test.ca",
          CREDIT_DTO_LIST);
  private static final AccountCreationRequest AN_ACCOUNT_CREATION_REQUEST = new AccountCreationRequest(new InvestorId(1234L),
          "John Doe",
          "jd@test.ca",
          CREDITS_LIST);
  private static final InvestorId INVESTOR_ID = new InvestorId(123L);
  private static final String STRING_ACCOUNT_NUMBER = "123";
  private static final AccountNumber ACCOUNT_NUMBER = new AccountNumber(STRING_ACCOUNT_NUMBER);
  private final List<APIErrorDTO> INVALID_AMOUNT_API_ERROR = Collections.singletonList(new InvalidAmountError());
  @Mock
  private AccountResponse accountResponse;

  @Mock
  private Account accountEntity;

  @Mock
  private AccountService accountService;

  @Mock
  private AccountResponseAssembler accountResponseAssembler;

  @Mock
  private AccountCreationRequestAssembler accountCreationRequestAssembler;

  @Mock
  private AccountCreationRequestDTOValidator accountCreationRequestDTOValidator;

  @Mock
  private AccountNumberAssembler accountNumberAssembler;

  @InjectMocks
  private AccountController accountController;

  @Before
  public void setup() {
    when(this.accountEntity.getInvestorId()).thenReturn(INVESTOR_ID);
    when(this.accountResponseAssembler.toAccountResponse(this.accountEntity)).thenReturn(this.accountResponse);
    when(this.accountCreationRequestAssembler.toEntity(AN_ACCOUNT_CREATION_REQUEST_DTO)).thenReturn(AN_ACCOUNT_CREATION_REQUEST);
    when(this.accountNumberAssembler.toEntity(STRING_ACCOUNT_NUMBER)).thenReturn(ACCOUNT_NUMBER);
    when(this.accountEntity.getAccountNumber()).thenReturn(ACCOUNT_NUMBER);
    when(this.accountService.openAccount(AN_ACCOUNT_CREATION_REQUEST)).thenReturn(this.accountEntity);
  }

  @Test
  public void givenADTOWithNoErrors_whenCreatingAnAccount_thenCreateAccountSuccessfully() {
    when(this.accountCreationRequestDTOValidator.findErrors(AN_ACCOUNT_CREATION_REQUEST_DTO)).thenReturn(Collections.emptyList());

    this.accountController.createAccount(AN_ACCOUNT_CREATION_REQUEST_DTO);

    verify(this.accountService).openAccount(AN_ACCOUNT_CREATION_REQUEST);
  }

  @Test
  public void givenADTOWithNoErrors_whenCreatingAnAccount_thenLocationHeaderIsSetCorrectly() {
    when(this.accountCreationRequestDTOValidator.findErrors(AN_ACCOUNT_CREATION_REQUEST_DTO)).thenReturn(new ArrayList<>());

    final ResponseEntity<?> response = this.accountController.createAccount(AN_ACCOUNT_CREATION_REQUEST_DTO);

    assertEquals(URI.create(String.format("/accounts/%s", this.accountEntity.getAccountNumber().getValue())), response.getHeaders().getLocation());
  }

  @Test
  public void givenADTOWithNoErrors_whenCreatingAnAccount_thenCreatedHttpStatusIsReturned() {
    when(this.accountCreationRequestDTOValidator.findErrors(AN_ACCOUNT_CREATION_REQUEST_DTO)).thenReturn(new ArrayList<>());

    final ResponseEntity<?> response = this.accountController.createAccount(AN_ACCOUNT_CREATION_REQUEST_DTO);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
  }

  @Test
  public void givenADTOWithErrors_whenCreatingAnAccount_thenAccountIsNotCreated() {
    when(this.accountCreationRequestDTOValidator.findErrors(AN_ACCOUNT_CREATION_REQUEST_DTO)).thenReturn(this.INVALID_AMOUNT_API_ERROR);

    this.accountController.createAccount(AN_ACCOUNT_CREATION_REQUEST_DTO);

    verify(this.accountService, never()).openAccount(AN_ACCOUNT_CREATION_REQUEST);
  }

  @Test
  public void givenADTOWithErrors_whenCreatingAnAccount_thenBadRequestHttpStatusIsReturned() {
    when(this.accountCreationRequestDTOValidator.findErrors(AN_ACCOUNT_CREATION_REQUEST_DTO)).thenReturn(this.INVALID_AMOUNT_API_ERROR);

    final ResponseEntity<?> response = this.accountController.createAccount(AN_ACCOUNT_CREATION_REQUEST_DTO);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  public void givenADTOWithErrors_whenCreatingAnAccount_thenTheListOfErrorsIsReturned() {
    when(this.accountCreationRequestDTOValidator.findErrors(AN_ACCOUNT_CREATION_REQUEST_DTO)).thenReturn(this.INVALID_AMOUNT_API_ERROR);

    final ResponseEntity<?> response = this.accountController.createAccount(AN_ACCOUNT_CREATION_REQUEST_DTO);

    assertEquals(this.INVALID_AMOUNT_API_ERROR, response.getBody());
  }

  @Test
  public void givenExistingInvestorId_whenGettingAnAccount_thenOKHttpStatusIsReturned() {
    when(this.accountService.findByAccountNumber(ACCOUNT_NUMBER)).thenReturn(this.accountEntity);
    when(this.accountResponseAssembler.toAccountResponse(this.accountEntity)).thenReturn(this.accountResponse);

    final ResponseEntity<?> response = this.accountController.getAccount(STRING_ACCOUNT_NUMBER);

    assertEquals(response.getStatusCode(), HttpStatus.OK);
  }

  @Test
  public void givenExistingInvestorId_whenGettingAnAccount_thenAccountIsReturned() {
    when(this.accountService.findByAccountNumber(ACCOUNT_NUMBER)).thenReturn(this.accountEntity);
    when(this.accountResponseAssembler.toAccountResponse(this.accountEntity)).thenReturn(this.accountResponse);

    final ResponseEntity<?> response = this.accountController.getAccount(STRING_ACCOUNT_NUMBER);

    assertEquals(this.accountResponse, response.getBody());
  }
}
