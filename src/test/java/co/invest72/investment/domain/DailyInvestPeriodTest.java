package co.invest72.investment.domain;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("일일 투자 기간 단위 테스트")
class DailyInvestPeriodTest {
	@Nested
	@DisplayName("생성자 제약 조건 검증")
	class ConstructorValidationTest {
		@Test
		@DisplayName("객체 생성시 시작일자가 null이면 예외가 발생해야 한다")
		@SuppressWarnings("ConstantConditions")
		void should_throw_exception_when_start_date_is_null() {
			// given
			LocalDate startDate = null;
			int days = 30;
			// when & then
			Assertions.assertThatThrownBy(() -> new DailyInvestPeriod(startDate, days))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("startDate must not null");
		}

		@ParameterizedTest
		@ValueSource(ints = {-1, -10, -100})
		@DisplayName("days가 음수이면 IllegalArgumentException 예외가 발생한다")
		void should_throw_exception_when_days_is_negative(int negativeDays) {
			// given
			LocalDate startDate = LocalDate.of(2026, 9, 11);
			// when & then
			Assertions.assertThatThrownBy(() -> new DailyInvestPeriod(startDate, negativeDays))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("days must not negative, days=" + negativeDays);
		}
	}

	@Nested
	@DisplayName("개월수 계산 검증")
	class getMonthsTest {
		@Test
		@DisplayName("0일이면 0개월을 반환한다")
		void should_return_zero_months_when_days_is_zero() {
			// given
			LocalDate startDate = LocalDate.of(2026, 1, 1);
			int days = 0;
			InvestPeriod sut = new DailyInvestPeriod(startDate, days);
			// when
			int months = sut.getMonths();
			// then
			Assertions.assertThat(months).isZero();
		}

		@ParameterizedTest
		@CsvSource({
			"30, 0", // 1월은 31일까지 있으므로 30일 경과(1/31)시 한달 미충족 -> 0개월
			"31, 1", // 31일 경과시 2월 1일이 되어 정확히 만 1개월 충족 -> 1개월
			"365, 12" // 1년(365일) 경과시 만 12개월 충족 -> 12개월
		})
		@DisplayName("일수가 주어졌을때 시작일로부터 몇개월인지 반환한다")
		void should_return_months_correctly_for_standard_days(int days, int expectedMonth) {
			// given
			LocalDate startDate = LocalDate.of(2026, 1, 1);
			InvestPeriod investPeriod = new DailyInvestPeriod(startDate, days);
			// when
			int months = investPeriod.getMonths();
			// then
			Assertions.assertThat(months).isEqualTo(expectedMonth);
		}
	}
}
