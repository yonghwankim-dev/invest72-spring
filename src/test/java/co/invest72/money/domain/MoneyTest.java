package co.invest72.money.domain;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

	@DisplayName("금액 비교 - 다른 통화와 비교시 예외가 발생해야 한다.")
	@Test
	void equals_whenDifferentCurrency_thenReturnFalse() {
		// given
		Money money1 = Money.dollar(5);
		Money money2 = Money.won(5);
		// when
		boolean result = money1.equals(money2);
		// then
		Assertions.assertThat(result).isFalse();
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
			.isInstanceOf(NullPointerException.class)
			.hasMessage("Money 객체는 null일 수 없습니다.");
	}

	@DisplayName("달러 생성 - 달러 생성 시, 통화는 USD로 설정되어야 한다.")
	@Test
	void dollar_whenCreateDollar_thenCurrencyIsUSD() {
		// given
		BigDecimal value = BigDecimal.valueOf(5);
		Currency dollar = Currency.dollar();
		// when
		Money money = Money.of(value, dollar);
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

	@DisplayName("달러 나눗셈 - 분모가 0이면 0이 반환되어야 한다.")
	@Test
	void divide_whenDivisorIsZero_thenReturnZero() {
		// given
		Money fiveBucks = Money.dollar(5);
		BigDecimal divisor = BigDecimal.ZERO;
		// when
		Money result = fiveBucks.divide(divisor);
		// then
		Assertions.assertThat(result).isEqualTo(Money.dollar(BigDecimal.ZERO));
	}

	@DisplayName("달러 나눗셈 - 분모가 null이면 예외가 발생해야 한다.")
	@Test
	void divide_whenDivisorIsNull_thenThrowException() {
		// given
		Money fiveBucks = Money.dollar(5);
		BigDecimal divisor = null;
		// when & then
		Assertions.assertThatThrownBy(() -> fiveBucks.divide(divisor))
			.isInstanceOf(NullPointerException.class)
			.hasMessage("분모는 null일 수 없습니다.");
	}

	@Nested
	@DisplayName("동등성 비교 검증")
	class EqualityTest {
		@Test
		@DisplayName("Scale이 달라도 수학적으로 같은 금액이면 equals는 true를 반환한다")
		void should_equal_when_values_are_numerically_same_despite_scale() {
			// given
			Money won1 = Money.won(new BigDecimal("1000"));
			Money won2 = Money.won(new BigDecimal("1000.0000"));
			// when & then
			Assertions.assertThat(won1)
				.isEqualTo(won2)
				.hasSameHashCodeAs(won2);
		}

		@Test
		@DisplayName("미세하게 수치가 다르면(10.004 vs 10.001) equals는 false를 반환한다")
		void should_not_equal_when_values_are_slightly_different() {
			// given
			Money won1 = Money.won(new BigDecimal("10.004"));
			Money won2 = Money.won(new BigDecimal("10.001"));
			// when & then
			Assertions.assertThat(won1).isNotEqualTo(won2);
		}

		@Test
		@DisplayName("통화가 다른 객체끼리 equals 비교시 예외를 발생시키지 않고 false를 반환한다")
		void should_return_false_when_equals_compared_with_different_currency() {
			// given
			Money won = Money.won(new BigDecimal("10"));
			Money dollar = Money.dollar(new BigDecimal("10"));
			// when & then
			Assertions.assertThat(won.equals(dollar)).isFalse();
			Assertions.assertThat(dollar.equals(won)).isFalse();
		}

		@Test
		@DisplayName("Scale 차이가 있는 동일 금액들을 Set에 저장 시 정상 중복 제거된다")
		void should_deduplicate_in_set() {
			// given
			Set<Money> set = new HashSet<>();
			Money won1 = Money.won(new BigDecimal("100"));
			Money won2 = Money.won(new BigDecimal("100.00"));
			// when
			set.add(won1);
			set.add(won2);
			// then
			Assertions.assertThat(set).hasSize(1);
		}
	}

	@Nested
	@DisplayName("금액 간 비율 계산 테스트")
	class ratioOfTest {

		@Test
		@DisplayName("20만원은 100만원의 0.2(20%) 비율을 반환한다")
		void should_return_ratio() {
			// given
			Money money = Money.won(200_000);
			Money target = Money.won(1_000_000);
			// when
			BigDecimal ratio = money.ratioOf(target);
			// then
			BigDecimal expected = BigDecimal.valueOf(0.2);
			Assertions.assertThat(ratio).isEqualByComparingTo(expected);
		}

		@Test
		@DisplayName("분모가 0인 경우 0을 반환해야 한다")
		void should_return_zero_when_target_is_zero() {
			// given
			Money money = Money.won(200_000);
			Money target = Money.won(0);
			// when
			BigDecimal ratio = money.ratioOf(target);
			// then
			Assertions.assertThat(ratio).isZero();
		}

		@Test
		@DisplayName("서로 다른 통화 간 비율 계산시 예외가 발생해야 한다")
		void should_throw_exception_when_currencies_are_different() {
			// given
			Money money = Money.won(200_000);
			Money target = Money.dollar(1_000_000);
			// when & then
			Assertions.assertThatThrownBy(() -> money.ratioOf(target))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("currencies are different, this.currency=KRW, target.currency=USD");
		}

		@Test
		@DisplayName("target이 null인 경우 예외가 발생해야 한다")
		void should_throw_exception_when_target_is_null() {
			// given
			Money money = Money.won(200_000);
			Money target = null;
			// when & then
			Assertions.assertThatThrownBy(() -> money.ratioOf(target))
				.isInstanceOf(NullPointerException.class)
				.hasMessage("target must not null");
		}
	}
}
