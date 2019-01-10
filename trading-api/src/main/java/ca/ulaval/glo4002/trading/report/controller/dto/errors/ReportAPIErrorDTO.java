package ca.ulaval.glo4002.trading.report.controller.dto.errors;

import ca.ulaval.glo4002.trading.common.controller.dto.errors.APIErrorDTO;
import lombok.Getter;

@Getter
public class ReportAPIErrorDTO extends APIErrorDTO {
  public ReportAPIErrorDTO(final String error, final String description) {
    super(error, description);
  }
}
