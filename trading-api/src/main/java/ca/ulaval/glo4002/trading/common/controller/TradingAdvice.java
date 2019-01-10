package ca.ulaval.glo4002.trading.common.controller;

import ca.ulaval.glo4002.trading.common.controller.dto.errors.APIErrorDTO;
import ca.ulaval.glo4002.trading.common.exceptions.TradingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

public class TradingAdvice {

  protected ResponseEntity<List<APIErrorDTO>> handleTradingException(final TradingException ex, final HttpStatus status) {
    APIErrorDTO apiErrorDTO = new APIErrorDTO(ex.getError(), ex.getDescription());
    List<APIErrorDTO> apiErrorDTOList = Collections.singletonList(apiErrorDTO);
    return new ResponseEntity<>(apiErrorDTOList, status);
  }

}
