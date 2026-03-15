package co.invest72.investment.domain.investment.factory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import co.invest72.investment.domain.InterestRate;
import co.invest72.investment.domain.InvestPeriod;
import co.invest72.investment.domain.Investment;
import co.invest72.investment.domain.InvestmentAmount;
import co.invest72.investment.domain.investment.MonthlyInvestmentDetail;
import co.invest72.money.domain.Currency;
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

	public List<MonthlyInvestmentDetail> createDetails() {
		List<MonthlyInvestmentDetail> result = new ArrayList<>();
		Currency currentCurrency = investmentAmount.getAmount().getCurrency();
		Money accInvestmentAmount = Money.of(BigDecimal.ZERO, currentCurrency);
		Money principal = Money.of(BigDecimal.ZERO, currentCurrency);
		Money interest = Money.of(BigDecimal.ZERO, currentCurrency);
		Money profit = Money.of(BigDecimal.ZERO, currentCurrency);

		result.add(new MonthlyInvestmentDetail(0, principal.getValue(), interest.getValue(), profit.getValue()));
		for (int i = 1; i <= investPeriod.getMonths(); i++) {
			accInvestmentAmount = accInvestmentAmount.add(investmentAmount.getAmount());
			principal = profit.add(investmentAmount.getAmount());
			interest = interestRate.calMonthlyInterest(accInvestmentAmount);
			profit = principal.add(interest);
			result.add(new MonthlyInvestmentDetail(
					i,
					Investment.roundToTwoDecimalPlaces.apply(principal.getValue()),
					Investment.roundToTwoDecimalPlaces.apply(interest.getValue()),
					Investment.roundToTwoDecimalPlaces.apply(profit.getValue())
				)
			);
		}
		return result;
	}
}
