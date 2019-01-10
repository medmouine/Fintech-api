package ca.ulaval.glo4002.trading.common.controller.dto;

import ca.ulaval.glo4002.trading.common.controller.dto.errors.APIErrorDTO;

import java.util.List;

public interface Validator<T> {
  List<APIErrorDTO> findErrors(T object);
}