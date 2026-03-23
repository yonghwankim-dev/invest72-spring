package co.invest72.financial_product.domain;

import java.math.BigDecimal;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class ProductTaxRateTest {

	@DisplayName("객체 생성 - 유효한 값이 주어졌을때 객체가 생성되어야 한다")
	@Test
	void newInstance_givenValue_thenReturnNewInstance() {
		// given
		BigDecimal value = BigDecimal.valueOf(0.154);
		// when
		ProductTaxRate productTaxRate = new ProductTaxRate(value);
		// then
		Assertions.assertThat(productTaxRate).isNotNull();
	}

	@DisplayName("객체 비교 - 동일한 값을 가진 두 상품 비교시 True를 반환해야 한다.")
	@Test
	void equals_whenSameValue_thenReturnTrue() {
		// given
		ProductTaxRate productTaxRate1 = new ProductTaxRate(BigDecimal.valueOf(0.154));
		ProductTaxRate productTaxRate2 = new ProductTaxRate(BigDecimal.valueOf(0.154));
		// when
		boolean actual = productTaxRate1.equals(productTaxRate2);
		// then
		Assertions.assertThat(actual).isTrue();
	}

	@DisplayName("객체 비교 - 소수점 자릿수(정밀도)가 달라도 값이 같으면 동일한 세율로 판단한다")
	@Test
	void equals_whenSameValueAndDiffPrecision_thenReturnTrue() {
		// given
		ProductTaxRate productTaxRate1 = new ProductTaxRate(BigDecimal.valueOf(0.154));
		ProductTaxRate productTaxRate2 = new ProductTaxRate(BigDecimal.valueOf(0.154000));
		// when
		boolean actual = productTaxRate1.equals(productTaxRate2);
		// then
		Assertions.assertThat(actual).isTrue();
		Assertions.assertThat(productTaxRate1).hasSameHashCodeAs(productTaxRate2);
	}

	@DisplayName("객체 비교 - 스케일 4를 초과하는 정밀도(precision)의 값이 달라도 비교시 논리적으로 같은 객체라고 판단한다")
	@ParameterizedTest
	@MethodSource(value = "source.ProductTaxRateDataProvider#provideTaxRateValues")
	void equals_when_comparing_values_with_different_precision_beyond_scale_4_then_return_equal(BigDecimal pivot,
		BigDecimal target) {
		// given
		ProductTaxRate productTaxRate1 = new ProductTaxRate(pivot);
		ProductTaxRate productTaxRate2 = new ProductTaxRate(target);
		// when
		boolean actual = productTaxRate1.equals(productTaxRate2);
		// then
		Assertions.assertThat(actual).isTrue();
		Assertions.assertThat(productTaxRate1).hasSameHashCodeAs(productTaxRate2);
	}

	@DisplayName("객체 비교 - 스케일 4를 초과한 값이 6이상이면 반올림해서 값이 다르다고 판단한다")
	@ParameterizedTest
	@MethodSource(value = "source.ProductTaxRateDataProvider#provideDiffTaxRateValue")
	void equals_whenScale5NumberIs6EqualMoreThan_ReturnFalse(BigDecimal pivot, BigDecimal target) {
		// given
		ProductTaxRate productTaxRate1 = new ProductTaxRate(pivot);
		ProductTaxRate productTaxRate2 = new ProductTaxRate(target);
		// when
		boolean actual = productTaxRate1.equals(productTaxRate2);
		// then
		Assertions.assertThat(actual).isFalse();
		Assertions.assertThat(productTaxRate1).doesNotHaveSameHashCodeAs(productTaxRate2);
	}

	@DisplayName("해시코드 - 스케일이 4를 초과해도 해시코드의 값은 스케일 4를 유지해야 한다")
	@ParameterizedTest
	@MethodSource(value = "source.ProductTaxRateDataProvider#provideTaxRateValues")
	void hashCode_whenOverScale4_thenReturnSameHashCode(BigDecimal pivot, BigDecimal target) {
		// given
		ProductTaxRate productTaxRate1 = new ProductTaxRate(pivot);
		ProductTaxRate productTaxRate2 = new ProductTaxRate(target);
		// when & then
		Assertions.assertThat(productTaxRate1).hasSameHashCodeAs(productTaxRate2);
	}
}
