package ca.ulaval.glo4002.trading.accounts.infrastructure.repository.hibernate;

import lombok.Getter;
import org.javamoney.moneta.Money;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
@Getter
public class CreditsHibernateEntity {
  @Id
  @GeneratedValue
  protected long id;
  @Column
  BigDecimal amount;
  @Column
  String currency;

  protected static CreditsHibernateEntity from(final Money credit) {
    final CreditsHibernateEntity creditsHibernateEntity = new CreditsHibernateEntity();
    creditsHibernateEntity.amount = credit.getNumberStripped();
    creditsHibernateEntity.currency = credit.getCurrency().getCurrencyCode();
    return creditsHibernateEntity;
  }
}
