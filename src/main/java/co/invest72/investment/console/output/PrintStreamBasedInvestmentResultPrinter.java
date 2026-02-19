package co.invest72.investment.console.output;

import java.io.PrintStream;
import java.util.List;

import co.invest72.investment.presentation.response.MonthlyInvestmentResult;

public class PrintStreamBasedInvestmentResultPrinter implements InvestmentResultPrinter {

	private final PrintStream out;

	public PrintStreamBasedInvestmentResultPrinter(PrintStream out) {
		this.out = out;
	}

	@Override
	public void printTotalPrincipal(int amount) {
		out.println("원금 합계: " + formattedAmount(amount) + "원");
	}

	private String formattedAmount(int amount) {
		return String.format("%,d", amount);
	}

	@Override
	public void printInterest(int amount) {
		out.println("세전 이자: " + formattedAmount(amount) + "원");
	}

	@Override
	public void printTax(int amount) {
		if (amount > 0) {
			amount = -amount; // Tax is shown as a negative value
		}
		String formatted = String.format("이자 과세: %s원", formattedAmount(amount));
		out.println(formatted);
	}

	@Override
	public void printTotalProfit(int amount) {
		out.println("세후 수령액: " + formattedAmount(amount) + "원");
	}

	@Override
	public void printMonthlyInvestmentResults(
		List<MonthlyInvestmentResult> monthlyInvestmentResults) {
		if (monthlyInvestmentResults.isEmpty()) {
			out.println("월별 투자 결과가 없습니다.");
			return;
		}

		out.println("월별 투자 결과:");
		for (MonthlyInvestmentResult result : monthlyInvestmentResults) {
			String formatted = String.format(
				"월: %d, 원금(누적): %s원, 이자(누적): %s원, 총 수익 금액: %s원",
				result.getMonth(),
				formattedAmount(result.getPrincipal()),
				formattedAmount(result.getInterest()),
				formattedAmount(result.getProfit())
			);
			out.println(formatted);
		}
	}
}
