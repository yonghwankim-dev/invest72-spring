package co.invest72.investment.domain.period;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

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
	public int getDays(LocalDate startDate) {
		LocalDate endDate = startDate.plusMonths(periodRange.toMonths());
		return (int)ChronoUnit.DAYS.between(startDate, endDate);
	}
}
