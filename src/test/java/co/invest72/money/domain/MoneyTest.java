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
		// when & then
		Assertions.assertThat(money1)
			.hasSameHashCodeAs(money2)
			.isEqualTo(money2);
	}

	@DisplayName("객체 생성 - 원화 생성")
	@Test
	void newInstance_whenCurrentIsKRW_thenReturnMoney() {
		// when
		Money money = Money.won(1000);
		// then
		Assertions.assertThat(money).isNotNull();
	}

	@DisplayName("금액 비교 - 원화와 달러를 비교했을때 달라야 한다.")
	@Test
	void equals_whenSameValueButDifferentCurrency_thenReturnFalse() {
		// given
		Money money1 = Money.dollar(5);
		Money money2 = Money.won(5);
		// when & then
		Assertions.assertThat(money1)
			.doesNotHaveSameHashCodeAs(money2)
			.isNotEqualTo(money2);
	}

	@DisplayName("덧셈 - 두개의 달러를 더했을 때, 새로운 달러 객체가 반환되어야 한다.")
	@Test
	void add_whenMoneyIsDollar_thenReturnNewSumDollar() {
		// given
		Money fiveBucks = Money.dollar(5);
		Money tenBucks = Money.dollar(10);
		// when
		Money result = fiveBucks.add(tenBucks);
		// then
		Assertions.assertThat(result).isEqualTo(Money.dollar(15));
	}

	@DisplayName("덧셈 - 통화가 다른 돈을 더하려고 할 때, 예외가 발생해야 한다.")
	@Test
	void add_whenCurrentIsDifferent_thenThrowException() {
		// given
		Money fiveBucks = Money.dollar(5);
		Money oneThousandWon = Money.won(1000);
		// when & then
		Assertions.assertThatThrownBy(() -> fiveBucks.add(oneThousandWon))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("통화가 일치하지 않습니다.");
	}
}
