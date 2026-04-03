package co.invest72.investment.domain.investment.factory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import co.invest72.investment.domain.InterestRate;
import co.invest72.investment.domain.InvestPeriod;
import co.invest72.investment.domain.InvestmentAmount;
import co.invest72.investment.domain.investment.InterestCalculator;
import co.invest72.investment.domain.investment.InvestmentDetail;
import co.invest72.money.domain.Money;
import lombok.Builder;

@Builder(toBuilder = true)
public class FixedDepositMonthlyDetailFactory {
	private final InvestmentAmount investmentAmount;
	private final InterestRate interestRate;
	private final InvestPeriod investPeriod;
	private final InterestCalculator interestCalculator;

	public FixedDepositMonthlyDetailFactory(InvestmentAmount investmentAmount, InterestRate interestRate,
		InvestPeriod investPeriod, InterestCalculator interestCalculator) {
		this.investmentAmount = investmentAmount;
		this.interestRate = interestRate;
		this.investPeriod = investPeriod;
		this.interestCalculator = interestCalculator;
	}

	public List<InvestmentDetail> createDetails() {
		List<InvestmentDetail> result = new ArrayList<>();

		Money originalAmount = investmentAmount.getAmount();
		Money principal = originalAmount;
		Money interest = principal.times(BigDecimal.ZERO);
		Money profit = originalAmount;
		result.add(new InvestmentDetail(0, principal, interest, profit));

		for (int i = 1; i <= investPeriod.getMonths(); i++) {
			principal = profit;
			interest = interestCalculator.calculate(originalAmount, principal, interestRate);
			profit = principal.add(interest);
			result.add(new InvestmentDetail(i, principal, interest, profit));
		}
		return result;
	}
}
