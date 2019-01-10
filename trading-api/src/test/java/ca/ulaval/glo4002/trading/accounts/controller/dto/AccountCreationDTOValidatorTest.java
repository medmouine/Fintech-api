package ca.ulaval.glo4002.trading.accounts.controller.dto;

import ca.ulaval.glo4002.trading.accounts.controller.dto.errors.InvalidAmountError;
import ca.ulaval.glo4002.trading.accounts.controller.dto.errors.InvalidCurrencyError;
import ca.ulaval.glo4002.trading.common.controller.dto.errors.APIErrorDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class AccountCreationDTOValidatorTest {

  private static final String USD = "USD";
  private static final String UnknownCurrency = "AAA";
  private static final float VALID_GREATER_THAN_ZERO_CREDITS_AMOUNT = 5f;
  private static final CreditDTO VALID_CREDIT_DTO = new CreditDTO(VALID_GREATER_THAN_ZERO_CREDITS_AMOUNT, USD);
  private static final List<CreditDTO> VALID_CREDIT_DTO_LIST = Arrays.asList(VALID_CREDIT_DTO);
  private static final CreditDTO INVALID_CREDITS_AMOUNT_DTO = new CreditDTO(0f, USD);
  private static final CreditDTO INVALID_CREDITS_CURRENCY_DTO = new CreditDTO(5f, UnknownCurrency);
  private static final List<CreditDTO> INVALID_CREDITS_AMOUNT_DTO_LIST = Arrays.asList(INVALID_CREDITS_AMOUNT_DTO);
  private static final List<CreditDTO> INVALID_CREDITS_CURRENCY_DTO_LIST = Arrays.asList(INVALID_CREDITS_CURRENCY_DTO);
  private static final List<CreditDTO> INVALID_CREDITS_CURRENCY_AMOUNT_DTO_LIST = Arrays.asList(INVALID_CREDITS_CURRENCY_DTO, INVALID_CREDITS_AMOUNT_DTO);

  private final AccountCreationRequestDTO accountCreationRequestDTO = new AccountCreationRequestDTO();
  @InjectMocks
  private AccountCreationRequestDTOValidator accountCreationRequestDTOValidator;

  @Test
  public void givenValidAccountDTO_whenLookingForErrors_thenEmptyErrorsListIsReturned() {
    this.accountCreationRequestDTO.setCredits(VALID_CREDIT_DTO_LIST);
    final List<APIErrorDTO> errors = this.accountCreationRequestDTOValidator.findErrors(this.accountCreationRequestDTO);

    assertEquals(Collections.emptyList(), errors);
  }

  @Test
  public void givenAccountWithInvalidCredits_whenFindingErrors_thenReturnAllErrors() {
    final APIErrorDTO expectedCurrencyError = new InvalidCurrencyError();
    final APIErrorDTO expectedAmountError = new InvalidCurrencyError();
    this.accountCreationRequestDTO.setCredits(INVALID_CREDITS_CURRENCY_AMOUNT_DTO_LIST);

    final List<APIErrorDTO> errors = this.accountCreationRequestDTOValidator.findErrors(this.accountCreationRequestDTO);

    assertTrue(errors.contains(expectedCurrencyError) && errors.contains(expectedAmountError));
  }

  @Test
  public void givenAccountWithInvalidCreditsAmount_whenFindingErrors_thenReturnInvalidAmountError() {
    final APIErrorDTO expectedError = new InvalidAmountError();
    this.accountCreationRequestDTO.setCredits(INVALID_CREDITS_AMOUNT_DTO_LIST);

    final List<APIErrorDTO> errors = this.accountCreationRequestDTOValidator.findErrors(this.accountCreationRequestDTO);

    assertTrue(errors.contains(expectedError));
  }

  @Test
  public void givenAccountWithInvalidCreditsCurrency_whenFindingErrors_thenReturnInvalidCurrencyError() {
    final APIErrorDTO expectedError = new InvalidCurrencyError();
    this.accountCreationRequestDTO.setCredits(INVALID_CREDITS_CURRENCY_DTO_LIST);

    final List<APIErrorDTO> errors = this.accountCreationRequestDTOValidator.findErrors(this.accountCreationRequestDTO);

    assertTrue(errors.contains(expectedError));
  }
}
