package co.invest72.financial_product.infrastructure.mapper;

import java.math.BigDecimal;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import co.invest72.financial_product.domain.ProductAmount;
import co.invest72.money.domain.Money;

class ProductAmountMapperTest {

	private ProductAmountMapper mapper;

	@BeforeEach
	void setUp() {
		mapper = new ProductAmountMapper();
	}

	@DisplayName("ProductAmount 변환 - Money 객체를 ProductAmount로 변환할 때, 올바른 금액이 반환되어야 한다.")
	@Test
	void toProductAmount_whenMoneyIsValid_thenReturnProductAmount() {
		// given
		Money money = Money.won(10000);

		// when
		ProductAmount productAmount = mapper.toProductAmount(money);

		// then
		ProductAmount expected = new ProductAmount(BigDecimal.valueOf(10000));
		Assertions.assertThat(productAmount)
			.hasSameHashCodeAs(expected)
			.isEqualTo(expected);
	}

	@DisplayName("ProductAmount 변환 - Money가 null인 경우 예외가 발생해야 한다.")
	@Test
	void toProductAmount_whenMoneyIsNull_thenThrowException() {
		// when & then
		Assertions.assertThatThrownBy(() -> mapper.toProductAmount(null))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("Money 객체는 null일 수 없습니다.");
	}

	@DisplayName("Money 객체 변환 - Money 객체의 통화 코드가 올바르게 매핑되어야 한다.")
	@Test
	void toMoney_whenProductAmountIsValid_thenReturnMoneyWithCorrectCurrency() {
		// given
		ProductAmount productAmount = new ProductAmount(BigDecimal.valueOf(10000));

		// when
		Money result = mapper.toMoney(productAmount);

		// then
		Assertions.assertThat(result)
			.isEqualTo(Money.won(10000));
	}

	@DisplayName("Money 객체 변환 - ProductAmount가 null인 경우 예외가 발생해야 한다.")
	@Test
	void toMoney_whenProductAmountIsNull_thenThrowException() {
		// when & then
		Assertions.assertThatThrownBy(() -> mapper.toMoney(null))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("ProductAmount 객체는 null일 수 없습니다.");
	}
}
