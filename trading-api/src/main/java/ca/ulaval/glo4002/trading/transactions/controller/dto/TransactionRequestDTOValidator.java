package ca.ulaval.glo4002.trading.transactions.controller.dto;

import ca.ulaval.glo4002.trading.common.controller.dto.Validator;
import ca.ulaval.glo4002.trading.common.controller.dto.errors.APIErrorDTO;
import ca.ulaval.glo4002.trading.transactions.controller.dto.errors.InvalidQuantityError;
import ca.ulaval.glo4002.trading.transactions.controller.dto.errors.UnsupportedTransactionTypeError;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TransactionRequestDTOValidator implements Validator<TransactionRequestDTO> {

  @Override
  public List<APIErrorDTO> findErrors(final TransactionRequestDTO transactionDTO) {
    final List<APIErrorDTO> errors = new ArrayList<>();

    this.getTransactionTypeErrors(transactionDTO, errors);
    this.getInvalidStockQuantityErrors(transactionDTO, errors);

    return errors;
  }

  private void getTransactionTypeErrors(final TransactionRequestDTO transactionDTO, final List<APIErrorDTO> errors) {
    if (!TransactionType.isValidTransactionType(transactionDTO.getType())) {
      errors.add(new UnsupportedTransactionTypeError(transactionDTO.getType()));
    }
  }

  private void getInvalidStockQuantityErrors(final TransactionRequestDTO transactionRequestDTO, final List<APIErrorDTO> errors) {
    if (transactionRequestDTO.getQuantity() <= 0) {
      errors.add(new InvalidQuantityError());
    }
  }
}
