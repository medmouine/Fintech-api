package ca.ulaval.glo4002.trading.accounts.controller.assembler;

import ca.ulaval.glo4002.trading.accounts.controller.dto.InvestorProfileDTO;
import ca.ulaval.glo4002.trading.accounts.domain.investorProfile.FocusArea;
import ca.ulaval.glo4002.trading.accounts.domain.investorProfile.InvestorProfile;
import ca.ulaval.glo4002.trading.accounts.domain.investorProfile.InvestorType;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class InvestorProfileAssemblerTest {

  private static final InvestorType CONSERVATIVE_INVESTOR_TYPE = InvestorType.CONSERVATIVE;
  private static final List<FocusArea> EMPTY_FOCUS_AREA = Collections.emptyList();

  private static final InvestorProfileDTO INVESTOR_PROFILE_DTO = new InvestorProfileDTO(CONSERVATIVE_INVESTOR_TYPE,
          EMPTY_FOCUS_AREA);
  private static final InvestorProfile INVESTOR_PROFILE = new InvestorProfile(CONSERVATIVE_INVESTOR_TYPE,
          EMPTY_FOCUS_AREA);

  private InvestorProfileAssembler investorProfileAssembler;

  @Before
  public void setup() {
    this.investorProfileAssembler = new InvestorProfileAssembler();
  }

  @Test
  public void givenInvestorProfileDTO_whenConvertedToEntity_thenEntityShouldBeValid() {
    final InvestorProfileDTO investorProfileDTO = INVESTOR_PROFILE_DTO;

    final InvestorProfile investorProfileEntity = this.investorProfileAssembler.toEntity(investorProfileDTO);

    assertEquals(investorProfileDTO.getFocusAreas(), investorProfileEntity.getFocusAreas());
    assertEquals(investorProfileDTO.getType(), investorProfileEntity.getInvestorType());
  }

  @Test
  public void givenInvestorProfileEntity_whenToDTOIsCalled_thenDTOShouldBeValid() {
    final InvestorProfile investorProfileEntity = INVESTOR_PROFILE;

    final InvestorProfileDTO investorProfileDTO = this.investorProfileAssembler.toDTO(investorProfileEntity);

    assertEquals(investorProfileEntity.getFocusAreas(), investorProfileDTO.getFocusAreas());
    assertEquals(investorProfileEntity.getInvestorType(), investorProfileDTO.getType());
  }
}
