package co.invest72.financial_product.domain;

import java.math.BigDecimal;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class ProductAmountTest {

	@DisplayName("금액이 범위 안에 있는 경우 인스턴스가 생성되어야 한다.")
	@ParameterizedTest(name = "금액: {0}, 설명: {1}")
	@MethodSource(value = "source.ProductAmountTestDataProvider#validAmounts")
	void newInstance_whenAmountIsValid_thenCreateInstance(BigDecimal value, String ignored) {
		Assertions.assertThatCode(() -> ProductAmount.won(value))
			.doesNotThrowAnyException();
	}

	@DisplayName("금액이 범위를 벗어난 경우 예외가 발생해야 한다.")
	@ParameterizedTest(name = "금액: {0}")
	@MethodSource(value = "source.ProductAmountTestDataProvider#invalidAmounts")
	void newInstance_whenAmountIsInvalid_thenThrowException(BigDecimal value) {
		Assertions.assertThatThrownBy(() -> ProductAmount.won(value))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("객체 해시코드 비교 - 두 객체의 해시코드가 동일하다")
	@Test
	void hashCode_whenSameValue_thenHashCodesAreEqual() {
		ProductAmount amount1 = ProductAmount.won(new BigDecimal("0.1"));
		ProductAmount amount2 = ProductAmount.won(new BigDecimal("0.10"));

		Assertions.assertThat(amount1).isEqualTo(amount2);
		Assertions.assertThat(amount1.hashCode()).hasSameHashCodeAs(amount2.hashCode());
	}

	@DisplayName("원화 생성 - KRW 통화의 상품 금액이 생성되어야 한다")
	@Test
	void won_whenCurrencyIsKRW_thenInstanceIsNotNull() {
		// given
		BigDecimal value = BigDecimal.valueOf(1000);
		// when
		ProductAmount productAmount = ProductAmount.won(value);
		// then
		Assertions.assertThat(productAmount).isNotNull();
	}
}
