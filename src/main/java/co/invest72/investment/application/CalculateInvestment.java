package co.invest72.investment.application;

import java.util.ArrayList;
import java.util.List;

import co.invest72.financial_product.presentation.dto.response.ProductCurrency;
import co.invest72.investment.domain.Investment;
import co.invest72.investment.presentation.response.CalculateInvestmentResponse;
import co.invest72.investment.presentation.response.CalculateMonthlyInvestmentResponse;
import co.invest72.investment.presentation.response.CalculateYearlyInvestmentResponse;
import co.invest72.investment.presentation.response.MonthlyInvestmentResult;
import co.invest72.investment.presentation.response.YearlyInvestmentResult;
import co.invest72.money.domain.Money;
import co.invest72.money.infrastructure.MoneyMapper;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CalculateInvestment {
	private final TaxFormatter taxFormatter;
	private final MoneyMapper moneyMapper;

	public CalculateInvestmentResponse calculate(Investment investment) {
		return CalculateInvestmentResponse.builder()
			.totalInvestment(investment.getTotalInvestment().round().getValue())
			.totalInterest(investment.getTotalInterest().round().getValue())
			.totalTax(investment.getTotalTax().round().getValue())
			.totalProfit(investment.getTotalProfit().round().getValue())
			.taxType(investment.getTaxType())
			.taxPercent(taxFormatter.format(investment.getTaxRate()))
			.monthlyDetails(getMonthlyInvestmentResults(investment))
			.yearlyDetails(getYearlyInvestmentResults(investment))
			.productCurrency(ProductCurrency.from(investment.getCurrency()))
			.build();
	}

	public CalculateMonthlyInvestmentResponse calMonthlyInvestment(Investment investment) {
		List<MonthlyInvestmentResult> result = getMonthlyInvestmentResults(investment);
		Money totalInvestment = investment.getTotalInvestment().round();
		Money totalInterest = investment.getTotalInterest().round();
		Money totalTax = investment.getTotalTax().round();
		Money totalProfit = investment.getTotalProfit().round();
		String taxType = investment.getTaxType();
		String taxPercent = taxFormatter.format(investment.getTaxRate());
		return CalculateMonthlyInvestmentResponse.builder()
			.totalInvestment(moneyMapper.toBigDecimal(totalInvestment))
			.totalInterest(moneyMapper.toBigDecimal(totalInterest))
			.totalTax(moneyMapper.toBigDecimal(totalTax))
			.totalProfit(moneyMapper.toBigDecimal(totalProfit))
			.taxType(taxType)
			.taxPercent(taxPercent)
			.details(result)
			.build();
	}

	private List<MonthlyInvestmentResult> getMonthlyInvestmentResults(Investment investment) {
		List<MonthlyInvestmentResult> result = new ArrayList<>();

		for (int month = 1; month <= investment.getFinalMonth(); month++) {
			result.add(new MonthlyInvestmentResult(
				month,
				moneyMapper.toBigDecimal(investment.getPrincipal(month).round()),
				moneyMapper.toBigDecimal(investment.getInterest(month).round()),
				moneyMapper.toBigDecimal(investment.getProfit(month).round())
			));
		}
		return result;
	}

	public CalculateYearlyInvestmentResponse calYearlyInvestment(Investment investment) {
		List<YearlyInvestmentResult> details = getYearlyInvestmentResults(investment);
		Money totalInvestment = investment.getTotalInvestment().round();
		Money totalInterest = investment.getTotalInterest().round();
		Money totalTax = investment.getTotalTax().round();
		Money totalProfit = investment.getTotalProfit().round();
		String taxType = investment.getTaxType();
		String taxPercent = taxFormatter.format(investment.getTaxRate());
		return CalculateYearlyInvestmentResponse.builder()
			.totalInvestment(moneyMapper.toBigDecimal(totalInvestment))
			.totalInterest(moneyMapper.toBigDecimal(totalInterest))
			.totalTax(moneyMapper.toBigDecimal(totalTax))
			.totalProfit(moneyMapper.toBigDecimal(totalProfit))
			.taxType(taxType)
			.taxPercent(taxPercent)
			.details(details)
			.build();
	}

	private List<YearlyInvestmentResult> getYearlyInvestmentResults(Investment investment) {
		List<YearlyInvestmentResult> details = new ArrayList<>();

		int years = getFinalYears(investment);
		for (int year = 1; year <= years; year++) {
			Money principal = investment.getPrincipalForYear(year).round();
			Money interest = investment.getInterestForYear(year).round();
			Money profit = investment.getProfitForYear(year).round();
			details.add(new YearlyInvestmentResult(
				year,
				moneyMapper.toBigDecimal(principal),
				moneyMapper.toBigDecimal(interest),
				moneyMapper.toBigDecimal(profit)
			));
		}
		return details;
	}

	private int getFinalYears(Investment investment) {
		if (investment.getFinalMonth() == 0) {
			return 0;
		}
		return (investment.getFinalMonth() - 1) / 12 + 1;
	}

}
