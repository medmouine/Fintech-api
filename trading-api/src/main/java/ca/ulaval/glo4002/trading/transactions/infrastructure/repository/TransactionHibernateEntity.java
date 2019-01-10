package ca.ulaval.glo4002.trading.transactions.infrastructure.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TransactionHibernateEntity {
  @Id
  private UUID transactionNumber;
  @Column
  private String accountNumber;
  @Column
  private Instant date;
  @Column
  private long quantity;
  @Column
  private BigDecimal stockUnitPrice;
  @Column
  private BigDecimal fees;
  @Column
  private String stockMarket;
  @Column
  private String stockSymbol;
  @Column
  private String type;
  @Column
  private UUID associatedBuyTransactionNumber;
}
