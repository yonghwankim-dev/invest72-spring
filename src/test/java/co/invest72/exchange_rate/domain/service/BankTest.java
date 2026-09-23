package co.invest72.exchange_rate.domain.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;

import co.invest72.exchange_rate.domain.entity.ExchangeRate;
import co.invest72.money.domain.Currency;
import co.invest72.money.domain.CurrencyPair;
import co.invest72.money.domain.Money;

class BankTest {

	private Bank bank;
	private ExchangeRateService exchangeRateService;

	@BeforeEach
	void setUp() {
		exchangeRateService = BDDMockito.mock(ExchangeRateService.class);
		// 엔화 추가
		Currency jpy = Currency.jpy();
		exchangeRateService.saveRate(new ExchangeRate(jpy.getCode(), jpy.getName(), new BigDecimal("9.5105")));
		Currency dollar = Currency.dollar();
		exchangeRateService.saveRate(new ExchangeRate(dollar.getCode(), dollar.getName(), BigDecimal.valueOf(1000)));
		bank = new Bank(exchangeRateService);
	}

	@DisplayName("환전 - 원화를 원화로 환전하면 금액이 그대로여야 한다")
	@Test
	void reduce_whenSourceCurrencyIsSameTargetCurrency_whenReturnSameAmount() {
		// given
		Money wonMoney = Money.won(1000);
		Currency wonCurrency = Currency.won();
		CurrencyPair pair = new CurrencyPair(wonMoney.getCurrency(), wonCurrency);
		BDDMockito.given(exchangeRateService.getRate(pair))
			.willReturn(Optional.of(BigDecimal.ONE));
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
		CurrencyPair pair = new CurrencyPair(wonMoney.getCurrency(), dollarCurrency);
		BDDMockito.given(exchangeRateService.getRate(pair))
			.willReturn(Optional.of(BigDecimal.valueOf(0.001)));
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
		CurrencyPair pair = new CurrencyPair(oneBucks.getCurrency(), wonCurrency);
		BDDMockito.given(exchangeRateService.getRate(pair))
			.willReturn(Optional.of(BigDecimal.valueOf(1_000)));
		// when
		Money wonMoney = bank.reduce(oneBucks, wonCurrency);
		// then
		Money expected = Money.won(1000);
		Assertions.assertThat(wonMoney).isEqualTo(expected);
	}

	@DisplayName("환전 - 엔화를 달러로 변환한다")
	@Test
	void reduce_whenJpyToUsd_thenReturnMoney() {
		// given
		Money jpyMoney = Money.of(BigDecimal.valueOf(1000), Currency.jpy());
		Currency dollar = Currency.dollar();
		CurrencyPair pair = new CurrencyPair(jpyMoney.getCurrency(), dollar);
		BDDMockito.given(exchangeRateService.getRate(pair))
			.willReturn(Optional.of(BigDecimal.valueOf(0.00951)));

		// when
		Money dollarMoney = bank.reduce(jpyMoney, dollar);
		// then
		Money expected = Money.dollar(new BigDecimal("9.51"));
		Assertions.assertThat(dollarMoney).isEqualTo(expected);
	}
}
