package ca.ulaval.glo4002.trading.report.controller.dto;

import ca.ulaval.glo4002.trading.common.controller.dto.Validator;
import ca.ulaval.glo4002.trading.common.controller.dto.errors.APIErrorDTO;
import ca.ulaval.glo4002.trading.report.controller.dto.errors.InvalidReportDateError;
import ca.ulaval.glo4002.trading.report.controller.dto.errors.MissingDateError;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class DailyReportRequestDTOValidator implements Validator<DailyTransactionReportRequestDTO> {
  @Override
  public List<APIErrorDTO> findErrors(final DailyTransactionReportRequestDTO dailyTransactionReportRequestDTO) {
    final List<APIErrorDTO> errors = new ArrayList<>();
    this.findDateErrors(dailyTransactionReportRequestDTO.getDate(), errors);
    return errors;
  }

  private void findDateErrors(final String date, final List<APIErrorDTO> errors) {
    if (StringUtils.isEmpty(date)) {
      errors.add(new MissingDateError());
    } else {
      this.findInvalidDateErrors(date, errors);
    }
  }

  private void findInvalidDateErrors(final String date, final List<APIErrorDTO> errors) {
    try {
      final LocalDate parsedDate = LocalDate.parse(date);
      if (this.isTodayOrAfter(parsedDate)) {
        errors.add(new InvalidReportDateError(date));
      }
    } catch (final Exception e) {
      errors.add(new InvalidReportDateError(date));
    }
  }

  private boolean isTodayOrAfter(final LocalDate date) {
    final LocalDate today = LocalDate.now();
    return date.equals(today) || date.isAfter(today);
  }
}
