package ca.ulaval.glo4002.trading.accounts.infrastructure.services;

import ca.ulaval.glo4002.trading.accounts.domain.InvestorId;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.javamoney.moneta.Money;

import java.util.List;

@Data
@AllArgsConstructor
public class AccountCreationRequest {

  private InvestorId investorId;

  private String investorName;

  private String email;

  private List<Money> credits;

}
