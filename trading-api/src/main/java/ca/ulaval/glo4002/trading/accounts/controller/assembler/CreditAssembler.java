package ca.ulaval.glo4002.trading.accounts.controller.assembler;

import ca.ulaval.glo4002.trading.accounts.controller.dto.CreditDTO;
import org.javamoney.moneta.Money;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CreditAssembler {
  public CreditDTO toDTO(final Money credit) {
    return new CreditDTO(credit.getNumberStripped().floatValue(), credit.getCurrency().getCurrencyCode());
  }

  public List<CreditDTO> ToDTOList(final List<Money> credits) {
    return credits.stream().map(this::toDTO).collect(Collectors.toList());
  }
}
