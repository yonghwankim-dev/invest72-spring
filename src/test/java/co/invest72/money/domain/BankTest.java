package co.invest72.money.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import co.invest72.money.infrastructure.api.FixedExchangeRateProvider;

class BankTest {

	private Bank bank;

	@BeforeEach
	void setUp() {
		bank = new Bank(new FixedExchangeRateProvider());
	}

	@DisplayName("환전 - 원화를 원화로 환전하면 금액이 그대로여야 한다")
	@Test
	void reduce_whenSourceCurrencyIsSameTargetCurrency_whenReturnSameAmount() {
		// given
		Money wonMoney = Money.won(1000);
		Currency wonCurrency = Currency.won();
		// when
		Money reducedMoney = bank.reduce(wonMoney, wonCurrency);
		// then
		Assertions.assertThat(reducedMoney).isEqualTo(wonMoney);
	}

	@DisplayName("환전 - 원화를 달러로 환전한다")
	@Test
	void reduce_givenWonMoney_whenTargetIsDollar_thenReturnDollarMoney() {
		// given
		Money wonMoney = Money.won(1000);
		Currency dollarCurrency = Currency.dollar();
		// when
		Money dollarMoney = bank.reduce(wonMoney, dollarCurrency);
		// then
		Money expected = Money.dollar(1);
		Assertions.assertThat(dollarMoney).isEqualTo(expected);
	}

	@DisplayName("환전 - 달러를 원화로 환전한다.")
	@Test
	void reduce_whenSourceIsDollar_thenReturnWonMoney() {
		// given
		Money oneBucks = Money.dollar(1);
		Currency wonCurrency = Currency.won();
		// when
		Money wonMoney = bank.reduce(oneBucks, wonCurrency);
		// then
		Money expected = Money.won(1000);
		Assertions.assertThat(wonMoney).isEqualTo(expected);
	}
}
