package co.invest72.financial_product.infrastructure.mapper;

import java.util.Objects;

import org.springframework.stereotype.Component;

import co.invest72.financial_product.domain.ProductAmount;
import co.invest72.money.domain.Money;

@Component
public class ProductAmountMapper {

	public ProductAmount toProductAmount(Money money) {
		return ProductAmount.from(money);
	}

	public Money toMoney(ProductAmount productAmount) {
		Objects.requireNonNull(productAmount, "ProductAmount 객체는 null일 수 없습니다.");
		return Money.of(productAmount.getValue(), productAmount.getCurrency());
	}
}
