package co.invest72.money.domain;

import java.math.BigDecimal;

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

	@DisplayName("대소 비교 - 같은 통화의 돈을 비교할때 0을 반환해야 한다.")
	@Test
	void compareTo_whenSameCurrency_thenReturnZero() {
		// given
		Money money1 = Money.dollar(5);
		Money money2 = Money.dollar(5);
		// when
		int result = money1.compareTo(money2);
		// then
		Assertions.assertThat(result).isZero();
	}

	@DisplayName("대소 비교 - 비교하려는 돈이 null인 경우 예외가 발생해야 한다.")
	@Test
	void compareTo_whenOtherIsNull_thenThrowException() {
		// given
		Money money1 = Money.dollar(5);
		Money money2 = null;
		// when & then
		Assertions.assertThatThrownBy(() -> money1.compareTo(money2))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("Money 객체는 null일 수 없습니다.");
	}

	@DisplayName("달러 생성 - 달러 생성 시, 통화는 USD로 설정되어야 한다.")
	@Test
	void dollar_whenCreateDollar_thenCurrencyIsUSD() {
		// given
		BigDecimal value = BigDecimal.valueOf(5);
		String currency = "USD";
		// when
		Money money = Money.of(value, currency);
		// then
		Assertions.assertThat(money).isEqualTo(Money.dollar(5));
	}

	@DisplayName("음수인지 체크 - 음수인 경우 true를 반환해야 한다.")
	@Test
	void isNegative_whenValueIsNegative_thenReturnTrue() {
		// given
		Money money = Money.dollar(-5);
		// when
		boolean result = money.isNegative();
		// then
		Assertions.assertThat(result).isTrue();
	}

	@DisplayName("달러 곱셈")
	@Test
	void times_whenMoneyIsDollar_thenReturnMultipliedDollar() {
		// given
		Money fiveBucks = Money.dollar(5);
		int multiplier = 3;
		// when
		Money result = fiveBucks.times(multiplier);
		// then
		Assertions.assertThat(result).isEqualTo(Money.dollar(15));
	}

	@DisplayName("달러 나눗셈 - Money를 BigDecimal로 나눌 때, 새로운 Money 객체가 반환되어야 한다.")
	@Test
	void divide_whenMoneyIsDollar_thenReturnDividedDollar() {
		// given
		Money fiveBucks = Money.dollar(5);
		BigDecimal divisor = BigDecimal.valueOf(2);
		// when
		Money result = fiveBucks.divide(divisor);
		// then
		Assertions.assertThat(result).isEqualTo(Money.dollar(BigDecimal.valueOf(2.5)));
	}
}
