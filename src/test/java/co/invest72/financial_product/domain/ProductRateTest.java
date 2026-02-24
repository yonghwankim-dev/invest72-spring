package co.invest72.financial_product.domain;

import java.math.BigDecimal;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class ProductRateTest {

	@DisplayName("객체 생성 - 유효한 비율")
	@ParameterizedTest(name = "비율: {0}")
	@MethodSource(value = "source.ProductRateTestDataProvider#validValues")
	void newInstance_whenRateIsValid_thenCreateInstance(BigDecimal value) {
		Assertions.assertThatCode(() -> new ProductRate(value))
			.doesNotThrowAnyException();
	}

	@DisplayName("객체 생성 - 유효하지 않은 비율")
	@ParameterizedTest(name = "비율: {0}")
	@MethodSource(value = "source.ProductRateTestDataProvider#invalidValues")
	void newInstance_whenRateIsInvalid_thenThrowException(BigDecimal value) {
		Assertions.assertThatThrownBy(() -> new ProductRate(value))
			.isInstanceOf(IllegalArgumentException.class);
	}
}
