package ca.ulaval.glo4002.trading.accounts.controller.assembler;

import ca.ulaval.glo4002.trading.accounts.domain.AccountNumber;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class AccountNumberAssemblerTest {

  private static final String AN_ACCOUNT_NUMBER = "ST-123";
  private AccountNumberAssembler accountNumberAssembler;

  @Before
  public void setup() {
    this.accountNumberAssembler = new AccountNumberAssembler();
  }

  @Test
  public void givenAnAccountNumber_whenToEntityIsCalled_thenShouldReturnValidAccountNumberEntity() {
    final AccountNumber accountNumber = this.accountNumberAssembler.toEntity(AN_ACCOUNT_NUMBER);

    assertEquals(AN_ACCOUNT_NUMBER, accountNumber.getValue());
  }
}
