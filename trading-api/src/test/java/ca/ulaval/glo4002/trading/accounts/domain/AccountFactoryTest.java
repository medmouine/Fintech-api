package ca.ulaval.glo4002.trading.accounts.domain;

import ca.ulaval.glo4002.trading.accounts.infrastructure.services.AccountCreationRequest;
import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccountFactoryTest {

  private static final String CAD = "CAD";
  private static final float AMOUNT = 15.5F;
  private static final Money SOME_MONEY = Money.of(AMOUNT, CAD);
  private static final List<Money> CREDITS_LIST = Arrays.asList(SOME_MONEY);
  private static final int NUMBER_OF_ACCOUNTS = 100;
  private static final AccountCreationRequest AN_ACCOUNT_CREATION_REQUEST = new AccountCreationRequest(new InvestorId(1233L),
          "John Doe",
          "jd@test.ca",
          CREDITS_LIST);
  @Mock
  private AccountRepository accountRepository;

  @Mock
  private StockWalletFactory stockWalletFactory;

  @Mock
  private StockWallet stockWallet;

  @InjectMocks
  private AccountFactory accountFactory;

  @Before
  public void setUp() {
    when(this.stockWallet.getCredits()).thenReturn(CREDITS_LIST);
    when(this.accountRepository.getNumberOfAccountOpened()).thenReturn(NUMBER_OF_ACCOUNTS);
    when(this.stockWalletFactory.create(CREDITS_LIST)).thenReturn(this.stockWallet);
  }

  @Test
  public void givenAnAccountCreationRequest_whenCreatingAccount_thenCreatedAccountIsValid() {
    final Account account = this.accountFactory.create(AN_ACCOUNT_CREATION_REQUEST);

    assertEquals(AN_ACCOUNT_CREATION_REQUEST.getInvestorId(), account.getInvestorId());
    assertEquals("JD-101", account.getAccountNumber().getValue());
    assertEquals(AN_ACCOUNT_CREATION_REQUEST.getEmail(), account.getEmail());
    assertEquals(AN_ACCOUNT_CREATION_REQUEST.getInvestorName(), account.getInvestorName());
    assertEquals(AN_ACCOUNT_CREATION_REQUEST.getCredits(), account.getWallet().getCredits());
  }

  @Test
  public void givenAnAccountCreationRequest_whenCreatingAccount_thenAccountNumberIsInitialsAndNextNumber() {
    final Account account = this.accountFactory.create(AN_ACCOUNT_CREATION_REQUEST);

    final String expectedAccountNumber = "JD-" + (NUMBER_OF_ACCOUNTS + 1);

    assertEquals(expectedAccountNumber, account.getAccountNumber().getValue());
  }

}