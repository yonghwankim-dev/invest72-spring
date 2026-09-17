package co.invest72.investment.domain;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DailyInvestPeriod implements InvestPeriod {

	private final LocalDate startDate;
	private final int days;

	/**
	 *
	 * @param startDate 시작일자
	 * @param days 일수
	 * @throws IllegalArgumentException startDate가 null이면 예외가 발생한다.
	 * @throws IllegalArgumentException 다음 조건 중 하나라도 만족하는 경우 발생한다:
	 *                                  <ul>
	 *                                    <li>startDate가 null인 경우</li>
	 *                                    <li>days가 0 미만인 경우</li>
	 *                                  </ul>
	 */
	public DailyInvestPeriod(LocalDate startDate, int days) throws IllegalArgumentException {
		if (startDate == null) {
			throw new IllegalArgumentException("startDate must not null");
		}
		if (days < 0) {
			throw new IllegalArgumentException("days must not negative, days=" + days);
		}
		this.startDate = startDate;
		this.days = days;
	}

	@Override
	public int getMonths() {
		LocalDate endDate = this.startDate.plusDays(days);
		return (int)ChronoUnit.MONTHS.between(this.startDate, endDate);
	}

	@Override
	public int getDays(LocalDate startDate) {
		if (startDate == null) {
			throw new IllegalArgumentException("startDate must not null");
		}
		LocalDate endDate = startDate.plusDays(days);
		return (int)ChronoUnit.DAYS.between(startDate, endDate);
	}
}
