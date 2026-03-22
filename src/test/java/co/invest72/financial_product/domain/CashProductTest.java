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

class CashProductTest {

	private CashProduct createInvalidUpdatedCashProduct() {
		return CashProduct.builder()
			.id("new-id") // id 변경
			.userId("user2") // userId 변경
			.name("Updated Cash Product")
			.productInvestmentType(ProductInvestmentType.from(InvestmentType.SAVINGS))
			.amount(ProductAmount.won(BigDecimal.valueOf(2_000_000L)))
			.months(new ProductMonths(12))
			.productAnnualInterestRate(new ProductAnnualInterestRate(BigDecimal.valueOf(0.05)))
			.interestType(InterestType.COMPOUND)
			.productInterestType(ProductInterestType.from(InterestType.COMPOUND))
			.taxType(TaxType.NON_TAX)
			.taxRate(new FixedTaxRate(BigDecimal.ZERO))
			.startDate(LocalDate.of(2024, 2, 1))
			.createdAt(LocalDate.of(2024, 2, 1).atStartOfDay())
			.build();
	}

	@DisplayName("상품 수정 - 현금 상품은 이름, 금액, 시작일자만 변경할 수 있다")
	@Test
	void update_whenValidUpdatedProduct_thenUpdateSuccessfully() {
		// Given
		FinancialProduct originalProduct = FinancialProductDataProvider.createCashProduct("user-1");
		FinancialProduct updatedProduct = createInvalidUpdatedCashProduct().toBuilder()
			.id(originalProduct.getId())
			.userId(originalProduct.getUserId())
			.name("Updated Cash Product") // 이름 변경
			.productInvestmentType(originalProduct.getProductInvestmentType())
			.amount(ProductAmount.won(BigDecimal.valueOf(2_000_000L))) // 금액 변경
			.months(originalProduct.getMonths())
			.productAnnualInterestRate(originalProduct.getProductAnnualInterestRate())
			.interestType(originalProduct.getInterestType())
			.taxType(originalProduct.getTaxType())
			.taxRate(originalProduct.getTaxRate())
			.startDate(originalProduct.getStartDate().plusDays(10)) // 시작 날짜 변경
			.createdAt(originalProduct.getCreatedAt()) // createdAt은 원래 값으로 유지
			.build();

		// When & then
		Assertions.assertThatCode(() -> originalProduct.update(updatedProduct))
			.doesNotThrowAnyException();
	}

	@DisplayName("상품 수정 - 현금 상품은 id를 변경할 수 없다")
	@Test
	void update_whenIdChanged_thenThrowException() {
		// Given
		FinancialProduct originalProduct = FinancialProductDataProvider.createCashProduct("user-1");
		FinancialProduct updatedProduct = createInvalidUpdatedCashProduct().toBuilder()
			.id("new-id") // id 변경
			.userId(originalProduct.getUserId()) // userId는 원래 값으로 유지
			.productInvestmentType(originalProduct.getProductInvestmentType())
			.name("Updated Cash Product") // 이름 변경
			.amount(ProductAmount.won(BigDecimal.valueOf(2_000_000L))) // 금액 변경
			.months(originalProduct.getMonths())
			.productAnnualInterestRate(originalProduct.getProductAnnualInterestRate())
			.interestType(originalProduct.getInterestType())
			.taxType(originalProduct.getTaxType())
			.taxRate(originalProduct.getTaxRate())
			.startDate(originalProduct.getStartDate().plusDays(10)) // 시작 날짜 변경
			.createdAt(originalProduct.getCreatedAt()) // createdAt은 원래 값으로 유지
			.build();

		// When
		Throwable throwable = Assertions.catchThrowable(() -> originalProduct.update(updatedProduct));

		// then
		Assertions.assertThat(throwable)
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("상품 ID는 변경할 수 없습니다.");
	}

	@DisplayName("상품 수정 - 현금 상품은 사용자 식별자(userId)를 변경할 수 없다")
	@Test
	void update_whenUserIdChanged_thenThrowException() {
		// Given
		FinancialProduct originalProduct = FinancialProductDataProvider.createCashProduct("user-1");
		FinancialProduct updatedProduct = createInvalidUpdatedCashProduct().toBuilder()
			.id(originalProduct.getId()) // id는 원래 값으로 유지
			.userId("user-2") // userId 변경
			.productInvestmentType(originalProduct.getProductInvestmentType())
			.name("Updated Cash Product") // 이름 변경
			.amount(ProductAmount.won(BigDecimal.valueOf(2_000_000L))) // 금액 변경
			.months(originalProduct.getMonths())
			.productAnnualInterestRate(originalProduct.getProductAnnualInterestRate())
			.interestType(originalProduct.getInterestType())
			.taxType(originalProduct.getTaxType())
			.taxRate(originalProduct.getTaxRate())
			.startDate(originalProduct.getStartDate().plusDays(10)) // 시작 날짜 변경
			.createdAt(originalProduct.getCreatedAt()) // createdAt은 원래 값으로 유지
			.build();

		// When
		Throwable throwable = Assertions.catchThrowable(() -> originalProduct.update(updatedProduct));

		// then
		Assertions.assertThat(throwable)
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("상품 소유자(userId)는 변경할 수 없습니다.");
	}

	@DisplayName("상품 수정 - 현금 상품은 투자 유형을 변경할 수 없다")
	@Test
	void update_whenInvestmentTypeChanged_thenThrowException() {
		// Given
		FinancialProduct originalProduct = FinancialProductDataProvider.createCashProduct("user-1");
		FinancialProduct updatedProduct = createInvalidUpdatedCashProduct().toBuilder()
			.id(originalProduct.getId()) // id는 원래 값으로 유지
			.userId(originalProduct.getUserId()) // userId는 원래 값으로 유지
			.productInvestmentType(ProductInvestmentType.from(InvestmentType.SAVINGS))
			.name("Updated Cash Product") // 이름 변경
			.amount(ProductAmount.won(BigDecimal.valueOf(2_000_000L))) // 금액 변경
			.months(originalProduct.getMonths())
			.productAnnualInterestRate(originalProduct.getProductAnnualInterestRate())
			.interestType(originalProduct.getInterestType())
			.taxType(originalProduct.getTaxType())
			.taxRate(originalProduct.getTaxRate())
			.startDate(originalProduct.getStartDate().plusDays(10)) // 시작 날짜 변경
			.createdAt(originalProduct.getCreatedAt()) // createdAt은 원래 값으로 유지
			.build();

		// When
		Throwable throwable = Assertions.catchThrowable(() -> originalProduct.update(updatedProduct));

		// then
		Assertions.assertThat(throwable)
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("투자 유형(InvestmentType)은 변경할 수 없습니다.");
	}

	@DisplayName("상품 수정 - 현금 상품은 createdAt을 변경할 수 없다")
	@Test
	void update_whenCreatedAtChanged_thenThrowException() {
		// Given
		FinancialProduct originalProduct = FinancialProductDataProvider.createCashProduct("user-1");
		FinancialProduct updatedProduct = createInvalidUpdatedCashProduct().toBuilder()
			.id(originalProduct.getId()) // id는 원래 값으로 유지
			.userId(originalProduct.getUserId()) // userId는 원래 값으로 유지
			.productInvestmentType(originalProduct.getProductInvestmentType())
			.name("Updated Cash Product") // 이름 변경
			.amount(ProductAmount.won(BigDecimal.valueOf(2_000_000L))) // 금액 변경
			.months(originalProduct.getMonths())
			.productAnnualInterestRate(originalProduct.getProductAnnualInterestRate())
			.interestType(originalProduct.getInterestType())
			.taxType(originalProduct.getTaxType())
			.taxRate(originalProduct.getTaxRate())
			.startDate(originalProduct.getStartDate().plusDays(10)) // 시작 날짜 변경
			.createdAt(LocalDate.of(2024, 2, 1).atStartOfDay()) // createdAt 변경
			.build();

		// When
		Throwable throwable = Assertions.catchThrowable(() -> originalProduct.update(updatedProduct));

		// then
		Assertions.assertThat(throwable)
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("생성 날짜(createdAt)는 변경할 수 없습니다.");
	}

	@DisplayName("상품 수정 - 현금 상품은 투자 기간(months)를 변경할 수 없다")
	@Test
	void update_whenMonthsChanged_thenThrowException() {
		// Given
		FinancialProduct originalProduct = FinancialProductDataProvider.createCashProduct("user-1");
		FinancialProduct updatedProduct = createInvalidUpdatedCashProduct().toBuilder()
			.id(originalProduct.getId()) // id는 원래 값으로 유지
			.userId(originalProduct.getUserId()) // userId는 원래 값으로 유지
			.productInvestmentType(originalProduct.getProductInvestmentType())
			.name("Updated Cash Product") // 이름 변경
			.amount(ProductAmount.won(BigDecimal.valueOf(2_000_000L))) // 금액 변경
			.months(new ProductMonths(24)) // months 변경
			.interestType(originalProduct.getInterestType())
			.taxType(originalProduct.getTaxType())
			.taxRate(originalProduct.getTaxRate())
			.startDate(originalProduct.getStartDate().plusDays(10)) // 시작 날짜 변경
			.createdAt(originalProduct.getCreatedAt()) // createdAt은 원래 값으로 유지
			.build();

		// When
		Throwable throwable = Assertions.catchThrowable(() -> originalProduct.update(updatedProduct));

		// then
		Assertions.assertThat(throwable)
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("투자 기간(months)은 변경할 수 없습니다.");
	}

	@DisplayName("상품 수정 - 현금 상품은 이자율(interestRate)을 변경할 수 없다")
	@Test
	void update_whenInterestRateChanged_thenThrowException() {
		// Given
		FinancialProduct originalProduct = FinancialProductDataProvider.createCashProduct("user-1");
		FinancialProduct updatedProduct = createInvalidUpdatedCashProduct().toBuilder()
			.id(originalProduct.getId()) // id는 원래 값으로 유지
			.userId(originalProduct.getUserId()) // userId는 원래 값으로 유지
			.productInvestmentType(originalProduct.getProductInvestmentType())
			.name("Updated Cash Product") // 이름 변경
			.amount(ProductAmount.won(BigDecimal.valueOf(2_000_000L))) // 금액 변경
			.months(originalProduct.getMonths())
			.interestType(originalProduct.getInterestType())
			.taxType(originalProduct.getTaxType())
			.taxRate(originalProduct.getTaxRate())
			.startDate(originalProduct.getStartDate().plusDays(10)) // 시작 날짜 변경
			.createdAt(originalProduct.getCreatedAt()) // createdAt은 원래 값으로 유지
			.build();

		// When
		Throwable throwable = Assertions.catchThrowable(() -> originalProduct.update(updatedProduct));

		// then
		Assertions.assertThat(throwable)
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("이자율(interestRate)은 변경할 수 없습니다.");
	}

	@DisplayName("상품 수정 - 현금 상품은 이자 유형(interestType)을 변경할 수 없다")
	@Test
	void update_whenInterestTypeChanged_thenThrowException() {
		// Given
		FinancialProduct originalProduct = FinancialProductDataProvider.createCashProduct("user-1");
		FinancialProduct updatedProduct = createInvalidUpdatedCashProduct().toBuilder()
			.id(originalProduct.getId()) // id는 원래 값으로 유지
			.userId(originalProduct.getUserId()) // userId는 원래 값으로 유지
			.productInvestmentType(originalProduct.getProductInvestmentType())
			.name("Updated Cash Product") // 이름 변경
			.amount(ProductAmount.won(BigDecimal.valueOf(2_000_000L))) // 금액 변경
			.months(originalProduct.getMonths())
			.productAnnualInterestRate(originalProduct.getProductAnnualInterestRate())
			.interestType(InterestType.COMPOUND) // interestType 변경
			.taxType(originalProduct.getTaxType())
			.taxRate(originalProduct.getTaxRate())
			.startDate(originalProduct.getStartDate().plusDays(10)) // 시작 날짜 변경
			.createdAt(originalProduct.getCreatedAt()) // createdAt은 원래 값으로 유지
			.build();

		// When
		Throwable throwable = Assertions.catchThrowable(() -> originalProduct.update(updatedProduct));

		// then
		Assertions.assertThat(throwable)
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("이자 유형(interestType)은 변경할 수 없습니다.");
	}

	@DisplayName("상품 수정 - 현금 상품은 세금 유형(taxType)을 변경할 수 없다")
	@Test
	void update_whenTaxTypeChanged_thenThrowException() {
		// Given
		FinancialProduct originalProduct = FinancialProductDataProvider.createCashProduct("user-1");
		FinancialProduct updatedProduct = createInvalidUpdatedCashProduct().toBuilder()
			.id(originalProduct.getId()) // id는 원래 값으로 유지
			.userId(originalProduct.getUserId()) // userId는 원래 값으로 유지
			.productInvestmentType(originalProduct.getProductInvestmentType())
			.name("Updated Cash Product") // 이름 변경
			.amount(ProductAmount.won(BigDecimal.valueOf(2_000_000L))) // 금액 변경
			.months(originalProduct.getMonths())
			.productAnnualInterestRate(originalProduct.getProductAnnualInterestRate())
			.interestType(originalProduct.getInterestType())
			.taxType(TaxType.NON_TAX) // taxType 변경
			.taxRate(originalProduct.getTaxRate())
			.startDate(originalProduct.getStartDate().plusDays(10)) // 시작 날짜 변경
			.createdAt(originalProduct.getCreatedAt()) // createdAt은 원래 값으로 유지
			.build();

		// When
		Throwable throwable = Assertions.catchThrowable(() -> originalProduct.update(updatedProduct));

		// then
		Assertions.assertThat(throwable)
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("세금 유형(taxType)은 변경할 수 없습니다.");
	}

	@DisplayName("상품 수정 - 현금 상품은 세율(taxRate)을 변경할 수 없다")
	@Test
	void update_whenTaxRateChanged_thenThrowException() {
		// Given
		FinancialProduct originalProduct = FinancialProductDataProvider.createCashProduct("user-1");
		FinancialProduct updatedProduct = createInvalidUpdatedCashProduct().toBuilder()
			.id(originalProduct.getId()) // id는 원래 값으로 유지
			.userId(originalProduct.getUserId()) // userId는 원래 값으로 유지
			.productInvestmentType(originalProduct.getProductInvestmentType())
			.name("Updated Cash Product") // 이름 변경
			.amount(ProductAmount.won(BigDecimal.valueOf(2_000_000L))) // 금액 변경
			.months(originalProduct.getMonths())
			.productAnnualInterestRate(originalProduct.getProductAnnualInterestRate())
			.interestType(originalProduct.getInterestType())
			.taxType(originalProduct.getTaxType())
			.taxRate(new FixedTaxRate(BigDecimal.valueOf(0.1))) // taxRate 변경
			.startDate(originalProduct.getStartDate().plusDays(10)) // 시작 날짜 변경
			.createdAt(originalProduct.getCreatedAt()) // createdAt은 원래 값으로 유지
			.build();

		// When
		Throwable throwable = Assertions.catchThrowable(() -> originalProduct.update(updatedProduct));

		// then
		Assertions.assertThat(throwable)
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("세율(taxRate)은 변경할 수 없습니다.");
	}

	@DisplayName("상품 수정 - 다른 상품이 현금 상품으로 업데이트될 수 없다")
	@Test
	void update_whenOtherProductType_thenThrowException() {
		// Given
		FinancialProduct originalProduct = FinancialProductDataProvider.createCashProduct("user-1");
		FinancialProduct updatedProduct = DepositProduct.builder()
			.id(originalProduct.getId()) // id는 원래 값으로 유지
			.userId(originalProduct.getUserId()) // userId는 원래 값으로 유지
			.productInvestmentType(ProductInvestmentType.from(InvestmentType.DEPOSIT))
			.name("Updated Deposit Product") // 이름 변경
			.amount(ProductAmount.won(BigDecimal.valueOf(2_000_000L))) // 금액 변경
			.months(new ProductMonths(12)) // months 변경
			.productAnnualInterestRate(new ProductAnnualInterestRate(BigDecimal.valueOf(0.05)))
			.interestType(InterestType.COMPOUND) // interestType 변경
			.productInterestType(ProductInterestType.from(InterestType.COMPOUND))
			.taxType(TaxType.NON_TAX) // taxType 변경
			.taxRate(new FixedTaxRate(BigDecimal.ZERO)) // taxRate 변경
			.startDate(originalProduct.getStartDate().plusDays(10)) // 시작 날짜 변경
			.createdAt(originalProduct.getCreatedAt()) // createdAt은 원래 값으로 유지
			.build();

		// When
		Throwable throwable = Assertions.catchThrowable(() -> originalProduct.update(updatedProduct));

		// then
		Assertions.assertThat(throwable)
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("업데이트된 상품은 CashProduct여야 합니다.");
	}
}
