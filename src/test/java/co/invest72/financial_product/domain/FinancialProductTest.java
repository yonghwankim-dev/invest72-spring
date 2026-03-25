package co.invest72.financial_product.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import co.invest72.investment.domain.interest.InterestType;
import co.invest72.investment.domain.investment.InvestmentType;
import co.invest72.investment.domain.tax.TaxType;
import source.FinancialProductDataProvider;

class FinancialProductTest {

	private FinancialProduct financialProduct;

	@DisplayName("예금 상품 진행률 계산 - 기준일자가 시작일자보다 이전인 경우 진행률은 0.0이 반환된다.")
	@Test
	void getProgressByLocalDate_whenStartDateIsAfterToday_thenReturnZero() {
		// Given
		financialProduct = FinancialProductDataProvider.createDepositProduct("user-1");
		// 시작일을 오늘보다 2개월 이전으로 설정
		LocalDate today = LocalDate.of(2026, 1, 1).minusMonths(2);

		// When
		BigDecimal progress = financialProduct.getProgressByLocalDate(today);

		// Then
		Assertions.assertThat(progress).isEqualByComparingTo(BigDecimal.ZERO);
	}

	@DisplayName("예금 상품 진행률 계산 - 기준일자가 만기일 이후인 경우 진행률은 1.0이 반환된다.")
	@Test
	void getProgressByLocalDate_whenExpirationDateIsBeforeToday_thenReturnOne() {
		// Given
		financialProduct = FinancialProductDataProvider.createDepositProduct("user-1");
		LocalDate today = LocalDate.of(2027, 1, 2);

		// When
		BigDecimal progress = financialProduct.getProgressByLocalDate(today);

		// Then
		Assertions.assertThat(progress).isEqualByComparingTo(BigDecimal.ONE);
	}

	@DisplayName("예금 상품 진행률 계산 - 기준일자가 만기일과 동일한 경우 진행률은 1.0이 반환된다.")
	@Test
	void getProgressByLocalDate_whenExpirationDateIsEqualToToday_thenReturnOne() {
		// Given
		financialProduct = FinancialProductDataProvider.createDepositProduct("user-1");
		LocalDate today = LocalDate.of(2027, 1, 1);

		// When
		BigDecimal progress = financialProduct.getProgressByLocalDate(today);

		// Then
		Assertions.assertThat(progress).isEqualByComparingTo(BigDecimal.ONE);
	}

	@DisplayName("예금 상품 진행률 계산 - 기준일자가 만기일 이전인 경우 진행률은 0.0과 1.0 사이의 값이 반환된다.")
	@Test
	void getProgressByLocalDate_whenExpirationDateIsAfterToday_thenReturnBetweenZeroAndOne() {
		// Given
		financialProduct = FinancialProductDataProvider.createDepositProduct("user-1");
		LocalDate today = LocalDate.of(2026, 2, 27);

		// When
		BigDecimal progress = financialProduct.getProgressByLocalDate(today);

		// Then
		Assertions.assertThat(progress).isEqualByComparingTo(BigDecimal.valueOf(0.16));
	}
	
	@DisplayName("적금 상품 진행률 계산 - 기준일자가 시작일자보다 이전인 경우 진행률은 0.0이 반환된다.")
	@Test
	void getProgressByLocalDate_whenStartDateIsAfterToday_thenReturnZeroForSavings() {
		// Given
		financialProduct = FinancialProductDataProvider.createSavingsProduct("user-1");
		// 시작일을 오늘보다 2개월 이전으로 설정
		LocalDate today = LocalDate.of(2026, 1, 1).minusMonths(2);

		// When
		BigDecimal progress = financialProduct.getProgressByLocalDate(today);

		// Then
		Assertions.assertThat(progress).isEqualByComparingTo(BigDecimal.ZERO);
	}

	@DisplayName("적금 상품 진행률 계산 - 기준일자가 만기일 이후인 경우 진행률은 1.0이 반환된다.")
	@Test
	void getProgressByLocalDate_whenExpirationDateIsBeforeToday_thenReturnOneForSavings() {
		// Given
		financialProduct = FinancialProductDataProvider.createSavingsProduct("user-1");
		LocalDate today = LocalDate.of(2027, 1, 2);

		// When
		BigDecimal progress = financialProduct.getProgressByLocalDate(today);

		// Then
		Assertions.assertThat(progress).isEqualByComparingTo(BigDecimal.ONE);
	}

	@DisplayName("적금 상품 진행률 계산 - 기준일자가 만기일과 동일한 경우 진행률은 1.0이 반환된다.")
	@Test
	void getProgressByLocalDate_whenExpirationDateIsEqualToToday_thenReturnOneForSavings() {
		// Given
		financialProduct = FinancialProductDataProvider.createSavingsProduct("user-1");
		LocalDate today = LocalDate.of(2027, 1, 1);

		// When
		BigDecimal progress = financialProduct.getProgressByLocalDate(today);

		// Then
		Assertions.assertThat(progress).isEqualByComparingTo(BigDecimal.ONE);
	}

	@DisplayName("적금 상품 진행률 계산 - 기준일자가 만기일 이전인 경우 진행률은 0.0과 1.0 사이의 값이 반환된다.")
	@Test
	void getProgressByLocalDate_whenExpirationDateIsAfterToday_thenReturnBetweenZeroAndOneForSavings() {
		// Given
		financialProduct = FinancialProductDataProvider.createSavingsProduct("user-1");
		LocalDate today = LocalDate.of(2026, 2, 27);

		// When
		BigDecimal progress = financialProduct.getProgressByLocalDate(today);

		// Then
		Assertions.assertThat(progress).isEqualByComparingTo(BigDecimal.valueOf(0.16));
	}

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
		FinancialProduct product = CashProduct.builder()
			.userId("user-1234")
			.name("현금 상품")
			.productInvestmentType(ProductInvestmentType.from(InvestmentType.CASH))
			.amount(ProductAmount.won(BigDecimal.valueOf(1_000_000L)))
			.months(new ProductMonths(0))
			.productAnnualInterestRate(new ProductAnnualInterestRate(BigDecimal.valueOf(0.0)))
			.productInterestType(ProductInterestType.from(InterestType.NONE))
			.productTaxType(ProductTaxType.from(TaxType.NONE))
			.productTaxRate(new ProductTaxRate(BigDecimal.ZERO))
			.startDate(LocalDate.of(2026, 1, 1))
			.createdAt(LocalDate.of(2026, 1, 1).atStartOfDay())
			.build();

		Assertions.assertThat(product).isNotNull();
	}
}
