package ca.ulaval.glo4002.trading.accounts.infrastructure.repository.hibernate;

import ca.ulaval.glo4002.trading.accounts.domain.investorProfile.InvestorType;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Entity
public class AccountHibernateEntity {
  @Id
  protected String accountNumber;
  @Column
  protected long investorId;
  @Enumerated(EnumType.ORDINAL)
  InvestorType investorType;
  @OneToMany
  List<FocusAreaHibernateEntity> focusAreas;
  @Column
  String investorName;
  @Column
  String email;
  @OneToMany(fetch = FetchType.LAZY, cascade = {ALL})
  List<CreditsHibernateEntity> credits;
}
