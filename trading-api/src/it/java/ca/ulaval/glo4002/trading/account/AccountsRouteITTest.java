package ca.ulaval.glo4002.trading.account;

import ca.ulaval.glo4002.trading.TradingSpringApplication;
import ca.ulaval.glo4002.trading.accounts.controller.dto.AccountCreationRequestDTO;
import ca.ulaval.glo4002.trading.accounts.controller.dto.CreditDTO;
import ca.ulaval.glo4002.trading.accounts.controller.dto.InvestorProfileDTO;
import ca.ulaval.glo4002.trading.accounts.controller.dto.errors.InvalidAmountError;
import ca.ulaval.glo4002.trading.accounts.controller.exceptions.AccountAlreadyOpenException;
import ca.ulaval.glo4002.trading.accounts.controller.response.AccountResponse;
import ca.ulaval.glo4002.trading.accounts.domain.InvestorId;
import ca.ulaval.glo4002.trading.accounts.domain.investorProfile.InvestorType;
import ca.ulaval.glo4002.trading.common.controller.dto.errors.APIErrorDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
public class AccountsRouteITTest {
  private static final String NEWLY_CREATED_ACCOUNT_NUMBER = "JD-1";
  private static final long INVESTOR_ID = 1234L;
  private static final float AMOUNT_IN_CAD = 1.31F;
  private static final String USD = "USD";
  private static final float AMOUNT = 1F;
  private static final CreditDTO SOME_CREDIT_DTO = new CreditDTO(AMOUNT, USD);
  private static final CreditDTO INVALID_CREDIT_DTO = new CreditDTO(-1, USD);
  private static final List<CreditDTO> INVALID_CREDIT_DTO_LIST = Arrays.asList(INVALID_CREDIT_DTO);
  private static final List<CreditDTO> VALID_CREDIT_DTO_LIST = Arrays.asList(SOME_CREDIT_DTO);
  private static final ObjectMapper mapper = new ObjectMapper();
  private static final AccountCreationRequestDTO ACCOUNT_CREATION_REQUEST_DTO = new AccountCreationRequestDTO(INVESTOR_ID,
          "John Doe",
          "jd@test.ca",
          VALID_CREDIT_DTO_LIST);
  private static final AccountCreationRequestDTO ACCOUNT_WITH_INVALID_CREDITS_CREATION_REQUEST_DTO = new AccountCreationRequestDTO(INVESTOR_ID,
          "John Doe",
          "jd@test.ca",
          INVALID_CREDIT_DTO_LIST);
  private static final AccountAlreadyOpenException ACCOUNT_ALREADY_OPEN_EXCEPTION = new AccountAlreadyOpenException(new InvestorId(INVESTOR_ID));
  private static final InvalidAmountError INVALID_AMOUNT_ERROR = new InvalidAmountError();
  private static final ResponseEntity<List<APIErrorDTO>> API_ERROR_DTO = new ResponseEntity<>(Arrays.asList(INVALID_AMOUNT_ERROR), HttpStatus.BAD_REQUEST);
  private static final String ACCOUNTS_URL = "/accounts";
  private static final InvestorProfileDTO INVESTOR_PROFILE_DTO = new InvestorProfileDTO(InvestorType.CONSERVATIVE, Collections.EMPTY_LIST);
  private static final AccountResponse ACCOUNT_RESPONSE = new AccountResponse(INVESTOR_ID,
          VALID_CREDIT_DTO_LIST, NEWLY_CREATED_ACCOUNT_NUMBER,
          INVESTOR_PROFILE_DTO,
          AMOUNT_IN_CAD);
  private static final double DELTA = 0.1;
  @Autowired
  private TestEntityManager testEntityManager;
  @Autowired
  private MockMvc mockMvc;

  @Before
  public void cleanDB() {
    this.testEntityManager.flush();
  }

  @Test
  public void givenAccountInformation_whenAccountCreated_thenAccountIsCreated() throws Exception {
    this.mockMvc
            .perform(post(ACCOUNTS_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsBytes(ACCOUNT_CREATION_REQUEST_DTO)))
            .andExpect(status().isCreated())
            .andExpect(header().string("location", "/accounts/" + NEWLY_CREATED_ACCOUNT_NUMBER));
  }

  @Test
  public void givenAccountInformation_whenAccountCreated_thenAccountNumberHasValidFormat() throws Exception {
    final MvcResult result = this.mockMvc
            .perform(post(ACCOUNTS_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsBytes(ACCOUNT_CREATION_REQUEST_DTO)))
            .andReturn();
    final String location = result.getResponse().getHeader("location");
    assertTrue(location.matches("(/accounts/[A-Z]+-\\d)"));
  }

  @Test
  public void givenCreatedAccount_whenGettingAccount_thenAccountIsReturnedSuccesfully() throws Exception {
    this.mockMvc.perform(post(ACCOUNTS_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsBytes(ACCOUNT_CREATION_REQUEST_DTO)));
    final MvcResult result = this.mockMvc.perform(get(ACCOUNTS_URL + "/" + NEWLY_CREATED_ACCOUNT_NUMBER))
            .andExpect(status().isOk())
            .andReturn();
    final JSONObject response = new JSONObject(result.getResponse().getContentAsString());

    assertEquals(response.getString("accountNumber"), ACCOUNT_RESPONSE.getAccountNumber());
    assertEquals(response.getLong("investorId"), ACCOUNT_RESPONSE.getInvestorId());
    assertEquals(response.getJSONObject("investorProfile").getString("type"), ACCOUNT_RESPONSE.getInvestorProfile().getType().toString());
    assertEquals(response.getJSONArray("credits").getJSONObject(0).getDouble("amount"), ACCOUNT_RESPONSE.getCredits().get(0).getAmount(), DELTA);
    assertEquals(response.getJSONArray("credits").getJSONObject(0).getString("currency"), ACCOUNT_RESPONSE.getCredits().get(0).getCurrency());
    assertEquals(response.getDouble("total"), ACCOUNT_RESPONSE.getTotal(), DELTA);
  }

  @Test
  public void givenAccountInformation_whenAccountCreated_thenAccountInvestorTypeShouldBeConservativeByDefault() throws Exception {
    this.mockMvc.perform(post(ACCOUNTS_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsBytes(ACCOUNT_CREATION_REQUEST_DTO)));
    final MvcResult result = this.mockMvc.perform(get(ACCOUNTS_URL + "/" + NEWLY_CREATED_ACCOUNT_NUMBER))
            .andReturn();
    final String responseInvestorType = new JSONObject(result.getResponse().getContentAsString()).getJSONObject("investorProfile").getString("type");
    assertEquals(responseInvestorType, InvestorType.CONSERVATIVE.toString());
  }

  @Test
  public void givenAccountAlreadyExisting_whenSameAccountCreated_thenAccountAlreadyOpenErrorReceived() throws Exception {
    this.mockMvc
            .perform(post(ACCOUNTS_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsBytes(ACCOUNT_CREATION_REQUEST_DTO)));
    final MvcResult result = this.mockMvc
            .perform(post(ACCOUNTS_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsBytes(ACCOUNT_CREATION_REQUEST_DTO)))
            .andExpect(status().isBadRequest())
            .andReturn();

    assertTrue(result.getResponse().getContentAsString().contains(ACCOUNT_ALREADY_OPEN_EXCEPTION.getError()));
    assertTrue(result.getResponse().getContentAsString().contains(ACCOUNT_ALREADY_OPEN_EXCEPTION.getDescription()));
  }

  @Test
  public void givenAccountWithInvalidCredits_whenAccountCreated_thenInvalidAmountErrorRecieved() throws Exception {
    final MvcResult result = this.mockMvc
            .perform(post(ACCOUNTS_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsBytes(ACCOUNT_WITH_INVALID_CREDITS_CREATION_REQUEST_DTO)))
            .andExpect(status().isBadRequest())
            .andReturn();
    final JSONArray response = new JSONArray(result.getResponse().getContentAsString());
    assertEquals(response.getJSONObject(0).getString("description"), INVALID_AMOUNT_ERROR.getDescription());
    assertEquals(response.getJSONObject(0).getString("error"), INVALID_AMOUNT_ERROR.getError());
  }
}
