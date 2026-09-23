package co.invest72.financial_product.infrastructure.mapper;

import java.math.BigDecimal;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;

import co.invest72.exchange_rate.domain.entity.ExchangeRate;
import co.invest72.exchange_rate.domain.service.ExchangeRateService;
import co.invest72.financial_product.domain.ProductAmount;
import co.invest72.money.domain.Money;

class ProductAmountMapperTest {

	private ProductAmountMapper mapper;
	private ExchangeRateService exchangeRateService;

	@BeforeEach
	void setUp() {
		exchangeRateService = BDDMockito.mock(ExchangeRateService.class);
		mapper = new ProductAmountMapper(exchangeRateService);
	}

	@DisplayName("ProductAmount 변환 - Money를 ProductAmount로 변환하여 반환해야 한다")
	@Test
	void should_return_product_Amount_type_when_param_is_money_type() {
		// given
		Money money = Money.won(10000);

		ExchangeRate exchangeRate = new ExchangeRate("KRW", "한국 원", BigDecimal.ONE);
		BDDMockito.given(exchangeRateService.findExchangeRate(money.getCurrency().getCode()))
			.willReturn(exchangeRate);
		// when
		ProductAmount productAmount = mapper.toProductAmount(money);

		// then
		ProductAmount expected = ProductAmount.of(BigDecimal.valueOf(10000), exchangeRate);
		Assertions.assertThat(productAmount).isEqualTo(expected);
	}

	@DisplayName("ProductAmount 변환 - Money가 null인 경우 예외가 발생해야 한다.")
	@Test
	void should_throw_exception_when_money_param_is_null() {
		// when & then
		Assertions.assertThatThrownBy(() -> mapper.toProductAmount(null))
			.isInstanceOf(NullPointerException.class)
			.hasMessage("Money 객체는 null일 수 없습니다.");
	}

	@DisplayName("Money 변환 - ProductAmount를 Money 타입으로 변환하여 반환한다")
	@Test
	void should_return_money_instance_type_when_param_type_is_product_amount_type() {
		// given
		ExchangeRate exchangeRate = new ExchangeRate("KRW", "한국 원", BigDecimal.ONE);
		BDDMockito.given(exchangeRateService.findExchangeRate("KRW"))
			.willReturn(exchangeRate);
		ProductAmount productAmount = ProductAmount.of(BigDecimal.valueOf(10000), exchangeRate);

		// when
		Money result = mapper.toMoney(productAmount);

		// then
		Assertions.assertThat(result).isEqualTo(Money.won(10000));
	}

	@DisplayName("Money 객체 변환 - ProductAmount가 null인 경우 예외가 발생해야 한다.")
	@Test
	void should_throw_exception_when_productAmount_param_is_null() {
		// when & then
		Assertions.assertThatThrownBy(() -> mapper.toMoney(null))
			.isInstanceOf(NullPointerException.class)
			.hasMessage("ProductAmount 객체는 null일 수 없습니다.");
	}
}
