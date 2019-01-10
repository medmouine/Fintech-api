package ca.ulaval.glo4002.trading.accounts.controller.dto;

import ca.ulaval.glo4002.trading.accounts.domain.investorProfile.FocusArea;
import ca.ulaval.glo4002.trading.accounts.domain.investorProfile.InvestorType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
public class InvestorProfileDTO {

  private InvestorType type;

  private List<FocusArea> focusAreas;

  public InvestorProfileDTO() {
    this.type = InvestorType.CONSERVATIVE;
    this.focusAreas = Collections.emptyList();
  }
}
