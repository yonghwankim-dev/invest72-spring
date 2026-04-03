package co.invest72.investment.domain.investment.factory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import co.invest72.investment.domain.InterestRate;
import co.invest72.investment.domain.InvestPeriod;
import co.invest72.investment.domain.InvestmentAmount;
import co.invest72.investment.domain.investment.InvestmentDetail;
import co.invest72.money.domain.Money;

public class SimpleFixedInstallmentSavingMonthlyDetailFactory {

	private final InvestmentAmount investmentAmount;
	private final InterestRate interestRate;
	private final InvestPeriod investPeriod;

	public SimpleFixedInstallmentSavingMonthlyDetailFactory(InvestmentAmount investmentAmount,
		InterestRate interestRate,
		InvestPeriod investPeriod) {
		this.investmentAmount = investmentAmount;
		this.interestRate = interestRate;
		this.investPeriod = investPeriod;
	}

	public List<InvestmentDetail> createDetails() {
		List<InvestmentDetail> result = new ArrayList<>();
		Money accInvestmentAmount = investmentAmount.getAmount().times(BigDecimal.ZERO);
		Money principal = investmentAmount.getAmount().times(BigDecimal.ZERO);
		Money interest = investmentAmount.getAmount().times(BigDecimal.ZERO);
		Money profit = investmentAmount.getAmount().times(BigDecimal.ZERO);

		result.add(new InvestmentDetail(0, principal, interest, profit));
		for (int i = 1; i <= investPeriod.getMonths(); i++) {
			accInvestmentAmount = accInvestmentAmount.add(investmentAmount.getAmount());
			principal = profit.add(investmentAmount.getAmount());
			interest = interestRate.calMonthlyInterest(accInvestmentAmount);
			profit = principal.add(interest);
			result.add(new InvestmentDetail(i, principal, interest, profit)
			);
		}
		return result;
	}
}
