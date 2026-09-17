package co.invest72.investment.domain;

import java.time.LocalDate;

public interface InvestPeriod {
	/**
	 * 투자 기간을 개월수(months)로 계산해서 반환한다.
	 * @return 개월수
	 */
	int getMonths();

	default int getDays(LocalDate startDate) {
		return 0;
	}
}
