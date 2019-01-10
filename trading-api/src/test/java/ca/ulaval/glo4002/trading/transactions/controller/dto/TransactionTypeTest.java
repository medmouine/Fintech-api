package ca.ulaval.glo4002.trading.transactions.controller.dto;

import org.junit.Assert;
import org.junit.Test;

public class TransactionTypeTest {
  public static String BUY = TransactionType.BUY;
  public static String SELL = TransactionType.SELL;
  public static String OTHER = "RANDOM_STRING";

  @Test
  public void givenABuyTransactionType_thenItShouldBeAValidType() {
    Assert.assertTrue(TransactionType.isValidTransactionType(BUY));
  }


  @Test
  public void givenASellTransactionType_thenItShouldBeAValidType() {
    Assert.assertTrue(TransactionType.isValidTransactionType(SELL));
  }


  @Test
  public void givenANonValidTransactionType_thenItShouldBeAnInvalidType() {
    Assert.assertFalse(TransactionType.isValidTransactionType(OTHER));
  }
}
