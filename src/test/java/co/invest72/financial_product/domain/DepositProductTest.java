package co.invest72.financial_product.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import co.invest72.investment.domain.interest.InterestType;
import co.invest72.investment.domain.investment.InvestmentType;
import co.invest72.investment.domain.tax.FixedTaxRate;
import co.invest72.investment.domain.tax.TaxType;
import source.FinancialProductDataProvider;

class DepositProductTest {

	/**
	 * 변경되면 안되는 정보가 변경된 예금 상품 객체 생성
	 * @return 변경되면 안되는 정보가 변경된 예금 상품 객체
	 */
	private DepositProduct createInvalidUpdatedDeposit() {
		return DepositProduct.builder()
			.id("new-id") // id 변경
			.userId("user2") // userId 변경
			.name("Updated Deposit")
			.productInvestmentType(ProductInvestmentType.from(InvestmentType.SAVINGS))
			.amount(ProductAmount.won(BigDecimal.valueOf(2000)))
			.months(new ProductMonths(24))
			.productAnnualInterestRate(new ProductAnnualInterestRate(BigDecimal.valueOf(0.06)))
			.interestType(InterestType.COMPOUND)
			.productInterestType(ProductInterestType.from(InterestType.COMPOUND))
			.taxType(TaxType.NON_TAX)
			.taxRate(new FixedTaxRate(BigDecimal.ZERO))
			.startDate(LocalDate.of(2024, 2, 1))
			.createdAt(LocalDate.of(2024, 2, 1).atStartOfDay()) // createdAt 변경
			.build();
	}

	@DisplayName("상품 수정 - 예금 상품은 상품의 id를 변경할 수 없다")
	@Test
	void update_whenUserIdOrInvestmentTypeChanged_thenThrowException() {
		// Given
		FinancialProduct originalProduct = FinancialProductDataProvider.createDepositProduct("user-1");
		DepositProduct updatedProduct = createInvalidUpdatedDeposit().toBuilder()
			.userId(originalProduct.getUserId()) // userId는 원래 값으로 유지
			.productInvestmentType(originalProduct.getProductInvestmentType())
			.build();

		// When
		Throwable throwable = Assertions.catchThrowable(() -> originalProduct.update(updatedProduct));

		// then
		Assertions.assertThat(throwable)
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("상품 ID는 변경할 수 없습니다.");
	}

	@DisplayName("상품 수정 - 예금 상품은 사용자 식별자(userId) 변경할 수 없다")
	@Test
	void update_whenUserIdChanged_thenThrowException() {
		// Given
		FinancialProduct originalProduct = FinancialProductDataProvider.createDepositProduct("user-1");
		DepositProduct updatedProduct = createInvalidUpdatedDeposit().toBuilder()
			.id(originalProduct.getId()) // id는 원래 값으로 유지
			.userId("user-2") // userId 변경
			.productInvestmentType(originalProduct.getProductInvestmentType())
			.build();

		// When
		Throwable throwable = Assertions.catchThrowable(() -> originalProduct.update(updatedProduct));

		// then
		Assertions.assertThat(throwable)
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("상품 소유자(userId)는 변경할 수 없습니다.");
	}

	@DisplayName("상품 수정 - 예금 상품은 투자 유형을 변경할 수 없다")
	@Test
	void update_whenInvestmentTypeChanged_thenThrowException() {
		// Given
		FinancialProduct originalProduct = FinancialProductDataProvider.createDepositProduct("user-1");
		DepositProduct updatedProduct = createInvalidUpdatedDeposit().toBuilder()
			.id(originalProduct.getId()) // id는 원래 값으로 유지
			.userId(originalProduct.getUserId()) // userId는 원래 값으로 유지
			.createdAt(originalProduct.getCreatedAt()) // createdAt은 원래 값으로 유지
			.build();

		// When
		Throwable throwable = Assertions.catchThrowable(() -> originalProduct.update(updatedProduct));

		// then
		Assertions.assertThat(throwable)
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("투자 유형(InvestmentType)은 변경할 수 없습니다.");
	}

	@DisplayName("상품 수정 - 예금 상품은 생성시간을 변경할 수 없다")
	@Test
	void update_whenCreatedAtChanged_thenThrowException() {
		// Given
		FinancialProduct originalProduct = FinancialProductDataProvider.createDepositProduct("user-1");
		DepositProduct updatedProduct = createInvalidUpdatedDeposit().toBuilder()
			.id(originalProduct.getId()) // id는 원래 값으로 유지
			.userId(originalProduct.getUserId()) // userId는 원래 값으로 유지
			.productInvestmentType(originalProduct.getProductInvestmentType())
			.createdAt(LocalDate.of(2024, 2, 1).atStartOfDay()) // createdAt 변경
			.build();

		// When
		Throwable throwable = Assertions.catchThrowable(() -> originalProduct.update(updatedProduct));

		// then
		Assertions.assertThat(throwable)
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("생성 날짜(createdAt)는 변경할 수 없습니다.");
	}

	@DisplayName("상품 수정 - 예금 상품을 적금 상품으로 업데이트할 수 없다")
	@Test
	void update_whenUpdatedProductIsNotDeposit_thenThrowException() {
		// Given
		FinancialProduct originalProduct = FinancialProductDataProvider.createDepositProduct("user-1");
		FinancialProduct updatedProduct = FinancialProductDataProvider.createSavingsProduct("user-1");

		// When
		Throwable throwable = Assertions.catchThrowable(() -> originalProduct.update(updatedProduct));

		// then
		Assertions.assertThat(throwable)
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("업데이트된 상품은 DepositProduct여야 합니다.");
	}
}
