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

public class CalculateMonthlyInvestmentConsoleRunner {
	private final PrintStream err;
	private final CalculateExpirationInvestmentReaderDelegator delegator;
	private final InvestmentResultPrinter printer;
	private final CalculateInvestment useCase;
	private final InvestmentFactory factory;

	public CalculateMonthlyInvestmentConsoleRunner(PrintStream err,
		CalculateExpirationInvestmentReaderDelegator delegator, InvestmentResultPrinter printer,
		CalculateInvestment useCase, InvestmentFactory factory) {
		this.err = err;
		this.delegator = delegator;
		this.printer = printer;
		this.useCase = useCase;
		this.factory = factory;
	}

	public void run() {
		try {
			// 입력받기
			CalculateInvestmentRequest request = delegator.readRequest();

			// Investment 객체 생성
			Investment investment = factory.createBy(request);

			// 계산 요청
			CalculateMonthlyInvestmentResponse response = useCase.calMonthlyInvestmentAmount(investment);

			// 출력
			printer.printMonthlyInvestmentResults(response.getDetails());

		} catch (IOException | IllegalArgumentException e) {
			err.println("[ERROR] Input Error: " + e.getMessage());
		}
	}
}
