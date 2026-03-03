package co.invest72.investment.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import co.invest72.financial_product.domain.FinancialProduct;
import co.invest72.financial_product.domain.ProductAmount;
import co.invest72.financial_product.domain.ProductMonths;
import co.invest72.financial_product.domain.ProductRate;
import co.invest72.investment.application.InvestmentFactory;
import co.invest72.investment.domain.interest.InterestType;
import co.invest72.investment.domain.investment.InvestmentType;
import co.invest72.investment.domain.investment.PaymentDay;
import co.invest72.investment.domain.tax.TaxType;

class InvestmentTest {

	private InvestmentFactory investmentFactory;

	@BeforeEach
	void setUp() {
		investmentFactory = new InvestmentFactory();
	}

	@DisplayName("적금 상품 수익 계산 - 최대값 검증")
	@Test
	void calculateSavingsInvestmentProfit_whenMaxValues_thenCalculateCorrectly() {
		// Given
		FinancialProduct financialProduct = FinancialProduct.builder()
			.userId("user-1")
			.name("적금 상품")
			.investmentType(InvestmentType.SAVINGS)
			.amount(new ProductAmount(BigDecimal.valueOf(1_000_000L)))
			.months(new ProductMonths(999 * 12))
			.paymentDay(new PaymentDay(15)) // 매월 5일 납입
			.interestRate(new ProductRate(BigDecimal.valueOf(9.9999)))
			.interestType(InterestType.SIMPLE)
			.taxType(TaxType.STANDARD)
			.taxRate(new ProductRate(BigDecimal.valueOf(9.9999)))
			.startDate(LocalDate.of(2026, 1, 1))
			.createdAt(LocalDate.of(2026, 1, 1).atStartOfDay())
			.build();
		Investment investment = investmentFactory.createBy(financialProduct);
		// When
		BigDecimal totalInvestment = investment.getTotalInvestment();
		BigDecimal totalInterest = investment.getTotalInterest();
		BigDecimal totalTax = investment.getTotalTax();
		BigDecimal totalProfit = investment.getTotalProfit();

		// Then
		Assertions.assertThat(totalInvestment).isEqualByComparingTo(new BigDecimal("11988000000"));
	}

}
