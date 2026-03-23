package co.invest72.financial_product.domain;

import java.math.BigDecimal;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class ProductAnnualInterestRateTest {

	@DisplayName("객체 생성")
	@Test
	void canCreated() {
		// when
		ProductAnnualInterestRate rate = new ProductAnnualInterestRate(BigDecimal.valueOf(0.05));
		// then
		Assertions.assertThat(rate).isNotNull();
	}

	@DisplayName("객체 비교 - 스케일 4를 초과하는 값을 비교시 동일하다고 판단한다")
	@ParameterizedTest
	@MethodSource(value = "source.ProductAnnualInterestRateTestDataProvider#provideScaledValues")
	void equals_whenScale4IsOver_thenReturnTrue(BigDecimal pivot, BigDecimal target) {
		// given
		ProductAnnualInterestRate rate1 = new ProductAnnualInterestRate(pivot);
		ProductAnnualInterestRate rate2 = new ProductAnnualInterestRate(target);
		// when
		boolean actual = rate1.equals(rate2);
		// then
		Assertions.assertThat(actual).isTrue();
		Assertions.assertThat(rate1).hasSameHashCodeAs(rate2);
	}

	@DisplayName("객체 비교 - 스케일 4를 초과하고 6이상이면 값을 비교시 동일하지 않다고 판단한다")
	@ParameterizedTest
	@MethodSource(value = "source.ProductAnnualInterestRateTestDataProvider#provideScaledValues")
	void equals_whenScale4IsOverAndValueIs6EqualMoreThan_thenReturnFalse(BigDecimal pivot, BigDecimal target) {
		// given
		ProductAnnualInterestRate rate1 = new ProductAnnualInterestRate(pivot);
		ProductAnnualInterestRate rate2 = new ProductAnnualInterestRate(target);
		// when
		boolean actual = rate1.equals(rate2);
		// then
		Assertions.assertThat(actual).isFalse();
	}
}
