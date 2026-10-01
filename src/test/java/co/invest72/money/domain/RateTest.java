package co.invest72.money.domain;

import java.math.BigDecimal;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RateTest {
	@Nested
	@DisplayName("Rate 생성자 검증")
	class constructorValidationTest {
		@Test
		@DisplayName("객체 정상 생성")
		void should_can_create_instance() {
			// when
			Rate rate = Rate.of(BigDecimal.valueOf(0.05));
			// then
			Assertions.assertThat(rate).isNotNull();
		}

		@Test
		@DisplayName("값이 null이면 예외를 발생시켜야 한다")
		void should_throw_exception_when_value_is_null() {
			// when
			Assertions.assertThatThrownBy(() -> Rate.of(null))
				.isInstanceOf(NullPointerException.class)
				.hasMessage("Rate value must not be null");
		}
	}

	@Nested
	@DisplayName("퍼센트 변환 검증")
	class toPercentageTest {
		@Test
		@DisplayName("0.05를 5로 변환한다")
		void should_return_five_percent_when_value_is_zero_point_zero_five() {
			// given
			Rate rate = Rate.of(BigDecimal.valueOf(0.05));
			// when
			BigDecimal percentage = rate.toPercentage();
			// then
			BigDecimal expected = BigDecimal.valueOf(5);
			Assertions.assertThat(percentage).isEqualByComparingTo(expected);
		}
	}

	@Nested
	@DisplayName("곱하기 검증")
	class applyToTest {
		@Test
		@DisplayName("원금 100만원에 5%를 곱하면 이자 금액 5만원을 반환해야 한다.")
		void should_return_interest_when_target_is_one_hundred_won() {
			// given
			Rate rate = Rate.of(BigDecimal.valueOf(0.05));
			Money principal = Money.won(1_000_000);
			// when
			Money interest = rate.applyTo(principal);
			// then
			Assertions.assertThat(interest).isEqualTo(Money.won(50_000));
		}

		@Test
		@DisplayName("target이 null이면 예외를 발생시켜야 한다")
		void should_throw_exception_when_target_is_null() {
			// given
			Rate rate = Rate.of(BigDecimal.valueOf(0.05));
			// when
			Assertions.assertThatThrownBy(() -> rate.applyTo(null))
				.isInstanceOf(NullPointerException.class)
				.hasMessage("Money must not be null");
		}
	}
}
