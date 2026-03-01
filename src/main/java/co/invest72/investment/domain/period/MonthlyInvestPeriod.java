package co.invest72.investment.domain.period;

import java.math.BigDecimal;

import co.invest72.investment.domain.InstallmentInvestmentAmount;
import co.invest72.investment.domain.InvestPeriod;
import co.invest72.investment.domain.PeriodRange;

public class MonthlyInvestPeriod implements InvestPeriod {
	private final PeriodRange periodRange;

	public MonthlyInvestPeriod(int months) {
		this(new PeriodMonthsRange(months));
	}

	public MonthlyInvestPeriod(PeriodRange periodRange) {
		this.periodRange = periodRange;
	}

	@Override
	public int getMonths() {
		return periodRange.toMonths();
	}

	@Override
	public int getTotalPrincipal(InstallmentInvestmentAmount investmentAmount) {
		return investmentAmount.getMonthlyAmount().multiply(BigDecimal.valueOf(periodRange.toMonths())).intValue();
	}
}
