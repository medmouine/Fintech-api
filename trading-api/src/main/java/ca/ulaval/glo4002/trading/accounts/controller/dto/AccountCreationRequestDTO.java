package ca.ulaval.glo4002.trading.accounts.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountCreationRequestDTO {

  private long investorId;

  private String investorName;

  private String email;

  private List<CreditDTO> credits;
}
