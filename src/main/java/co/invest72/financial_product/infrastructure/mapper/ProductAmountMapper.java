package co.invest72.financial_product.infrastructure.mapper;

import java.util.Objects;

import org.springframework.transaction.annotation.Transactional;

import co.invest72.exchange_rate.domain.ExchangeRateRepository;
import co.invest72.financial_product.domain.ProductAmount;
import co.invest72.money.domain.Currency;
import co.invest72.money.domain.Money;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProductAmountMapper {

	private final ExchangeRateRepository exchangeRateRepository;

	public ProductAmount toProductAmount(Money money) {
		return ProductAmount.from(money);
	}

	@Transactional(readOnly = true)
	public Money toMoney(ProductAmount productAmount) {
		Objects.requireNonNull(productAmount, "ProductAmount 객체는 null일 수 없습니다.");

		Currency currency = exchangeRateRepository.findByCode(productAmount.getCurrency())
			.map(exchangeRate -> Currency.of(exchangeRate.getCurrencyCode(), exchangeRate.getCurrencyName()))
			.orElseThrow(
				() -> new IllegalArgumentException("not found ExchangeRate, code=" + productAmount.getCurrency()));
		return Money.of(productAmount.getValue(), currency);
	}
}
