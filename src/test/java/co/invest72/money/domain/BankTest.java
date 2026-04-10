package co.invest72.money.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BankTest {

	@DisplayName("객체 생성")
	@Test
	void canCreated() {
		// when
		Bank bank = new Bank();
		// then
		Assertions.assertThat(bank).isNotNull();
	}

	@DisplayName("환전 - 원화를 달러로 환전한다")
	@Test
	void reduce_givenWonMoney_whenTargetIsDollar_thenReturnDollarMoney() {
		// given
		Bank bank = new Bank();
		Money wonMoney = Money.won(1000);
		Currency dollarCurrency = Currency.dollar();
		// when
		Money dollarMoney = bank.reduce(wonMoney, dollarCurrency);
		// then
		Money expected = Money.dollar(1);
		Assertions.assertThat(dollarMoney).isEqualTo(expected);
	}

}
