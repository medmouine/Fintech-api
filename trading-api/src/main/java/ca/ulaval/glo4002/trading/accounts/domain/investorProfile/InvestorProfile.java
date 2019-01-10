package ca.ulaval.glo4002.trading.accounts.domain.investorProfile;

import lombok.Data;

import java.util.List;

@Data
public class InvestorProfile {
  private InvestorType investorType;
  private List<FocusArea> focusAreas;

  public InvestorProfile(final InvestorType investorType, final List<FocusArea> focusAreas) {
    this.investorType = investorType;
    this.focusAreas = focusAreas;
  }
}
