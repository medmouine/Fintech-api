package ca.ulaval.glo4002.trading.report.controller.dto;

import ca.ulaval.glo4002.trading.common.controller.dto.errors.APIErrorDTO;
import ca.ulaval.glo4002.trading.report.controller.dto.errors.InvalidReportDateError;
import ca.ulaval.glo4002.trading.report.controller.dto.errors.MissingDateError;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class DailyDailyTransactionReportRequestDTOValidatorTest {

  private static final String AN_ACCOUNT_NUMBER_STRING = "SP-1";

  private static final String DATE_IN_PAST = LocalDate.now().minusDays(1).toString();
  private static final String DATE_IN_FUTURE = LocalDate.now().plusDays(1).toString();
  private static final String INVALID_DATE_FORMAT = "2018-245-12";

  private static final APIErrorDTO INVALID_DATE_ERROR = new InvalidReportDateError(DATE_IN_FUTURE);
  private static final APIErrorDTO INVALID_DATE_FORMAT_ERROR = new InvalidReportDateError(INVALID_DATE_FORMAT);
  private static final APIErrorDTO MISSING_DATE_ERROR = new MissingDateError();

  @InjectMocks
  private DailyReportRequestDTOValidator dailyReportRequestDTOValidator;

  private DailyTransactionReportRequestDTO dailyTransactionReportRequestDTO;

  @Before
  public void Setup() {
    this.dailyTransactionReportRequestDTO = new DailyTransactionReportRequestDTO(DATE_IN_PAST, AN_ACCOUNT_NUMBER_STRING);
  }

  @Test
  public void givenAReportWithNoError_whenFindingErrors_thenEmptyListIsReturned() {
    final List<APIErrorDTO> errors = this.dailyReportRequestDTOValidator.findErrors(this.dailyTransactionReportRequestDTO);

    assertEquals(Collections.emptyList(), errors);
  }

  @Test
  public void givenAnInvalidDateFormat_whenFindingErrors_thenInvalidReportDateErrorReturned() {
    this.dailyTransactionReportRequestDTO = new DailyTransactionReportRequestDTO(INVALID_DATE_FORMAT, AN_ACCOUNT_NUMBER_STRING);

    final List<APIErrorDTO> errors = this.dailyReportRequestDTOValidator.findErrors(this.dailyTransactionReportRequestDTO);
    assertEquals(INVALID_DATE_FORMAT_ERROR.getError(), errors.get(0).getError());
  }

  @Test
  public void givenADateInTheFuture_whenFindingErrors_thenInvalidReportDateErrorReturned() {
    this.dailyTransactionReportRequestDTO = new DailyTransactionReportRequestDTO(DATE_IN_FUTURE, AN_ACCOUNT_NUMBER_STRING);

    final List<APIErrorDTO> errors = this.dailyReportRequestDTOValidator.findErrors(this.dailyTransactionReportRequestDTO);
    assertEquals(INVALID_DATE_ERROR.getError(), errors.get(0).getError());
  }

  @Test
  public void givenNoDate_whenFindingErrors_thenMissingDateErrorReturned() {
    this.dailyTransactionReportRequestDTO = new DailyTransactionReportRequestDTO(null, AN_ACCOUNT_NUMBER_STRING);

    final List<APIErrorDTO> errors = this.dailyReportRequestDTOValidator.findErrors(this.dailyTransactionReportRequestDTO);
    assertEquals(MISSING_DATE_ERROR.getError(), errors.get(0).getError());
  }

  @Test
  public void givenAnEmptyStringDate_whenFindingErrors_thenMissingDateErrorReturned() {
    this.dailyTransactionReportRequestDTO = new DailyTransactionReportRequestDTO("", AN_ACCOUNT_NUMBER_STRING);

    final List<APIErrorDTO> errors = this.dailyReportRequestDTOValidator.findErrors(this.dailyTransactionReportRequestDTO);
    assertEquals(MISSING_DATE_ERROR.getError(), errors.get(0).getError());
  }
}