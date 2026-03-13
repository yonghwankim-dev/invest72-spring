package co.invest72.investment.domain.investment.factory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import co.invest72.investment.domain.InterestRate;
import co.invest72.investment.domain.InvestPeriod;
import co.invest72.investment.domain.InvestmentAmount;
import co.invest72.investment.domain.investment.MonthlyInvestmentDetail;
import co.invest72.money.domain.Money;

public class CompoundFixedInstallmentSavingMonthlyDetailFactory {

	private final InvestmentAmount investmentAmount;
	private final InterestRate interestRate;
	private final InvestPeriod investPeriod;

	public CompoundFixedInstallmentSavingMonthlyDetailFactory(InvestmentAmount investmentAmount,
		InterestRate interestRate,
		InvestPeriod investPeriod) {
		this.investmentAmount = investmentAmount;
		this.interestRate = interestRate;
		this.investPeriod = investPeriod;
	}

	public List<MonthlyInvestmentDetail> createDetails() {
		List<MonthlyInvestmentDetail> result = new ArrayList<>();
		// todo: 원화가 아닌 통화도 지원하도록 수정 필요
		Money principal = Money.won(BigDecimal.ZERO);
		Money interest = Money.won(BigDecimal.ZERO);
		Money profit = Money.won(BigDecimal.ZERO);

		result.add(new MonthlyInvestmentDetail(0, principal.getValue(), interest.getValue(), profit.getValue()));
		for (int i = 1; i <= investPeriod.getMonths(); i++) {
			principal = profit.add(investmentAmount.getAmount());
			interest = interestRate.calMonthlyInterest(principal);
			profit = principal.add(interest);
			result.add(new MonthlyInvestmentDetail(i, principal.getValue(), interest.getValue(), profit.getValue()));
		}
		return result;
	}
}
