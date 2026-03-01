package co.invest72.investment.console.output;

import java.math.BigDecimal;
import java.util.List;

import co.invest72.investment.presentation.response.MonthlyInvestmentResult;

public interface InvestmentResultPrinter {
	void printTotalPrincipal(BigDecimal amount);

	void printInterest(BigDecimal amount);

	void printTax(BigDecimal amount);

	void printTotalProfit(BigDecimal amount);

	void printMonthlyInvestmentResults(List<MonthlyInvestmentResult> monthlyInvestmentResults);
}
