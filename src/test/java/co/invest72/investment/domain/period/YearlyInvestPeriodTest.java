package co.invest72.investment.domain.period;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import co.invest72.investment.domain.InvestPeriod;

class YearlyInvestPeriodTest {

	@Nested
	@DisplayName("생성자 제약조건 검증")
	class ConstructorValidationTest {
		@ParameterizedTest(name = "년수가 {0}년일때 객체가 생성된다")
		@ValueSource(ints = {0, 1, 999})
		void can_create_instance(int years) {
			// when
			InvestPeriod sut = new YearlyInvestPeriod(years);
			// then
			Assertions.assertThat(sut).isNotNull();
		}

		@ParameterizedTest(name = "년수({0})가 음수이거나 999년보다 크면 예외가 발생해야 한다")
		@ValueSource(ints = {-1, 1000})
		void should_throw_exception_when_years_is_negative_or_more_than_999(int years) {
			Assertions.assertThatThrownBy(() -> new YearlyInvestPeriod(years))
				.isInstanceOf(IllegalArgumentException.class);
		}
	}

	@Nested
	@DisplayName("투자 개월수 계산 검증")
	class getMonthsTest {
		@ParameterizedTest
		@CsvSource({
			"0, 0",
			"1, 12",
			"2, 24",
			"12, 144"
		})
		@DisplayName("투자 년수가 1년이면 12개월을 반환해야 한다")
		void should_return_invest_months_given_years(int years, int expectedMonths) {
			// given
			InvestPeriod sut = new YearlyInvestPeriod(years);
			// when
			int months = sut.getMonths();
			// then
			Assertions.assertThat(months).isEqualTo(expectedMonths);
		}
	}

	@Test
	void shouldReturnMonths() {
		InvestPeriod investPeriod = new YearlyInvestPeriod(10);

		int months = investPeriod.getMonths();

		int expectedMonths = 120;
		assertEquals(expectedMonths, months);
	}

	@Nested
	@DisplayName("투자 일수 계산 검증")
	class getDaysTest {
		@Test
		void should_return_invest_days_when_year_is_one() {
			// given
			int years = 1;
			InvestPeriod sut = new YearlyInvestPeriod(years);
			LocalDate startDate = LocalDate.of(2026, 1, 1);
			// when
			int days = sut.getDays(startDate);
			// then
			Assertions.assertThat(days).isEqualTo(365);
		}
	}
}
