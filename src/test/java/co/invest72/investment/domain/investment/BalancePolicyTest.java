package co.invest72.investment.domain.investment;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import co.invest72.exchange_rate.domain.ExchangeRateRepository;
import co.invest72.exchange_rate.domain.entity.ExchangeRate;
import co.invest72.exchange_rate.infrastructure.persistence.InMemoryExchangeRateRepository;
import co.invest72.financial_product.domain.FinancialProduct;
import co.invest72.financial_product.domain.service.FinancialProductCalculator;
import co.invest72.money.domain.Currency;
import co.invest72.money.domain.Money;
import source.FinancialProductDataProvider;

class BalancePolicyTest {

	private FinancialProductCalculator calculator;
	private ExchangeRateRepository exchangeRateRepository;

	@BeforeEach
	void setUp() {
		exchangeRateRepository = new InMemoryExchangeRateRepository();
		calculator = new FinancialProductCalculator(exchangeRateRepository);
	}

	@DisplayName("투자 금액 계산 - FIXED, 기준일자 상관없이 원금을 반환한다")
	@Test
	void calculate_fixed_beforeStartDate() {
		// Given
		BalancePolicy policy = BalancePolicy.FIXED;
		FinancialProduct product = FinancialProductDataProvider.createDepositProduct("user-1");
		LocalDate today = LocalDate.of(2026, 1, 1);
		LocalDate expirationDate = calculator.calculateExpirationDate(product);
		ExchangeRate exchangeRate = exchangeRateRepository.findByCode(product.getAmount().getCurrency()).orElseThrow();
		Currency productCurrency = Currency.of(exchangeRate.getCurrencyCode(), exchangeRate.getCurrencyName());

		// When
		Money balance = policy.calculate(product, productCurrency, today, expirationDate);

		// Then
		Money expected = Money.of(product.getAmount().getValue(), productCurrency);
		Assertions.assertThat(balance).isEqualByComparingTo(expected);
	}

	@DisplayName("투자 금액 계산 - ACCUMULATIVE, 기준일자가 시작일자 하루전인 경우 투자 금액은 0이다")
	@Test
	void calculate_accumulative_beforeStartDate() {
		// Given
		BalancePolicy policy = BalancePolicy.ACCUMULATIVE;
		FinancialProduct product = FinancialProductDataProvider.createSavingsProduct("user-1");
		LocalDate today = LocalDate.of(2025, 12, 31);
		LocalDate expirationDate = calculator.calculateExpirationDate(product);
		ExchangeRate exchangeRate = exchangeRateRepository.findByCode(product.getAmount().getCurrency()).orElseThrow();
		Currency productCurrency = Currency.of(exchangeRate.getCurrencyCode(), exchangeRate.getCurrencyName());

		// When
		Money balance = policy.calculate(product, productCurrency, today, expirationDate);

		// Then
		Assertions.assertThat(balance)
			.isEqualByComparingTo(Money.won(BigDecimal.ZERO));
	}

	@DisplayName("투자 금액 계산 - ACCUMULATIVE, 기준일자가 만기일자 하루후인 경우 투자 금액은 원금 * 개월수이다")
	@Test
	void calculate_accumulative_afterExpirationDate() {
		// Given
		BalancePolicy policy = BalancePolicy.ACCUMULATIVE;
		FinancialProduct product = FinancialProductDataProvider.createSavingsProduct("user-1");
		LocalDate today = LocalDate.of(2027, 1, 2);
		LocalDate expirationDate = calculator.calculateExpirationDate(product);
		ExchangeRate exchangeRate = exchangeRateRepository.findByCode(product.getAmount().getCurrency()).orElseThrow();
		Currency productCurrency = Currency.of(exchangeRate.getCurrencyCode(), exchangeRate.getCurrencyName());

		// When
		Money balance = policy.calculate(product, productCurrency, today, expirationDate);

		// Then
		Money expectedBalance = Money.won(product.getAmount().getValue()
			.multiply(BigDecimal.valueOf(product.getMonths().getValue())));
		Assertions.assertThat(balance)
			.isEqualByComparingTo(expectedBalance);
	}
}
