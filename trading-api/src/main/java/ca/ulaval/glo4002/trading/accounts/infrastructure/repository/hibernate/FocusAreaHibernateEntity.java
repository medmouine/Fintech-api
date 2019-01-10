package ca.ulaval.glo4002.trading.accounts.infrastructure.repository.hibernate;

import ca.ulaval.glo4002.trading.accounts.domain.investorProfile.FocusArea;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class FocusAreaHibernateEntity {
  @GeneratedValue
  @Id
  private long id;

  protected static FocusAreaHibernateEntity from(final FocusArea focusArea) {
    return new FocusAreaHibernateEntity();
  }

  protected static FocusArea from(final FocusAreaHibernateEntity focusAreaEntity) {
    return new FocusArea();
  }
}