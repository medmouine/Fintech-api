package ca.ulaval.glo4002.trading.accounts.controller.assembler;

import ca.ulaval.glo4002.trading.accounts.controller.dto.AccountCreationRequestDTO;
import ca.ulaval.glo4002.trading.accounts.controller.dto.CreditDTO;
import ca.ulaval.glo4002.trading.accounts.infrastructure.services.AccountCreationRequest;
import org.javamoney.moneta.Money;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class AccountCreationRequestAssemblerTest {

  private static final float AMOUNT = 15.5f;
  private static final String CAD = "CAD";
  private static final CreditDTO SOME_CREDIT_DTO = new CreditDTO(AMOUNT, CAD);
  private static final List<CreditDTO> CREDIT_DTO_LIST = Arrays.asList(SOME_CREDIT_DTO);
  private static final Money SOME_CREDIT = Money.of(AMOUNT, CAD);

  private static final AccountCreationRequestDTO AN_ACCOUNT_CREATION_REQUEST_DTO = new AccountCreationRequestDTO(1234L,
          "John Doe",
          "jd@test.ca",
          CREDIT_DTO_LIST);

  @InjectMocks
  private AccountCreationRequestAssembler accountCreationRequestAssembler;

  @Test
  public void givenAnAccountCreationRequestDTO_whenConvertedToEntity_thenEntityShouldBeValid() {
    final AccountCreationRequest request = this.accountCreationRequestAssembler.toEntity(AN_ACCOUNT_CREATION_REQUEST_DTO);

    Assert.assertEquals(AN_ACCOUNT_CREATION_REQUEST_DTO.getInvestorId(), request.getInvestorId().getValue());
    Assert.assertEquals(AN_ACCOUNT_CREATION_REQUEST_DTO.getInvestorName(), request.getInvestorName());
    Assert.assertEquals(AN_ACCOUNT_CREATION_REQUEST_DTO.getEmail(), request.getEmail());
    Assert.assertTrue(this.compareCreditsLists(AN_ACCOUNT_CREATION_REQUEST_DTO.getCredits(), request.getCredits()));
  }

  private boolean compareCreditsLists(final List<CreditDTO> creditDTOS, final List<Money> creditsEntities) {
    return creditDTOS.stream()
            .anyMatch(creditDTO -> this.assertCreditsEntityListContainsCreditDTO(creditsEntities, creditDTO))
            &&
            creditsEntities.stream()
                    .anyMatch(creditsEntitie -> this.assertCreditsDTOListContainsCreditEntity(creditDTOS, creditsEntitie));
  }

  private boolean assertCreditsDTOListContainsCreditEntity(final List<CreditDTO> creditDTOList, final Money creditEntity) {
    return creditDTOList.stream()
            .anyMatch(creditDTO -> creditDTO.getAmount() == creditEntity.getNumber().floatValue()
                    &&
                    creditDTO.getCurrency().equals(creditEntity.getCurrency().getCurrencyCode()));
  }

  private boolean assertCreditsEntityListContainsCreditDTO(final List<Money> moneyList, final CreditDTO creditDTO) {
    return moneyList.stream()
            .anyMatch(money -> creditDTO.getAmount() == money.getNumber().floatValue()
                    &&
                    creditDTO.getCurrency().equals(money.getCurrency().getCurrencyCode()));
  }
}