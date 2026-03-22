package co.invest72.financial_product.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class ProductMonthsTest {

	@DisplayName("객체 생성 - 유효한 기간")
	@ParameterizedTest(name = "기간: {0}개월")
	@MethodSource(value = "source.ProductMonthsTestDataProvider#validValues")
	void newInstance_whenMonthsIsValid_thenCreateInstance(Integer value) {
		Assertions.assertThatCode(() -> new ProductMonths(value))
			.doesNotThrowAnyException();
	}

	@DisplayName("객체 생성 - null 체크")
	@Test
	void newInstance_whenMonthsIsValue_thenThrowException() {
		Assertions.assertThatThrownBy(() -> new ProductMonths(null))
			.isInstanceOf(NullPointerException.class);
	}

	@DisplayName("객체 생성 - 유효하지 않은 기간")
	@ParameterizedTest(name = "기간: {0}개월")
	@MethodSource(value = "source.ProductMonthsTestDataProvider#invalidValues")
	void newInstance_whenMonthsIsInvalid_thenThrowException(Integer value) {
		Assertions.assertThatThrownBy(() -> new ProductMonths(value))
			.isInstanceOf(IllegalArgumentException.class);
	}
}
