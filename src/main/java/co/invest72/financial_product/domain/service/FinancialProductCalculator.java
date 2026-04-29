package co.invest72.financial_product.domain.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.NoSuchElementException;

import co.invest72.exchange_rate.domain.ExchangeRateRepository;
import co.invest72.financial_product.domain.FinancialProduct;
import co.invest72.investment.domain.investment.InvestmentType;
import co.invest72.money.domain.Currency;
import co.invest72.money.domain.Money;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FinancialProductCalculator {

	private final ExchangeRateRepository exchangeRateRepository;

	public LocalDate calculateExpirationDate(FinancialProduct product) {
		InvestmentType investmentType = InvestmentType.valueOf(product.getInvestmentTypeName());
		LocalDate startDate = product.getStartDate();
		Integer months = product.getMonthsValue();

		return investmentType.calculateExpirationDate(startDate, months);
	}

	public Money calculateBalance(FinancialProduct product, LocalDate today) {
		LocalDate expirationDate = calculateExpirationDate(product);
		return calculateBalance(product, today, expirationDate);
	}

	public Money calculateBalance(FinancialProduct product, LocalDate today, LocalDate expirationDate) {
		InvestmentType investmentType = InvestmentType.valueOf(product.getInvestmentTypeName());
		Currency currency = exchangeRateRepository.findByCode(product.getAmount().getCurrency())
			.map(exchangeRate -> Currency.of(exchangeRate.getCurrencyCode(), exchangeRate.getCurrencyName()))
			.orElseThrow(
				() -> new NoSuchElementException("not found ExchangeRate, code=" + product.getAmount().getCurrency()));
		return investmentType.calculateBalance(product, currency, today, expirationDate);
	}

	public BigDecimal calculateProgress(FinancialProduct product, LocalDate today) {
		LocalDate expirationDate = calculateExpirationDate(product);
		return calculateProgress(product, today, expirationDate);
	}

	public BigDecimal calculateProgress(FinancialProduct product, LocalDate today, LocalDate expirationDate) {
		InvestmentType investmentType = InvestmentType.valueOf(product.getInvestmentTypeName());
		LocalDate startDate = product.getStartDate();

		return investmentType.calculateProgress(startDate, expirationDate, today);
	}

	public Long calculateRemainingDays(FinancialProduct product, LocalDate today) {
		LocalDate expirationDate = calculateExpirationDate(product);
		return calculateRemainingDays(product, today, expirationDate);
	}

	public Long calculateRemainingDays(FinancialProduct product, LocalDate today, LocalDate expirationDate) {
		InvestmentType investmentType = InvestmentType.valueOf(product.getInvestmentTypeName());
		return investmentType.calculateRemainingDays(today, expirationDate);
	}
}
