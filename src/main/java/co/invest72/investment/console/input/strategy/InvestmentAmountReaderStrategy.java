package co.invest72.investment.console.input.strategy;

import java.io.IOException;

import co.invest72.investment.console.input.reader.InvestmentAmountReader;

public interface InvestmentAmountReaderStrategy {
	String readAmount(InvestmentAmountReader reader) throws IOException;
}
