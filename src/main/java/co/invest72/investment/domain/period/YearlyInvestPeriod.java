package co.invest72.investment.domain.period;

import co.invest72.investment.domain.InstallmentInvestmentAmount;
import co.invest72.investment.domain.InvestPeriod;
import co.invest72.investment.domain.PeriodRange;

public class YearlyInvestPeriod implements InvestPeriod {

	private final PeriodRange periodRange;

	public YearlyInvestPeriod(int years) {
		this(new PeriodYearRange(years));
	}

	public YearlyInvestPeriod(PeriodRange periodRange) {
		this.periodRange = periodRange;
	}

	@Override
	public int getMonths() {
		return periodRange.toMonths();
	}

	@Override
	public int getTotalPrincipal(InstallmentInvestmentAmount investmentAmount) {
		return investmentAmount.getMonthlyAmount()
			.times(periodRange.toMonths())
			.getValue()
			.intValue();
	}
}
