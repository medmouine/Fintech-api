package ca.ulaval.glo4002.trading.accounts.controller.response;

import ca.ulaval.glo4002.trading.accounts.controller.dto.CreditDTO;
import ca.ulaval.glo4002.trading.accounts.controller.dto.InvestorProfileDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AccountResponse {

  private long investorId;

  private List<CreditDTO> credits;

  private String accountNumber;

  private InvestorProfileDTO investorProfile;

  private float total;
}
