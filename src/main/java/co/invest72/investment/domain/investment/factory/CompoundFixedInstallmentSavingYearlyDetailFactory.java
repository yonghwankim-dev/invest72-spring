package co.invest72.investment.domain.investment.factory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import co.invest72.investment.domain.InterestRate;
import co.invest72.investment.domain.InvestPeriod;
import co.invest72.investment.domain.Investment;
import co.invest72.investment.domain.InvestmentAmount;
import co.invest72.investment.domain.investment.YearlyInvestmentDetail;
import co.invest72.money.domain.Currency;
import co.invest72.money.domain.Money;

public class CompoundFixedInstallmentSavingYearlyDetailFactory {

	private final InvestmentAmount investmentAmount;
	private final InterestRate interestRate;
	private final InvestPeriod investPeriod;

	public CompoundFixedInstallmentSavingYearlyDetailFactory(InvestmentAmount investmentAmount,
		InterestRate interestRate,
		InvestPeriod investPeriod) {
		this.investmentAmount = investmentAmount;
		this.interestRate = interestRate;
		this.investPeriod = investPeriod;
	}

	public List<YearlyInvestmentDetail> createDetails() {
		List<YearlyInvestmentDetail> result = new ArrayList<>();
		Currency currency = investmentAmount.getAmount().getCurrency();
		Money principal = Money.of(BigDecimal.ZERO, currency);
		Money interest = Money.of(BigDecimal.ZERO, currency);
		Money profit = Money.of(BigDecimal.ZERO, currency);

		result.add(new YearlyInvestmentDetail(0,
			Investment.roundToTwoDecimalPlaces.apply(principal.getValue()),
			Investment.roundToTwoDecimalPlaces.apply(interest.getValue()),
			Investment.roundToTwoDecimalPlaces.apply(profit.getValue())));
		int years = getFinalYear();
		for (int i = 1; i <= years; i++) {
			BigDecimal months = BigDecimal.valueOf(Math.min(12, investPeriod.getMonths() - (i - 1) * 12));
			Money value = investmentAmount.getAmount().times(months);
			principal = profit.add(value);

			interest = calculateYearlyInterest(profit, months.intValue());

			profit = principal.add(interest);
			result.add(new YearlyInvestmentDetail(i,
				Investment.roundToTwoDecimalPlaces.apply(principal.getValue()),
				Investment.roundToTwoDecimalPlaces.apply(interest.getValue()),
				Investment.roundToTwoDecimalPlaces.apply(profit.getValue())));
		}
		return result;
	}

	private int getFinalYear() {
		return (investPeriod.getMonths() - 1) / 12 + 1;
	}

	private Money calculateYearlyInterest(Money baseProfit, int month) {
		Money principal;
		Money result = baseProfit.times(BigDecimal.ZERO);
		Money interest;
		Money profit = baseProfit;
		for (int i = 1; i <= month; i++) {
			principal = profit.add(investmentAmount.getAmount());
			interest = interestRate.calMonthlyInterest(principal);
			profit = principal.add(interest);
			result = result.add(interest);
		}
		return result;
	}
}
