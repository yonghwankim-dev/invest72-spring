package co.invest72.financial_product.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import source.FinancialProductDataProvider;

class FinancialProductTest {

	@DisplayName("객체 생성 - 적금 상품 생성시 이체일이 초기화되지 않는 경우 예외가 발생한다.")
	@Test
	void constructor_whenCreatingSavingsProduct_thenThrowExceptionIfPaymentDayIsNotSet() {
		// Given
		SavingsProduct savings = (SavingsProduct)FinancialProductDataProvider.createSavingsProduct("user-1");

		// when
		Throwable throwable = Assertions.catchThrowable(() -> savings.toBuilder()
			.paymentDay(null)
			.build());
		// then
		Assertions.assertThat(throwable)
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("적금 상품은 납입일이 반드시 필요합니다.");
	}

	@DisplayName("객체 생성 - 빌더를 이용하여 현금 생성")
	@Test
	void newInstance_whenInvestmentTypeIsCash_thenReturnProduct() {
		FinancialProduct product = FinancialProductDataProvider.createCashProduct("user-1234");

		Assertions.assertThat(product).isNotNull();
	}
}
