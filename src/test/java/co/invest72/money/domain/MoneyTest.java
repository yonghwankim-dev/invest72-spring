package co.invest72.money.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MoneyTest {

	@DisplayName("객체 생성 - 달러 생성")
	@Test
	void newInstance_whenValueIsDollar_thenReturnMoney() {
		// when
		Money money = Money.dollar(5);
		// then
		Assertions.assertThat(money).isNotNull();
	}

	@DisplayName("객체 비교 - 동일한 달러를 가진 객체들끼리는 동일한 객체로 간주")
	@Test
	void equals_whenSameValue_thenReturnTrue() {
		// given
		Money money1 = Money.dollar(5);
		Money money2 = Money.dollar(5);
		// when
		boolean result = money1.equals(money2);
		// then
		Assertions.assertThat(result).isTrue();
		Assertions.assertThat(money1).hasSameHashCodeAs(money2);
		Assertions.assertThat(money1).isEqualTo(money2);
	}
}
