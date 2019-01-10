package ca.ulaval.glo4002.trading.accounts.domain;

import ca.ulaval.glo4002.trading.transactions.domain.Transaction;
import org.javamoney.moneta.Money;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class AccountTest {

  private static final String CAD = "CAD";
  private static final float AMOUNT = 15.5F;
  private static final Money SOME_MONEY = Money.of(AMOUNT, CAD);
  private static final List<Money> CREDITS_LIST = Arrays.asList(SOME_MONEY);

  @Mock
  private Transaction transaction;
  @Mock
  private StockWallet stockWallet;
  @InjectMocks
  private Account account;

  @Test
  public void whenPerformingTransaction_thenShouldExecuteTheTransaction() {
    when(this.stockWallet.getCredits()).thenReturn(CREDITS_LIST);

    this.account.performTransaction(this.transaction);

    verify(this.transaction, times(1)).execute(this.stockWallet);
  }
}