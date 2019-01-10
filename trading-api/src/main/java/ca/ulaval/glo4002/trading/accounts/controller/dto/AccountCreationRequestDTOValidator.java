package ca.ulaval.glo4002.trading.accounts.controller.dto;

import ca.ulaval.glo4002.trading.accounts.controller.dto.errors.InvalidAmountError;
import ca.ulaval.glo4002.trading.accounts.controller.dto.errors.InvalidCurrencyError;
import ca.ulaval.glo4002.trading.common.controller.dto.Validator;
import ca.ulaval.glo4002.trading.common.controller.dto.errors.APIErrorDTO;
import ca.ulaval.glo4002.trading.markets.domain.Market;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class AccountCreationRequestDTOValidator implements Validator<AccountCreationRequestDTO> {

  private static boolean hasKnownCurrency(final CreditDTO creditDTO) {
    return Arrays.stream(Market.values())
            .anyMatch(market -> market.getCurrencyCode().equals(creditDTO.getCurrency()));
  }

  @Override
  public List<APIErrorDTO> findErrors(final AccountCreationRequestDTO accountCreationRequestDTO) {

    final List<APIErrorDTO> errors = new ArrayList<>();

    this.getCreditCurrencyErrors(accountCreationRequestDTO, errors);
    this.getCreditsAmountErrors(accountCreationRequestDTO, errors);

    return errors;
  }

  private void getCreditsAmountErrors(final AccountCreationRequestDTO accountCreationRequestDTO, final List<APIErrorDTO> errors) {
    if (accountCreationRequestDTO.getCredits().stream().anyMatch(creditDTO -> creditDTO.getAmount() <= 0f)) {
      errors.add(new InvalidAmountError());
    }
  }

  private void getCreditCurrencyErrors(final AccountCreationRequestDTO accountCreationRequestDTO, final List<APIErrorDTO> errors) {
    if (accountCreationRequestDTO.getCredits().stream().anyMatch(creditDTO -> !hasKnownCurrency(creditDTO))) {
      errors.add(new InvalidCurrencyError());
    }
  }
}
