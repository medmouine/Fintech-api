package ca.ulaval.glo4002.trading.accounts.controller.assembler;

import ca.ulaval.glo4002.trading.accounts.controller.dto.CreditDTO;
import ca.ulaval.glo4002.trading.accounts.controller.dto.InvestorProfileDTO;
import ca.ulaval.glo4002.trading.accounts.controller.response.AccountResponse;
import ca.ulaval.glo4002.trading.accounts.domain.Account;
import ca.ulaval.glo4002.trading.accounts.domain.AccountNumber;
import ca.ulaval.glo4002.trading.accounts.domain.InvestorId;
import ca.ulaval.glo4002.trading.accounts.domain.StockWallet;
import ca.ulaval.glo4002.trading.accounts.domain.investorProfile.FocusArea;
import ca.ulaval.glo4002.trading.accounts.domain.investorProfile.InvestorProfile;
import ca.ulaval.glo4002.trading.accounts.domain.investorProfile.InvestorType;
import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccountResponseAssemblerTest {

  private static final List<FocusArea> EMPTY_FOCUS_AREA = Collections.emptyList();
  private static final InvestorType CONSERVATIVE_INVESTOR_TYPE = InvestorType.CONSERVATIVE;
  private static final float VALID_CREDIT = 10F;
  public static final String CAD = "CAD";
  private static final Money A_MONEY_AMOUNT = Money.of(VALID_CREDIT, CAD);
  private static final CreditDTO A_CREDIT_DTO = new CreditDTO(VALID_CREDIT, CAD);
  private static final List<Money> CREDITS_LIST = Arrays.asList(A_MONEY_AMOUNT);
  private static final List<CreditDTO> CREDIT_DTO_LIST = Arrays.asList(A_CREDIT_DTO);
  private static final InvestorId AN_INVESTOR_ID = new InvestorId(123L);
  private static final String AN_INVESTOR_NAME = "keven";
  private static final String AN_EMAIL = "keven@email.com";
  private static final AccountNumber ACCOUNT_NUMBER = new AccountNumber("123");
  private static final InvestorProfile INVESTOR_PROFILE = new InvestorProfile(CONSERVATIVE_INVESTOR_TYPE,
          EMPTY_FOCUS_AREA);
  private static final InvestorProfileDTO INVESTOR_PROFILE_DTO = new InvestorProfileDTO(CONSERVATIVE_INVESTOR_TYPE,
          EMPTY_FOCUS_AREA);
  private static final double ACCEPTABLE_DELTA_FOR_DTOS_MONEY_TO_FLOAT = 0.0001;

  @Mock
  private StockWallet stockWallet;

  @InjectMocks
  private final Account AN_ACCOUNT = new Account(INVESTOR_PROFILE,
          AN_INVESTOR_ID,
          AN_INVESTOR_NAME,
          AN_EMAIL,
          this.stockWallet,
          ACCOUNT_NUMBER);
  @Mock
  private CreditAssembler creditAssembler;
  @Mock
  private InvestorProfileAssembler investorProfileAssembler;
  @InjectMocks
  private AccountResponseAssembler accountResponseAssembler;

  @Before
  public void setup() {
    when(this.investorProfileAssembler.toDTO(INVESTOR_PROFILE)).thenReturn(INVESTOR_PROFILE_DTO);
    when(this.investorProfileAssembler.toEntity(INVESTOR_PROFILE_DTO)).thenReturn(INVESTOR_PROFILE);
    when(this.creditAssembler.ToDTOList(CREDITS_LIST)).thenReturn(CREDIT_DTO_LIST);
    when(this.stockWallet.getCredits()).thenReturn(CREDITS_LIST );
    when(this.stockWallet.getCreditsSumInCAD()).thenReturn(A_MONEY_AMOUNT);
  }

  @Test
  public void givenAnAccountEntity_whenConvertedToResponse_thenItShouldBeValid() {
    final Account accountEntity = this.AN_ACCOUNT;

    final AccountResponse accountResponse = this.accountResponseAssembler.toAccountResponse(accountEntity);

    assertEquals(accountEntity.getWallet().getCredits().size(), accountResponse.getCredits().size());
    assertEquals(accountEntity.getInvestorId().getValue(), accountResponse.getInvestorId());
    assertEquals(accountEntity.getAccountNumber().getValue(), accountResponse.getAccountNumber());
    assertEquals(INVESTOR_PROFILE_DTO, accountResponse.getInvestorProfile());
    assertEquals(accountEntity.getWallet().getCreditsSumInCAD(), A_MONEY_AMOUNT);
  }
}
