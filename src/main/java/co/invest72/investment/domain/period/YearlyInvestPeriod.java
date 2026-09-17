package co.invest72.investment.domain.period;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

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
	public int getDays(LocalDate startDate) {
		if (startDate == null) {
			throw new IllegalArgumentException("startDate must not null");
		}
		LocalDate endDate = startDate.plusMonths(periodRange.toMonths());
		return (int)ChronoUnit.DAYS.between(startDate, endDate);
	}
}
