package co.invest72.investment.console;

import java.io.IOException;
import java.io.PrintStream;

import co.invest72.investment.application.CalculateInvestment;
import co.invest72.investment.application.InvestmentFactory;
import co.invest72.investment.console.input.delegator.CalculateExpirationInvestmentReaderDelegator;
import co.invest72.investment.console.output.InvestmentResultPrinter;
import co.invest72.investment.domain.Investment;
import co.invest72.investment.presentation.request.CalculateInvestmentRequest;
import co.invest72.investment.presentation.response.CalculateMonthlyInvestmentResponse;

public class CalculateExpirationInvestmentConsoleRunner {
	private final PrintStream err;
	private final CalculateExpirationInvestmentReaderDelegator delegator;
	private final InvestmentResultPrinter printer;
	private final CalculateInvestment useCase;
	private final InvestmentFactory investmentFactory;

	public CalculateExpirationInvestmentConsoleRunner(
		PrintStream err,
		CalculateExpirationInvestmentReaderDelegator delegator,
		InvestmentResultPrinter printer,
		CalculateInvestment useCase,
		InvestmentFactory investmentFactory) {
		this.err = err;
		this.delegator = delegator;
		this.printer = printer;
		this.useCase = useCase;
		this.investmentFactory = investmentFactory;
	}

	public void run() {
		try {
			// 입력받기
			CalculateInvestmentRequest request = delegator.readRequest();

			// Investment 생성
			Investment investment = investmentFactory.createBy(request);

			// 계산 요청
			CalculateMonthlyInvestmentResponse response = useCase.calMonthlyInvestment(investment);

			// 출력
			printer.printTotalPrincipal(response.getTotalPrincipal());
			printer.printInterest(response.getTotalInterest());
			printer.printTax(response.getTotalTax());
			printer.printTotalProfit(response.getTotalProfit());

		} catch (IOException | IllegalArgumentException e) {
			err.println("[ERROR] Input Error: " + e.getMessage());
		}
	}
}
