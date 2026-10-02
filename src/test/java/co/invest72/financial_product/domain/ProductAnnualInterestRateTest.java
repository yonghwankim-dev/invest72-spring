package co.invest72.financial_product.domain;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ProductAnnualInterestRateTest {

	@Nested
	@DisplayName("동등성 비교 검증")
	class EqualityTest {
		@Test
		@DisplayName("동일한 수치 값을 가질 경우 Scale(소수점 자릿수 표현)이 달라도 equals는 true를 반환한다")
		void should_return_true_when_values_are_match_regardless_of_scale() {
			// given
			ProductAnnualInterestRate rate1 = new ProductAnnualInterestRate(new BigDecimal("0.05"));
			ProductAnnualInterestRate rate2 = new ProductAnnualInterestRate(new BigDecimal("0.05000000"));
			// when & then
			Assertions.assertThat(rate1).isEqualTo(rate2);
		}

		@Test
		@DisplayName("동일한 수치 값을 가질 경우 Scale이 달라도 hashCode는 동일하다")
		void should_have_same_hash_code_when_values_are_match_regardless_of_scale() {
			// given
			ProductAnnualInterestRate rate1 = new ProductAnnualInterestRate(new BigDecimal("0.05"));
			ProductAnnualInterestRate rate2 = new ProductAnnualInterestRate(new BigDecimal("0.05000000"));
			// when & then
			Assertions.assertThat(rate1.hashCode()).hasSameHashCodeAs(rate2);
		}

		@Test
		@DisplayName("Scale이 다른 동일 수치 객체들을 Set에 저장하면 중복 제거되어 1개만 남는다")
		void should_deduplicate_in_set_when_values_are_numerically_equal() {
			// given
			ProductAnnualInterestRate rate1 = new ProductAnnualInterestRate(new BigDecimal("0.05"));
			ProductAnnualInterestRate rate2 = new ProductAnnualInterestRate(new BigDecimal("0.05000000"));
			Set<ProductAnnualInterestRate> set = new HashSet<>();
			// when
			set.add(rate1);
			set.add(rate2);
			// then
			Assertions.assertThat(set).hasSize(1);
		}

		@Test
		@DisplayName("수치 값이 다르면 equals는 false를 반환한다")
		void should_return_false_when_values_are_different() {
			// given
			ProductAnnualInterestRate rate1 = new ProductAnnualInterestRate(new BigDecimal("0.05"));
			ProductAnnualInterestRate rate2 = new ProductAnnualInterestRate(new BigDecimal("0.05000001"));
			// when & then
			Assertions.assertThat(rate1).isNotEqualTo(rate2);
		}
	}

	@Nested
	@DisplayName("생성 및 유효성 검증")
	class ValidationTest {
		@ParameterizedTest
		@ValueSource(strings = {"0", "0.05", "0.05555555", "9.99999999", "9.999999990"})
		@DisplayName("0% 이상 999.99999999% 이하의 유효한 범위의 값이 들어오면 정상적으로 객체가 생성된다")
		void should_create_instance_when_value_is_within_valid_range(String inputValue) {
			// given
			BigDecimal value = new BigDecimal(inputValue);
			// when
			ProductAnnualInterestRate rate = new ProductAnnualInterestRate(value);
			// then
			Assertions.assertThat(rate.getValue()).isEqualByComparingTo(value);
		}

		@Test
		@DisplayName("value가 null이면 예외를 발생시켜야 한다")
		void should_throw_exception_when_value_is_null() {
			// given
			BigDecimal value = null;
			// when
			Assertions.assertThatThrownBy(() -> new ProductAnnualInterestRate(value))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("값은 0 이상 9.99999999 이하이어야 합니다.");
		}

		@ParameterizedTest
		@ValueSource(strings = {"-0.00000001", "-1", "10.00000000", "99.99999999", "9.999999999", "9.999999991"})
		@DisplayName("0 미만이거나 상한값(9.99999999)을 초과하는 경우 IllegalArgumentException이 발생한다")
		void should_throw_exception_when_value_is_out_of_range(String inputValue) {
			// given
			BigDecimal value = new BigDecimal(inputValue);
			// when & then
			Assertions.assertThatThrownBy(() -> new ProductAnnualInterestRate(value))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("값은 0 이상 9.99999999 이하이어야 합니다.");
		}
	}
}
