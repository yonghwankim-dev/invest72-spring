package co.invest72.investment.domain.period;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import co.invest72.investment.domain.InvestPeriod;

class MonthlyInvestPeriodTest {

	private InvestPeriod investPeriod;

	@BeforeEach
	void setUp() {
		investPeriod = new MonthlyInvestPeriod(12);
	}

	@Test
	void created() {
		assertNotNull(investPeriod);
	}

	@Test
	@DisplayName("개월수가 0이어도 객체 생성이 가능하다")
	void can_created_instance_when_months_is_zero() {
		int months = 0;

		investPeriod = new MonthlyInvestPeriod(months);

		assertNotNull(investPeriod);
	}

	@Test
	@DisplayName("투자 개월수를 반환한다")
	void should_return_invest_months() {
		int actualMonths = investPeriod.getMonths();

		int expectedMonths = 12;
		assertEquals(expectedMonths, actualMonths);
	}

	@Test
	@DisplayName("투자 개월수가 음수이면 예외가 발생해야 한다")
	void should_throw_exception_when_months_is_negative() {
		// given
		int months = -1;
		// when & then
		assertThrows(IllegalArgumentException.class,
			() -> new MonthlyInvestPeriod(months));
	}

	@Test
	@DisplayName("투자 기간이 1개월일때 투자 일수를 계산하여 반환해야 한다")
	void should_return_invest_days_when_months_is_one() {
		// given
		investPeriod = new MonthlyInvestPeriod(1);
		LocalDate startDate = LocalDate.of(2026, 1, 1);
		// when
		int days = investPeriod.getDays(startDate);
		// then
		Assertions.assertThat(days).isEqualTo(31);
	}
}
