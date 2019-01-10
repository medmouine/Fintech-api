package ca.ulaval.glo4002.trading.accounts.controller.assembler;

import ca.ulaval.glo4002.trading.accounts.controller.dto.AccountCreationRequestDTO;
import ca.ulaval.glo4002.trading.accounts.controller.dto.CreditDTO;
import ca.ulaval.glo4002.trading.accounts.infrastructure.services.AccountCreationRequest;
import ca.ulaval.glo4002.trading.accounts.domain.InvestorId;
import org.javamoney.moneta.Money;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AccountCreationRequestAssembler {

  public AccountCreationRequest toEntity(final AccountCreationRequestDTO accountCreationRequestDTO) {

    return new AccountCreationRequest(new InvestorId(accountCreationRequestDTO.getInvestorId()),
            accountCreationRequestDTO.getInvestorName(),
            accountCreationRequestDTO.getEmail(),
            this.buildCreditsList(accountCreationRequestDTO.getCredits()));
  }

  private List<Money> buildCreditsList(final List<CreditDTO> creditDTOs) {
    return creditDTOs.stream()
            .map(creditDTO -> Money.of(creditDTO.getAmount(), creditDTO.getCurrency()))
            .collect(Collectors.toList());
  }
}
