package co.invest72.exchange_rate.domain.service;

import java.math.BigDecimal;

import co.invest72.exchange_rate.domain.Currency;
import co.invest72.exchange_rate.domain.ExchangeRateProvider;
import co.invest72.money.domain.Money;

public class Bank {

	private final ExchangeRateProvider exchangeRateProvider;

	public Bank(ExchangeRateProvider exchangeRateProvider) {
		this.exchangeRateProvider = exchangeRateProvider;
	}

	/**
	 * target에 해당하는 통화로 환전하여 반환한다
	 * @param money 대상 금액
	 * @param target 환전하고자 하는 통화 단위
	 * @return 환전된 금액
	 */
	public Money reduce(Money money, Currency target) {
		BigDecimal rate = exchangeRateProvider.getRate(money.getCurrency(), target)
			.orElseThrow(() -> new IllegalArgumentException("undefined rate, " + money.getCurrency() + "->" + target));
		BigDecimal amount = money.getValue().multiply(rate);
		return Money.of(amount, target);
	}
}
