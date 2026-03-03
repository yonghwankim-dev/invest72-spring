package co.invest72.financial_product.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import co.invest72.investment.domain.interest.AnnualInterestRate;
import co.invest72.investment.domain.interest.InterestType;
import co.invest72.investment.domain.investment.InvestmentType;
import co.invest72.investment.domain.investment.PaymentDay;
import co.invest72.investment.domain.tax.FixedTaxRate;
import co.invest72.investment.domain.tax.TaxType;
import source.FinancialProductDataProvider;

class SavingsProductTest {

	private SavingsProduct createInvalidUpdatedSavingProduct() {
		return SavingsProduct.builder()
			.id("new-id") // id 변경
			.userId("user2")
			.name("Updated Savings")
			.investmentType(InvestmentType.DEPOSIT)
			.amount(new ProductAmount(BigDecimal.valueOf(2000)))
			.months(new ProductMonths(24))
			.interestRate(new AnnualInterestRate(BigDecimal.valueOf(0.06)))
			.interestType(InterestType.COMPOUND)
			.taxType(TaxType.NON_TAX)
			.taxRate(new FixedTaxRate(BigDecimal.ZERO))
			.startDate(LocalDate.of(2024, 2, 1))
			.createdAt(LocalDate.of(2024, 2, 1).atStartOfDay())
			.build();
	}

	@DisplayName("상품 수정 - 적금 상품은 이름, 금액, 기간, 납입일, 이자율, 이자 유형, 세금 유형, 세율, 시작 날짜를 변경할 수 있다")
	@Test
	void update_whenValidUpdatedProduct_thenUpdateSuccessfully() {
		// Given
		FinancialProduct originalProduct = FinancialProductDataProvider.createSavingsProduct("user-1");
		SavingsProduct updatedProduct = createInvalidUpdatedSavingProduct().toBuilder()
			.id(originalProduct.getId()) // id는 원래 값으로 유지
			.userId(originalProduct.getUserId()) // userId는 원래 값으로 유지
			.investmentType(InvestmentType.SAVINGS) // investmentType은 원래 값으로 유지
			.createdAt(originalProduct.getCreatedAt()) // createdAt은 원래 값으로 유지
			.paymentDay(new PaymentDay(15)) // 납입일 변경
			.build();

		// When
		Assertions.assertThatCode(() -> originalProduct.update(updatedProduct))
			.doesNotThrowAnyException();
	}

	@DisplayName("상품 수정 - 적금 상품은 id를 변경할 수 없다")
	@Test
	void update_whenIdChanged_thenThrowException() {
		// Given
		FinancialProduct originalProduct = FinancialProductDataProvider.createSavingsProduct("user-1");
		SavingsProduct updatedProduct = createInvalidUpdatedSavingProduct();

		// When
		Throwable throwable = Assertions.catchThrowable(() -> originalProduct.update(updatedProduct));

		// then
		Assertions.assertThat(throwable)
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("상품 ID는 변경할 수 없습니다.");
	}

	@DisplayName("상품 수정 - 적금 상품은 사용자 식별자 아이디를 변경할 수 없다")
	@Test
	void update_whenUserIdChanged_thenThrowException() {
		// Given
		FinancialProduct originalProduct = FinancialProductDataProvider.createSavingsProduct("user-1");
		SavingsProduct updatedProduct = createInvalidUpdatedSavingProduct().toBuilder()
			.id(originalProduct.getId()) // id는 원래 값으로 유지
			.investmentType(InvestmentType.SAVINGS) // investmentType은 원래 값으로 유지
			.createdAt(originalProduct.getCreatedAt()) // createdAt은 원래 값으로 유지
			.paymentDay(new PaymentDay(15)) // 납입일 변경
			.build();

		// When
		Throwable throwable = Assertions.catchThrowable(() -> originalProduct.update(updatedProduct));

		// then
		Assertions.assertThat(throwable)
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("상품 소유자(userId)는 변경할 수 없습니다.");
	}

	@DisplayName("상품 수정 - 적금 상품은 투자 유형을 변경할 수 없다")
	@Test
	void update_whenInvestmentTypeChanged_thenThrowException() {
		// Given
		FinancialProduct originalProduct = FinancialProductDataProvider.createSavingsProduct("user-1");
		SavingsProduct updatedProduct = createInvalidUpdatedSavingProduct().toBuilder()
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

	@DisplayName("상품 수정 - 적금 상품은 생성 날짜를 변경할 수 없다")
	@Test
	void update_whenCreatedAtChanged_thenThrowException() {
		// Given
		FinancialProduct originalProduct = FinancialProductDataProvider.createSavingsProduct("user-1");
		SavingsProduct updatedProduct = createInvalidUpdatedSavingProduct().toBuilder()
			.id(originalProduct.getId()) // id는 원래 값으로 유지
			.userId(originalProduct.getUserId()) // userId는 원래 값으로 유지
			.investmentType(InvestmentType.SAVINGS) // investmentType은 원래 값으로 유지
			.paymentDay(new PaymentDay(15))
			.build();

		// When
		Throwable throwable = Assertions.catchThrowable(() -> originalProduct.update(updatedProduct));

		// then
		Assertions.assertThat(throwable)
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("생성 날짜(createdAt)는 변경할 수 없습니다.");
	}

	@DisplayName("상품 수정 - 적금 상품은 예금 상품으로 변경할 수 없다")
	@Test
	void update_whenChangedToDepositProduct_thenThrowException() {
		// Given
		FinancialProduct originalProduct = FinancialProductDataProvider.createSavingsProduct("user-1");
		DepositProduct updatedProduct = DepositProduct.builder()
			.id(originalProduct.getId()) // id는 원래 값으로 유지
			.userId(originalProduct.getUserId()) // userId는 원래 값으로 유지
			.name("Updated Deposit")
			.investmentType(InvestmentType.DEPOSIT) // 투자 유형 변경
			.amount(new ProductAmount(BigDecimal.valueOf(2000)))
			.months(new ProductMonths(24))
			.interestRate(new AnnualInterestRate(BigDecimal.valueOf(0.06)))
			.interestType(InterestType.COMPOUND)
			.taxType(TaxType.NON_TAX)
			.taxRate(new FixedTaxRate(BigDecimal.ZERO))
			.startDate(LocalDate.of(2024, 2, 1))
			.createdAt(originalProduct.getCreatedAt()) // createdAt은 원래 값으로 유지
			.build();

		// When
		Throwable throwable = Assertions.catchThrowable(() -> originalProduct.update(updatedProduct));

		// then
		Assertions.assertThat(throwable)
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("업데이트된 상품은 SavingsProduct여야 합니다.");
	}
}
