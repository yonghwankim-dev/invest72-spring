package co.invest72.financial_product.domain;

import java.math.BigDecimal;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class ProductAmountTest {

	@DisplayName("금액이 범위 안에 있는 경우 인스턴스가 생성되어야 한다.")
	@ParameterizedTest(name = "금액: {0}")
	@MethodSource(value = "source.ProductAmountTestDataProvider#validAmounts")
	void newInstance_whenAmountIsValid_thenCreateInstance(BigDecimal value) {
		Assertions.assertThatCode(() -> new ProductAmount(value))
			.doesNotThrowAnyException();
	}

	@DisplayName("금액이 범위를 벗어난 경우 예외가 발생해야 한다.")
	@ParameterizedTest(name = "금액: {0}")
	@MethodSource(value = "source.ProductAmountTestDataProvider#invalidAmounts")
	void newInstance_whenAmountIsInvalid_thenThrowException(BigDecimal value) {
		Assertions.assertThatThrownBy(() -> new ProductAmount(value))
			.isInstanceOf(IllegalArgumentException.class);
	}

}
