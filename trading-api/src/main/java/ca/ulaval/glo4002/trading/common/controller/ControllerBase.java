package ca.ulaval.glo4002.trading.common.controller;

import ca.ulaval.glo4002.trading.common.controller.dto.errors.APIErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ControllerBase {
  protected ResponseEntity<List<APIErrorDTO>> buildValidationErrorResponse(final List<APIErrorDTO> errors) {
    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }
}
