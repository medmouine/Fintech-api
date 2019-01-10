package ca.ulaval.glo4002.trading.transactions.buyTransactions;


import ca.ulaval.glo4002.trading.TradingSpringApplication;
import ca.ulaval.glo4002.trading.accounts.controller.dto.AccountCreationRequestDTO;
import ca.ulaval.glo4002.trading.accounts.controller.dto.CreditDTO;
import ca.ulaval.glo4002.trading.markets.domain.MarketDetailsRepository;
import ca.ulaval.glo4002.trading.markets.domain.response.MarketDetailsResponse;
import ca.ulaval.glo4002.trading.stocks.controller.dto.StockDTO;
import ca.ulaval.glo4002.trading.stocks.infrastructure.repository.StocksAPIRepository;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TradingSpringApplication.class)
@AutoConfigureMockMvc
@JsonInclude(JsonInclude.Include.NON_NULL)
@AutoConfigureTestEntityManager
@AutoConfigureDataJpa
@Transactional
public class BuyTransactionsITTest {
  public static final String TIME_ZONE = "UTC-00:00";
  public static final String LOCATION = "location";
  public static final int PRICE = 100;
  private static final UUID TRANSACTION_NUMBER = UUID.randomUUID();
  private static final List<String> OPENHOURS_LIST = Arrays.asList("8:00-12:00");
  private static final String NEWLY_CREATED_ACCOUNT_NUMBER = "JD-1";
  private static final long INVESTOR_ID = 1234L;
  private static final float AMOUNT_IN_CAD = 13100F;
  private static final String USD = "USD";
  private static final float AMOUNT = 10000F;
  private static final CreditDTO SOME_CREDIT_DTO = new CreditDTO(AMOUNT, USD);
  private static final CreditDTO INVALID_CREDIT_DTO = new CreditDTO(-1, USD);
  private static final List<CreditDTO> VALID_CREDIT_DTO_LIST = Arrays.asList(SOME_CREDIT_DTO);
  private static final ObjectMapper mapper = new ObjectMapper();
  private static final AccountCreationRequestDTO ACCOUNT_CREATION_REQUEST_DTO = new AccountCreationRequestDTO(INVESTOR_ID,
          "John Doe",
          "jd@test.ca",
          VALID_CREDIT_DTO_LIST);
  private static final String ACCOUNT_NUMBER = "JD-1";
  private static final String ACCOUNTS_URL = "/accounts";
  private static final String TRANSACTION_URL = "/transactions";
  private static final String BUY = "BUY";
  private static final String STOCK_SYMBOL = "GOOG";
  private static final String MARKET = "NASDAQ";
  private static final StockDTO STOCK_DTO = new StockDTO(MARKET, STOCK_SYMBOL);
  private static final long QUANTITY = 5L;
  private static final String SELL = "SELL";
  private static final String DATE_STRING = "2018-08-21T11:23:20.142Z";
  private final TransactionRequestDTO BUY_TRANSACTION_CREATION_REQUEST_DTO = new TransactionRequestDTO(BUY, DATE_STRING, STOCK_DTO, TRANSACTION_NUMBER, QUANTITY);
  @Autowired
  private TestEntityManager testEntityManager;
  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private StocksAPIRepository stocksAPIRepository;
  @MockBean
  private MarketDetailsRepository marketDetailsRepository;


  @Before
  public void setup() {
    this.testEntityManager.flush();
    when(this.marketDetailsRepository.findMarketDetailsBySymbol(MARKET)).thenReturn(MarketDetailsResponse.of(TIME_ZONE, OPENHOURS_LIST));
    when(this.stocksAPIRepository.findPriceBy(any(), any())).thenReturn(Money.of(PRICE, USD));
  }

  @Test
  public void givenNoAccountOpened_whenBuyTransactionSubmitted_thenAccountNotFoundErrorReturned() throws Exception {
    this.mockMvc
            .perform(post(ACCOUNTS_URL + ACCOUNT_NUMBER + TRANSACTION_URL)
                    .contentType(APPLICATION_JSON)
                    .content(mapper.writeValueAsBytes(this.BUY_TRANSACTION_CREATION_REQUEST_DTO)))
            .andExpect(status().isNotFound())
            .andReturn();
  }

  @Test
  public void givenAccountOpened_whenBuyTransactionSubmitted_thenSuccess() throws Exception {
    this.mockMvc
            .perform(post(ACCOUNTS_URL)
                    .contentType(APPLICATION_JSON)
                    .content(mapper.writeValueAsBytes(ACCOUNT_CREATION_REQUEST_DTO)))
            .andExpect(status().isCreated())
            .andExpect(header().string(LOCATION, ACCOUNTS_URL + "/" + NEWLY_CREATED_ACCOUNT_NUMBER));

    this.mockMvc
            .perform(post(ACCOUNTS_URL + "/" + ACCOUNT_NUMBER + TRANSACTION_URL)
                    .contentType(APPLICATION_JSON)
                    .content(mapper.writeValueAsBytes(this.BUY_TRANSACTION_CREATION_REQUEST_DTO)))
            .andExpect(status().isCreated());
  }

  @Getter
  @AllArgsConstructor
  private class TransactionRequestDTO {
    private final String type;
    private final String date;
    private final StockDTO stock;
    private final UUID transactionNumber;
    private final long quantity;
  }
}
