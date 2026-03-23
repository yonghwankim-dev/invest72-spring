package co.invest72.financial_product.domain;

import java.math.BigDecimal;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTaxRateTest {

	@DisplayName("객체 생성 - 유효한 값이 주어졌을때 객체가 생성되어야 한다")
	@Test
	void newInstance_givenValue_thenReturnNewInstance() {
		// given
		BigDecimal value = BigDecimal.valueOf(0.154);
		// when
		ProductTaxRate productTaxRate = new ProductTaxRate(value);
		// then
		Assertions.assertThat(productTaxRate).isNotNull();
	}
}
