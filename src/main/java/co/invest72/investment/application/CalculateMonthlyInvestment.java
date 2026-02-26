package co.invest72.investment.application;

import java.util.ArrayList;
import java.util.List;

import co.invest72.investment.domain.Investment;
import co.invest72.investment.presentation.request.CalculateInvestmentRequest;
import co.invest72.investment.presentation.response.CalculateMonthlyInvestmentResponse;
import co.invest72.investment.presentation.response.CalculateYearlyInvestmentResponse;
import co.invest72.investment.presentation.response.MonthlyInvestmentResult;
import co.invest72.investment.presentation.response.YearlyInvestmentResult;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CalculateMonthlyInvestment {
	private final InvestmentFactory investmentFactory;
	private final TaxFormatter taxFormatter;

	public CalculateMonthlyInvestmentResponse calMonthlyInvestmentAmount(CalculateInvestmentRequest request) {
		Investment investment = investmentFactory.createBy(request);
		return calMonthlyInvestmentAmount(investment);
	}

	public CalculateMonthlyInvestmentResponse calMonthlyInvestmentAmount(Investment investment) {
		List<MonthlyInvestmentResult> result = new ArrayList<>();

		for (int month = 1; month <= investment.getFinalMonth(); month++) {
			result.add(new MonthlyInvestmentResult(
				month,
				investment.getPrincipal(month),
				investment.getInterest(month),
				investment.getProfit(month)
			));
		}
		int totalInvestment = investment.getTotalInvestment();
		int totalPrincipal = investment.getTotalPrincipal();
		int totalInterest = investment.getTotalInterest();
		int totalTax = investment.getTotalTax();
		int totalProfit = investment.getTotalProfit();
		String taxType = investment.getTaxType();
		String taxPercent = taxFormatter.format(investment.getTaxRate());
		return CalculateMonthlyInvestmentResponse.builder()
			.totalInvestment(totalInvestment)
			.totalPrincipal(totalPrincipal)
			.totalInterest(totalInterest)
			.totalTax(totalTax)
			.totalProfit(totalProfit)
			.taxType(taxType)
			.taxPercent(taxPercent)
			.details(result)
			.build();
	}

	public CalculateYearlyInvestmentResponse calYearlyInvestmentAmount(CalculateInvestmentRequest request) {
		List<YearlyInvestmentResult> details = new ArrayList<>();
		Investment investment = investmentFactory.createBy(request);

		int years = (investment.getFinalMonth() - 1) / 12 + 1;
		for (int year = 1; year <= years; year++) {
			int principal = investment.getPrincipalForYear(year);
			int interest = investment.getInterestForYear(year);
			int profit = investment.getProfitForYear(year);
			details.add(new YearlyInvestmentResult(year, principal, interest, profit));
		}
		int totalInvestment = investment.getTotalInvestment();
		int totalPrincipal = investment.getTotalPrincipal();
		int totalInterest = investment.getTotalInterest();
		int totalTax = investment.getTotalTax();
		int totalProfit = investment.getTotalProfit();
		String taxType = investment.getTaxType();
		String taxPercent = taxFormatter.format(request.getTaxRate());
		return CalculateYearlyInvestmentResponse.builder()
			.totalInvestment(totalInvestment)
			.totalPrincipal(totalPrincipal)
			.totalInterest(totalInterest)
			.totalTax(totalTax)
			.totalProfit(totalProfit)
			.taxType(taxType)
			.taxPercent(taxPercent)
			.details(details)
			.build();
	}

}
