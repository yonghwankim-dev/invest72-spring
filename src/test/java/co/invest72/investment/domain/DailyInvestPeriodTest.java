package co.invest72.investment.domain;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
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
}
