package ca.ulaval.glo4002.trading.stocks.controller.assembler;

import ca.ulaval.glo4002.trading.markets.domain.Market;
import ca.ulaval.glo4002.trading.stocks.controller.dto.StockDTO;
import ca.ulaval.glo4002.trading.stocks.domain.Stock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StockAssemblerTest {

  private static final String A_MARKET_SYMBOL = "marketSymbol";
  private static final Market MARKET = Market.NASDAQ;
  private static final StockDTO STOCK_DTO = new StockDTO(MARKET.toString(), A_MARKET_SYMBOL);
  private static final Stock STOCK_ENTITY = new Stock(MARKET, A_MARKET_SYMBOL);
  private StockAssembler stockAssembler;

  @Before
  public void setup() {
    this.stockAssembler = new StockAssembler();
  }

  @Test
  public void givenAStockDTO_whenConvertedToEntity_thenShouldReturnValidStockEntity() {
    final StockDTO dto = STOCK_DTO;

    final Stock entity = this.stockAssembler.toEntity(dto);

    Assert.assertEquals(dto.getMarket(), entity.getMarket().toString());
    Assert.assertEquals(dto.getSymbol(), entity.getSymbol());
  }

  @Test
  public void givenAStockEntity_whenConvertedToDTO_thenShouldReturnValidStockDTO() {
    final Stock entity = STOCK_ENTITY;

    final StockDTO dto = this.stockAssembler.toDTO(entity);

    Assert.assertEquals(entity.getMarket().toString(), dto.getMarket());
    Assert.assertEquals(entity.getSymbol(), dto.getSymbol());
  }

}