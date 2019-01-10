package ca.ulaval.glo4002.trading.transactions.controller.dto;

public class TransactionType {
  public static final String BUY = "BUY";
  public static final String SELL = "SELL";

  public static boolean isValidTransactionType(final String transactionType) {
    return BUY.equals(transactionType) || SELL.equals(transactionType);
  }
}
