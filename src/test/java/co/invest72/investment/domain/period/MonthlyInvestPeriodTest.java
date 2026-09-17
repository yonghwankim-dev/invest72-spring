package co.invest72.investment.domain.period;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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
	@DisplayName("투자 개월수가 0개월일때 0일을 반환한다")
	void should_return_zero_days_when_months_is_zero() {
		// given
		investPeriod = new MonthlyInvestPeriod(0);
		LocalDate startDate = LocalDate.of(2026, 1, 1);
		// when
		int days = investPeriod.getDays(startDate);
		// then
		Assertions.assertThat(days).isZero();
	}

	@ParameterizedTest(name = "투자 개월수가 {0}개월일때 {1}일을 반환한다")
	@CsvSource({
		"1, 31", // 1월1일 ~ 2월1일, 총 31일
		"2, 59", // 1월1일 ~ 3월1일, 총 59일
		"12, 365" // 1월1일 ~ 27년 1월1일, 총 365
	})
	void should_return_invest_days_when_months_is_one(int months, int expectedMonths) {
		// given
		investPeriod = new MonthlyInvestPeriod(months);
		LocalDate startDate = LocalDate.of(2026, 1, 1);
		// when
		int days = investPeriod.getDays(startDate);
		// then
		Assertions.assertThat(days).isEqualTo(expectedMonths);
	}
}
