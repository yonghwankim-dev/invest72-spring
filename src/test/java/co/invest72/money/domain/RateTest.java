package co.invest72.money.domain;

import java.math.BigDecimal;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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

	@Nested
	@DisplayName("반올림 검증")
	class roundTest {
		@ParameterizedTest
		@DisplayName("Rate 객체의 round() 메서드는 소수점 넷째 자리(0.0001) 기준으로 반올림된 값을 반환한다")
		@CsvSource(value = {
			// 1. 일반적인 올림 / 버림 (5 미만 버림, 5 초과 올림)
			"0.0555444444, 0.0555", // 5번째 자리가 4 -> 버림
			"0.0555666666, 0.0556", // 5번째 자리가 6 -> 올림

			// 2. HALF_EVEN 핵심 경계 조건: 5번째 자리가 '정확히 5'일 때 (뒤에 0만 있거나 끝남)
			// 2-1. 앞자리(4번째 자리)가 '홀수'인 경우 -> 올림하여 짝수로 만듦
			"0.05515,       0.0552", // 4번째 자리 '1'(홀수) + 뒤에 '5' -> 올림하여 0.0552
			"0.05535,       0.0554", // 4번째 자리 '3'(홀수) + 뒤에 '5' -> 올림하여 0.0554
			"0.05555,       0.0556", // 4번째 자리 '5'(홀수) + 뒤에 '5' -> 올림하여 0.0556

			// 2-2. 앞자리(4번째 자리)가 '짝수'인 경우 -> 버려서 짝수로 유지
			"0.05525,       0.0552", // 4번째 자리 '2'(짝수) + 뒤에 '5' -> 버려서 0.0552
			"0.05545,       0.0554", // 4번째 자리 '4'(짝수) + 뒤에 '5' -> 버려서 0.0554
			"0.05565,       0.0556", // 4번째 자리 '6'(짝수) + 뒤에 '5' -> 버려서 0.0556

			// 3. 5번째 자리가 '5'이지만 뒤에 0이 아닌 숫자가 더 붙어 있는 경우 (.5000...1 초과)
			// -> 앞자리가 짝수이더라도 항상 올림 처리됨
			"0.0552500001,  0.0553", // 짝수 '2' 뒤의 5 초과 수치 -> 올림되어 0.0553

			// 4. 경계값 (0, 음수 등)
			"0.0000000000,  0.0000",
			"-0.055555555, -0.0556"
		})
		void should_return_rounded_value_when_rate_round(double value, double expected) {
			// given
			Rate rate = Rate.of(BigDecimal.valueOf(value));
			// when
			BigDecimal roundedValue = rate.round();
			// then
			Assertions.assertThat(roundedValue).isEqualByComparingTo(BigDecimal.valueOf(expected));
		}
	}
}
