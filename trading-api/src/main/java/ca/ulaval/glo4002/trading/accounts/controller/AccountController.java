package ca.ulaval.glo4002.trading.accounts.controller;

import ca.ulaval.glo4002.trading.accounts.controller.assembler.AccountCreationRequestAssembler;
import ca.ulaval.glo4002.trading.accounts.controller.assembler.AccountNumberAssembler;
import ca.ulaval.glo4002.trading.accounts.controller.assembler.AccountResponseAssembler;
import ca.ulaval.glo4002.trading.accounts.controller.dto.AccountCreationRequestDTO;
import ca.ulaval.glo4002.trading.accounts.controller.response.AccountResponse;
import ca.ulaval.glo4002.trading.accounts.domain.Account;
import ca.ulaval.glo4002.trading.accounts.infrastructure.services.AccountCreationRequest;
import ca.ulaval.glo4002.trading.accounts.infrastructure.services.AccountService;
import ca.ulaval.glo4002.trading.common.controller.ControllerBase;
import ca.ulaval.glo4002.trading.common.controller.dto.Validator;
import ca.ulaval.glo4002.trading.common.controller.dto.errors.APIErrorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RepositoryRestController
@RequestMapping("/accounts")
public class AccountController extends ControllerBase {

  private static final String HEADERS_LOCATION_ATTRIBUTE = "Location";

  private final AccountService accountService;
  private final AccountResponseAssembler accountResponseAssembler;
  private final AccountCreationRequestAssembler accountCreationRequestAssembler;
  private final Validator<AccountCreationRequestDTO> accountCreationRequestDTOValidator;
  private final AccountNumberAssembler accountNumberAssembler;

  @Autowired
  public AccountController(final AccountService accountService,
                           final AccountResponseAssembler accountResponseAssembler,
                           final AccountCreationRequestAssembler accountCreationRequestAssembler,
                           final AccountNumberAssembler accountNumberAssembler,
                           final Validator<AccountCreationRequestDTO> accountCreationRequestDTOValidator) {
    this.accountService = accountService;
    this.accountResponseAssembler = accountResponseAssembler;
    this.accountCreationRequestAssembler = accountCreationRequestAssembler;
    this.accountCreationRequestDTOValidator = accountCreationRequestDTOValidator;
    this.accountNumberAssembler = accountNumberAssembler;
  }

  @PostMapping
  public ResponseEntity<?> createAccount(@RequestBody final AccountCreationRequestDTO accountCreationRequestDTO) {
    final List<APIErrorDTO> errors = this.accountCreationRequestDTOValidator.findErrors(accountCreationRequestDTO);
    if (!errors.isEmpty()) {
      return this.buildValidationErrorResponse(errors);
    }
    final AccountCreationRequest accountCreationRequest = this.accountCreationRequestAssembler.toEntity(accountCreationRequestDTO);
    final Account account = this.accountService.openAccount(accountCreationRequest);
    return new ResponseEntity<>(this.getSuccessHeaders(account), HttpStatus.CREATED);
  }

  @GetMapping("/{accountNumber}")
  public ResponseEntity<AccountResponse> getAccount(@PathVariable("accountNumber") final String accountNumber) {
    final Account account = this.accountService.findByAccountNumber(this.accountNumberAssembler.toEntity(accountNumber));
    return new ResponseEntity<>(this.accountResponseAssembler.toAccountResponse(account), HttpStatus.OK);
  }

  private HttpHeaders getSuccessHeaders(final Account accountEntity) {
    final HttpHeaders headers = new HttpHeaders();
    headers.set(HEADERS_LOCATION_ATTRIBUTE, String.format("/accounts/%s", accountEntity.getAccountNumber().getValue()));
    return headers;
  }
}
