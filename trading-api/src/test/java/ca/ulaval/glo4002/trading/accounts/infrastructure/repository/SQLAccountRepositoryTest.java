package ca.ulaval.glo4002.trading.accounts.infrastructure.repository;

import ca.ulaval.glo4002.trading.accounts.controller.exceptions.AccountAlreadyOpenException;
import ca.ulaval.glo4002.trading.accounts.controller.exceptions.AccountNotFoundException;
import ca.ulaval.glo4002.trading.accounts.domain.Account;
import ca.ulaval.glo4002.trading.accounts.domain.AccountNumber;
import ca.ulaval.glo4002.trading.accounts.domain.InvestorId;
import ca.ulaval.glo4002.trading.accounts.infrastructure.repository.hibernate.AccountHibernateEntity;
import ca.ulaval.glo4002.trading.accounts.infrastructure.repository.hibernate.AccountHibernateEntityAssembler;
import ca.ulaval.glo4002.trading.accounts.infrastructure.repository.hibernate.HibernateAccountRepository;
import ca.ulaval.glo4002.trading.accounts.infrastructure.repository.hibernate.SQLAccountRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SQLAccountRepositoryTest {

  private static final InvestorId AN_INVESTOR_ID = new InvestorId(1);
  private static final AccountNumber AN_ACCOUNT_NUMBER = new AccountNumber("JS-123");

  @Mock
  private Account account;
  @Mock
  private AccountHibernateEntity accountHibernateEntity;
  @Mock
  private HibernateAccountRepository hibernateAccountRepository;
  @Mock
  private AccountHibernateEntityAssembler accountHibernateEntityAssembler;
  @InjectMocks
  private SQLAccountRepository accountRepository;

  @Before
  public void setUp() {
    when(this.hibernateAccountRepository.findAccountHibernateEntityByInvestorId(AN_INVESTOR_ID.getValue())).thenReturn(this.accountHibernateEntity);
    when(this.hibernateAccountRepository.findAccountHibernateEntityByAccountNumber(AN_ACCOUNT_NUMBER.getValue())).thenReturn(this.accountHibernateEntity);
    when(this.accountHibernateEntityAssembler.from(this.account)).thenReturn(this.accountHibernateEntity);
    when(this.accountHibernateEntityAssembler.from(this.accountHibernateEntity)).thenReturn(this.account);
  }

  @Test
  public void givenANonExistingAccountForAnInvestorId_whenVerifyingNoAccountExistsWithInvestorId_thenNoExceptionIsThrown() {
    when(this.hibernateAccountRepository.findAccountHibernateEntityByInvestorId(AN_INVESTOR_ID.getValue())).thenReturn(null);

    this.accountRepository.assertAccountDoesntExist(AN_INVESTOR_ID);
  }

  @Test(expected = AccountAlreadyOpenException.class)
  public void givenAnExistingAccountForAnInvestorId_whenVerifyingNoAccountExistsWithInvestorId_thenThrowsAccountAlreadyOpenedException() {
    this.accountRepository.assertAccountDoesntExist(AN_INVESTOR_ID);
  }

  @Test
  public void givenASavedAccount_whenFindingAnAccountByAccountNumber_thenTheAccountIsReturned() {
    assertEquals(this.account, this.accountRepository.findByAccountNumber(AN_ACCOUNT_NUMBER));
  }

  @Test(expected = AccountNotFoundException.class)
  public void givenNonExistingAccount_whenFindingByAccountNumber_thenThrowAccountNotFoundException() {
    when(this.hibernateAccountRepository.findAccountHibernateEntityByAccountNumber(AN_ACCOUNT_NUMBER.getValue())).thenReturn(null);

    this.accountRepository.findByAccountNumber(AN_ACCOUNT_NUMBER);
  }

  @Test
  public void whenSavingAnAccount_thenTheAccountIsSavedIntoTheHibernateEntity() {
    this.accountRepository.save(this.account);

    verify(this.hibernateAccountRepository, times(1)).save(this.accountHibernateEntity);
  }

  @Test
  public void givenAnExistingAccountNumber_whenEnsuringAccountExistence_thenNoExceptionsThrown() {
    when(this.hibernateAccountRepository.existsAccountHibernateEntityByAccountNumber(AN_ACCOUNT_NUMBER.getValue())).thenReturn(true);
    this.accountRepository.exists(AN_ACCOUNT_NUMBER);
  }

  @Test(expected = AccountNotFoundException.class)
  public void givenANotExistingAccountNumber_whenEnsuringAccountExistence_thenAccountNotFoundExceptionThrown() {
    when(this.hibernateAccountRepository.existsAccountHibernateEntityByAccountNumber(AN_ACCOUNT_NUMBER.getValue())).thenReturn(false);
    this.accountRepository.exists(AN_ACCOUNT_NUMBER);
  }

}