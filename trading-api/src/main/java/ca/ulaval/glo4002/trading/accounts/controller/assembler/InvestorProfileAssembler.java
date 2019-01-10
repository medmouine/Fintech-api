package ca.ulaval.glo4002.trading.accounts.controller.assembler;

import ca.ulaval.glo4002.trading.accounts.controller.dto.InvestorProfileDTO;
import ca.ulaval.glo4002.trading.accounts.domain.investorProfile.InvestorProfile;
import ca.ulaval.glo4002.trading.accounts.domain.investorProfile.InvestorType;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class InvestorProfileAssembler {

  public InvestorProfile toEntity(final InvestorProfileDTO investorProfileDTO) {
    return new InvestorProfile(investorProfileDTO.getType(), investorProfileDTO.getFocusAreas());
  }

  public InvestorProfileDTO toDTO(final InvestorProfile investorProfileEntity) {
    final InvestorProfileDTO investorProfileDTO = new InvestorProfileDTO();

    investorProfileDTO.setFocusAreas(investorProfileEntity.getFocusAreas());
    investorProfileDTO.setType(investorProfileEntity.getInvestorType());

    return investorProfileDTO;
  }
}
