package ca.ulaval.glo4002.trading.common.infrastructure.currencyConverter;

import ca.ulaval.glo4002.trading.common.infrastructure.currencyConverter.exceptions.InvalidCurrencyException;
import org.javamoney.moneta.Money;

import javax.money.CurrencyUnit;

public interface CurrencyConverter {

  Money convertToCAD(Money money);

  Money convertFromCAD(Money money) throws InvalidCurrencyException;

  Money convertTo(Money money, CurrencyUnit currencyUnit);

  Money convertFrom(Money money);
}
