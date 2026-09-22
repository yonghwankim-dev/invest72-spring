package co.invest72.financial_product.infrastructure.mapper;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.invest72.exchange_rate.domain.entity.ExchangeRate;
import co.invest72.exchange_rate.domain.service.ExchangeRateService;
import co.invest72.financial_product.domain.ProductAmount;
import co.invest72.money.domain.Currency;
import co.invest72.money.domain.Money;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductAmountMapper {

	private final ExchangeRateService exchangeRateService;

	@Transactional(readOnly = true)
	public ProductAmount toProductAmount(Money money) {
		Objects.requireNonNull(money, "Money 객체는 null일 수 없습니다.");
		ExchangeRate exchangeRate = exchangeRateService.findExchangeRate(money.getCurrency().getCode());
		return ProductAmount.from(money, exchangeRate);
	}

	@Transactional(readOnly = true)
	public Money toMoney(ProductAmount productAmount) {
		Objects.requireNonNull(productAmount, "ProductAmount 객체는 null일 수 없습니다.");

		ExchangeRate exchangeRate = exchangeRateService.findExchangeRate(productAmount.getCurrency());
		Currency currency = Currency.of(exchangeRate.getCurrencyCode(), exchangeRate.getCurrencyName());
		return Money.of(productAmount.getValue(), currency);
	}
}
