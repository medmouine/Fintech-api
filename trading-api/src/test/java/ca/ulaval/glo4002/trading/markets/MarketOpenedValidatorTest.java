package ca.ulaval.glo4002.trading.markets;

import ca.ulaval.glo4002.trading.markets.domain.Market;
import ca.ulaval.glo4002.trading.markets.domain.MarketDetailsRepository;
import ca.ulaval.glo4002.trading.markets.domain.MarketOpenedValidator;
import ca.ulaval.glo4002.trading.markets.domain.response.MarketDetailsResponse;
import ca.ulaval.glo4002.trading.markets.infrastructure.repository.exceptions.MarketDetailsNotFoundExceptions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MarketOpenedValidatorTest {

  private static final Instant CLOSED_TIME = Instant.parse("2018-11-07T19:01:02.000Z");
  private static final Instant OPENED_TIME = Instant.parse("2018-11-07T12:00:00.000Z");
  private static final Instant WEEKEND_DATE = Instant.parse("2018-11-10T12:00:00.000Z");
  private static final Market A_MARKET = Market.NASDAQ;
  private static final List<String> OPEN_HOURS_LIST = Arrays.asList("08:00-16:59");
  private static final MarketDetailsResponse MARKET_DETAILS_RESPONSE = MarketDetailsResponse.of("UTC-01:00", OPEN_HOURS_LIST);

  @Mock
  private MarketDetailsRepository marketDetailsRepository;
  @Mock
  private MarketDetailsNotFoundExceptions marketDetailsNotFoundExceptions;

  @InjectMocks
  private MarketOpenedValidator marketOpenedValidator;

  @Before
  public void setUp() {
    when(this.marketDetailsRepository.findMarketDetailsBySymbol(A_MARKET.toString())).thenReturn(MARKET_DETAILS_RESPONSE);
  }

  @Test
  public void givenClosedMarketTime_whenValidatingMarketIsOpen_thenReturnFalse() {
    assertFalse(this.marketOpenedValidator.validateAtDate(A_MARKET, CLOSED_TIME));
  }

  @Test
  public void givenOpenedTimeMarket_whenValidatingMarketIsOpen_thenReturnTrue() {
    assertTrue(this.marketOpenedValidator.validateAtDate(A_MARKET, OPENED_TIME));
  }

  @Test
  public void givenWeekEndDate_whenValidatingMarketIsOpen_thenReturnFalse() {
    assertFalse(this.marketOpenedValidator.validateAtDate(A_MARKET, WEEKEND_DATE));
  }

  @Test
  public void givenNoMArketDetails_whenValidationMarketIsOpen_ThenReturnFalse() {
    when(this.marketDetailsRepository.findMarketDetailsBySymbol(A_MARKET.toString())).thenThrow(this.marketDetailsNotFoundExceptions);
    assertFalse(this.marketOpenedValidator.validateAtDate(A_MARKET, WEEKEND_DATE));
  }
}