package co.invest72.investment.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import co.invest72.investment.domain.Investment;
import co.invest72.investment.presentation.response.CalculateInvestmentResponse;
import co.invest72.investment.presentation.response.CalculateMonthlyInvestmentResponse;
import co.invest72.investment.presentation.response.CalculateYearlyInvestmentResponse;
import co.invest72.investment.presentation.response.MonthlyInvestmentResult;
import co.invest72.investment.presentation.response.YearlyInvestmentResult;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CalculateInvestment {
	private final TaxFormatter taxFormatter;

	public CalculateInvestmentResponse calculate(Investment investment) {
		List<MonthlyInvestmentResult> monthlyDetails = getMonthlyInvestmentResults(investment);
		List<YearlyInvestmentResult> yearlyDetails = getYearlyInvestmentResults(investment);
		BigDecimal totalInvestment = investment.getTotalInvestment().getValue();
		BigDecimal totalInterest = investment.getTotalInterest().getValue();
		BigDecimal totalTax = investment.getTotalTax().getValue();
		BigDecimal totalProfit = investment.getTotalProfit().getValue();
		String taxType = investment.getTaxType();
		String taxPercent = taxFormatter.format(investment.getTaxRate());

		return CalculateInvestmentResponse.builder()
			.totalInvestment(totalInvestment)
			.totalInterest(totalInterest)
			.totalTax(totalTax)
			.totalProfit(totalProfit)
			.taxType(taxType)
			.taxPercent(taxPercent)
			.monthlyDetails(monthlyDetails)
			.yearlyDetails(yearlyDetails)
			.build();
	}

	public CalculateMonthlyInvestmentResponse calMonthlyInvestment(Investment investment) {
		List<MonthlyInvestmentResult> result = getMonthlyInvestmentResults(investment);
		BigDecimal totalInvestment = investment.getTotalInvestment().getValue();
		BigDecimal totalInterest = investment.getTotalInterest().getValue();
		BigDecimal totalTax = investment.getTotalTax().getValue();
		BigDecimal totalProfit = investment.getTotalProfit().getValue();
		String taxType = investment.getTaxType();
		String taxPercent = taxFormatter.format(investment.getTaxRate());
		return CalculateMonthlyInvestmentResponse.builder()
			.totalInvestment(totalInvestment)
			.totalInterest(totalInterest)
			.totalTax(totalTax)
			.totalProfit(totalProfit)
			.taxType(taxType)
			.taxPercent(taxPercent)
			.details(result)
			.build();
	}

	private static List<MonthlyInvestmentResult> getMonthlyInvestmentResults(Investment investment) {
		List<MonthlyInvestmentResult> result = new ArrayList<>();

		for (int month = 1; month <= investment.getFinalMonth(); month++) {
			result.add(new MonthlyInvestmentResult(
				month,
				investment.getPrincipal(month).getValue(),
				investment.getInterest(month).getValue(),
				investment.getProfit(month).getValue()
			));
		}
		return result;
	}

	public CalculateYearlyInvestmentResponse calYearlyInvestment(Investment investment) {
		List<YearlyInvestmentResult> details = getYearlyInvestmentResults(investment);
		BigDecimal totalInvestment = investment.getTotalInvestment().getValue();
		BigDecimal totalInterest = investment.getTotalInterest().getValue();
		BigDecimal totalTax = investment.getTotalTax().getValue();
		BigDecimal totalProfit = investment.getTotalProfit().getValue();
		String taxType = investment.getTaxType();
		String taxPercent = taxFormatter.format(investment.getTaxRate());
		return CalculateYearlyInvestmentResponse.builder()
			.totalInvestment(totalInvestment)
			.totalInterest(totalInterest)
			.totalTax(totalTax)
			.totalProfit(totalProfit)
			.taxType(taxType)
			.taxPercent(taxPercent)
			.details(details)
			.build();
	}

	private static List<YearlyInvestmentResult> getYearlyInvestmentResults(Investment investment) {
		List<YearlyInvestmentResult> details = new ArrayList<>();

		int years = (investment.getFinalMonth() - 1) / 12 + 1;
		for (int year = 1; year <= years; year++) {
			BigDecimal principal = investment.getPrincipalForYear(year).getValue();
			BigDecimal interest = investment.getInterestForYear(year);
			BigDecimal profit = investment.getProfitForYear(year);
			details.add(new YearlyInvestmentResult(year, principal, interest, profit));
		}
		return details;
	}

}
