package ca.ulaval.glo4002.trading.transactions.controller;

import ca.ulaval.glo4002.trading.accounts.controller.assembler.AccountNumberAssembler;
import ca.ulaval.glo4002.trading.accounts.domain.AccountNumber;
import ca.ulaval.glo4002.trading.common.controller.ControllerBase;
import ca.ulaval.glo4002.trading.common.controller.dto.Validator;
import ca.ulaval.glo4002.trading.common.controller.dto.errors.APIErrorDTO;
import ca.ulaval.glo4002.trading.transactions.controller.assembler.TransactionAssembler;
import ca.ulaval.glo4002.trading.transactions.controller.assembler.TransactionRequestAssembler;
import ca.ulaval.glo4002.trading.transactions.controller.dto.TransactionRequestDTO;
import ca.ulaval.glo4002.trading.transactions.controller.response.TransactionResponse;
import ca.ulaval.glo4002.trading.transactions.domain.Transaction;
import ca.ulaval.glo4002.trading.transactions.domain.TransactionRequest;
import ca.ulaval.glo4002.trading.transactions.infrastructure.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RepositoryRestController
@RequestMapping("/accounts/{accountNumber}/transactions")
public class TransactionController extends ControllerBase {

  public static final String HEADER_LOCATION_ATTRIBUTE = "Location";

  private final TransactionService transactionService;
  private final Validator<TransactionRequestDTO> transactionRequestDTOValidator;
  private final TransactionAssembler transactionAssembler;
  private final AccountNumberAssembler accountNumberAssembler;
  private final TransactionRequestAssembler transactionRequestAssembler;

  @Autowired
  public TransactionController(
          final TransactionService transactionService,
          final Validator<TransactionRequestDTO> transactionRequestDTOValidator,
          final TransactionAssembler transactionAssembler,
          final AccountNumberAssembler accountNumberAssembler,
          final TransactionRequestAssembler transactionRequestAssembler) {
    this.transactionService = transactionService;
    this.transactionRequestDTOValidator = transactionRequestDTOValidator;
    this.transactionAssembler = transactionAssembler;
    this.accountNumberAssembler = accountNumberAssembler;
    this.transactionRequestAssembler = transactionRequestAssembler;
  }

  @PostMapping
  public ResponseEntity<?> createTransaction(
          @PathVariable("accountNumber") final String accountNumber,
          @RequestBody final TransactionRequestDTO transactionRequestDTO) {

    final List<APIErrorDTO> errors = this.transactionRequestDTOValidator.findErrors(transactionRequestDTO);

    if (!errors.isEmpty()) {
      return this.buildValidationErrorResponse(errors);
    }

    final AccountNumber accountNumberEntity = this.accountNumberAssembler.toEntity(accountNumber);
    final TransactionRequest transactionRequest = this.transactionRequestAssembler.toEntity(transactionRequestDTO);
    final UUID transactionNumber = this.transactionService.makeTransaction(accountNumberEntity, transactionRequest);
    return new ResponseEntity<>(this.getSuccessHeaders(accountNumberEntity, transactionNumber), HttpStatus.CREATED);
  }

  @GetMapping("/{transactionNumber}")
  public ResponseEntity<TransactionResponse> getTransaction(
          @PathVariable("accountNumber") final String accountNumber,
          @PathVariable("transactionNumber") final UUID transactionNumber) {
    final Transaction transaction = this.transactionService.getTransaction(transactionNumber);
    return new ResponseEntity<>(this.transactionAssembler.toTransactionResponse(transaction), HttpStatus.OK);
  }

  private HttpHeaders getSuccessHeaders(final AccountNumber accountNumber, final UUID transactionNumber) {
    final HttpHeaders headers = new HttpHeaders();
    headers.add(HEADER_LOCATION_ATTRIBUTE, String.format("/accounts/%s/transactions/%s", accountNumber.getValue(), transactionNumber.toString()));
    return headers;
  }
}
