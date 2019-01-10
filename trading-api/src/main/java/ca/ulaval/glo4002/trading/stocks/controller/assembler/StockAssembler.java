package ca.ulaval.glo4002.trading.stocks.controller.assembler;

import ca.ulaval.glo4002.trading.markets.domain.Market;
import ca.ulaval.glo4002.trading.stocks.controller.dto.StockDTO;
import ca.ulaval.glo4002.trading.stocks.domain.Stock;
import org.springframework.stereotype.Component;

@Component
public class StockAssembler {
  public Stock toEntity(final StockDTO stockDTO) {
    final Stock stock = new Stock(Market.valueOf(stockDTO.getMarket()), stockDTO.getSymbol());
    return stock;
  }

  public StockDTO toDTO(final Stock stockEntity) {
    final StockDTO stockDTO = new StockDTO(stockEntity.getMarket().toString(), stockEntity.getSymbol());
    return stockDTO;
  }
}
