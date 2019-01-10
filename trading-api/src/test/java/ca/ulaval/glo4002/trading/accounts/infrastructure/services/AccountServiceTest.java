package ca.ulaval.glo4002.trading.accounts.infrastructure.services;

import ca.ulaval.glo4002.trading.accounts.domain.*;
import org.javamoney.moneta.Money;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

  private static final String CAD = "CAD";
  private static final float AMOUNT = 15.5F;
  private static final Money SOME_MONEY = Money.of(AMOUNT, CAD);
  private static final List<Money> CREDITS_LIST = Arrays.asList(SOME_MONEY);
  private static final String ACCOUNT_NUMBER_VALUE = "1337";
  private static final AccountNumber AN_ACCOUNT_NUMBER = new AccountNumber(ACCOUNT_NUMBER_VALUE);
  private static final AccountCreationRequest AN_ACCOUNT_CREATION_REQUEST = new AccountCreationRequest(new InvestorId(1233L),
          "John Doe",
          "jd@test.ca",
          CREDITS_LIST);

  @Mock
  private Account account;

  @Mock
  private AccountRepository accountRepository;

  @Mock
  private AccountFactory accountFactory;

  @InjectMocks
  private AccountService accountService;

  @Test
  public void givenARequestToCreateAnAccount_whenNoAccountWithThisInvestorIdExists_thenAccountIsCreated() {
    when(this.accountFactory.create(AN_ACCOUNT_CREATION_REQUEST)).thenReturn(this.account);

    this.accountService.openAccount(AN_ACCOUNT_CREATION_REQUEST);

    verify(this.accountRepository).save(this.account);
  }


  @Test
  public void givenAlreadyExistingAccount_WhenCreateAccountIsCalled_thenAccountIsNotCreated() {
    this.accountService.openAccount(AN_ACCOUNT_CREATION_REQUEST);

    verify(this.accountRepository, times(1)).assertAccountDoesntExist(AN_ACCOUNT_CREATION_REQUEST.getInvestorId());
    verify(this.accountRepository, times(0)).save(this.account);
  }

  @Test
  public void givenExistingAccount_whenFindingAccountByAccountNumber_thenAccountIsReturned() {
    when(this.accountRepository.findByAccountNumber(AN_ACCOUNT_NUMBER)).thenReturn(this.account);

    final Account response = this.accountService.findByAccountNumber(AN_ACCOUNT_NUMBER);

    assertEquals(this.account, response);
  }

}
