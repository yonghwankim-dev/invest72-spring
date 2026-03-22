package co.invest72.investment.console.input.delegator;

import java.io.IOException;

import co.invest72.investment.console.input.reader.CalculateInvestmentRequestReader;
import co.invest72.investment.console.input.registry.InvestmentAmountReaderStrategyRegistry;
import co.invest72.investment.domain.investment.InvestmentType;
import co.invest72.investment.presentation.request.CalculateInvestmentRequest;

public class CalculateExpirationInvestmentReaderDelegator {
	private final InvestmentAmountReaderStrategyRegistry registry;
	private final CalculateInvestmentRequestReader reader;

	public CalculateExpirationInvestmentReaderDelegator(
		InvestmentAmountReaderStrategyRegistry registry,
		CalculateInvestmentRequestReader reader) {
		this.registry = registry;
		this.reader = reader;
	}

	public CalculateInvestmentRequest readRequest() throws IOException {
		String investmentType = reader.readInvestmentType();
		String investmentAmount = readInvestmentAmount(investmentType);
		String amountType = investmentAmount.split(" ")[0];
		int amount = Integer.parseInt(investmentAmount.split(" ")[1]);
		String periodType = reader.readPeriodType();
		int periodValue = reader.readPeriod();
		String interestType = reader.readInterestType();
		double annualInterestRate = reader.readInterestRate();
		String taxType = reader.readTaxType();
		double taxRate = reader.readTaxRate();
		String currency = reader.readCurrency();

		return CalculateInvestmentRequest.builder()
			.type(investmentType)
			.amountType(amountType)
			.amount(amount)
			.periodType(periodType)
			.periodValue(periodValue)
			.interestType(interestType)
			.annualInterestRate(annualInterestRate)
			.taxType(taxType)
			.taxRate(taxRate)
			.currencyCode(currency)
			.build();
	}

	private String readInvestmentAmount(String investmentType) throws IOException {
		InvestmentType type = InvestmentType.from(investmentType);
		return registry.getStrategy(type).readAmount(reader);
	}
}
