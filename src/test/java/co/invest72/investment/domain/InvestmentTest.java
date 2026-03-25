package co.invest72.investment.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import co.invest72.financial_product.domain.DepositProduct;
import co.invest72.financial_product.domain.FinancialProduct;
import co.invest72.financial_product.domain.ProductAmount;
import co.invest72.financial_product.domain.ProductAnnualInterestRate;
import co.invest72.financial_product.domain.ProductInterestType;
import co.invest72.financial_product.domain.ProductInvestmentType;
import co.invest72.financial_product.domain.ProductMonths;
import co.invest72.financial_product.domain.ProductTaxRate;
import co.invest72.financial_product.domain.ProductTaxType;
import co.invest72.financial_product.domain.SavingsProduct;
import co.invest72.financial_product.infrastructure.mapper.ProductAmountMapper;
import co.invest72.investment.application.InvestmentFactory;
import co.invest72.investment.domain.interest.InterestType;
import co.invest72.investment.domain.investment.InvestmentType;
import co.invest72.investment.domain.investment.PaymentDay;
import co.invest72.investment.domain.tax.TaxType;

class InvestmentTest {

	private InvestmentFactory investmentFactory;

	@BeforeEach
	void setUp() {
		ProductAmountMapper productAmountMapper = new ProductAmountMapper();
		investmentFactory = new InvestmentFactory(productAmountMapper);
	}

	@DisplayName("예금 상품 수익 계산 - 최대값 검증")
	@Test
	void calculateDepositInvestmentProfit_whenMaxValues_thenCalculateCorrectly() {
		// Given
		FinancialProduct financialProduct = DepositProduct.builder()
			.userId("user-1")
			.name("정기예금")
			.productInvestmentType(ProductInvestmentType.from(InvestmentType.DEPOSIT))
			.amount(ProductAmount.won(new BigDecimal("10000000000000"))) // 10조
			.months(new ProductMonths(999 * 12))
			.productAnnualInterestRate(new ProductAnnualInterestRate(BigDecimal.valueOf(9.9999)))
			.productInterestType(ProductInterestType.from(InterestType.SIMPLE))
			.productTaxType(ProductTaxType.from(TaxType.STANDARD))
			.productTaxRate(new ProductTaxRate(BigDecimal.valueOf(0.154)))
			.startDate(LocalDate.of(2026, 1, 1))
			.createdAt(LocalDate.of(2026, 1, 1).atStartOfDay())
			.build();
		Investment investment = investmentFactory.createBy(financialProduct);
		// When
		BigDecimal totalInvestment = investment.getTotalInvestment().getValue();
		BigDecimal totalInterest = investment.getTotalInterest().getValue();
		BigDecimal totalTax = investment.getTotalTax().getValue();
		BigDecimal totalProfit = investment.getTotalProfit().getValue();

		// Then
		Assertions.assertThat(totalInvestment).isEqualByComparingTo(new BigDecimal("10000000000000"));
		Assertions.assertThat(totalInterest).isEqualByComparingTo(new BigDecimal("99899001000000000"));
		Assertions.assertThat(totalTax).isEqualByComparingTo(new BigDecimal("15384446154000000"));
		Assertions.assertThat(totalProfit).isEqualByComparingTo(new BigDecimal("84524554846000000"));
	}

	@DisplayName("적금 상품 수익 계산 - 최대값 검증")
	@Test
	void calculateSavingsInvestmentProfit_whenMaxValues_thenCalculateCorrectly() {
		// Given
		FinancialProduct financialProduct = SavingsProduct.builder()
			.userId("user-1")
			.name("적금 상품")
			.productInvestmentType(ProductInvestmentType.from(InvestmentType.SAVINGS))
			.amount(ProductAmount.won(new BigDecimal("10000000000000"))) // 10조
			.months(new ProductMonths(999 * 12))
			.paymentDay(new PaymentDay(15)) // 매월 15일 납입
			.productAnnualInterestRate(new ProductAnnualInterestRate(BigDecimal.valueOf(9.9999)))
			.productInterestType(ProductInterestType.from(InterestType.SIMPLE))
			.productTaxType(ProductTaxType.from(TaxType.STANDARD))
			.productTaxRate(new ProductTaxRate(BigDecimal.valueOf(0.154)))
			.startDate(LocalDate.of(2026, 1, 1))
			.createdAt(LocalDate.of(2026, 1, 1).atStartOfDay())
			.build();
		Investment investment = investmentFactory.createBy(financialProduct);
		// When
		BigDecimal totalInvestment = investment.getTotalInvestment().getValue();
		BigDecimal totalInterest = investment.getTotalInterest().getValue();
		BigDecimal totalTax = investment.getTotalTax().getValue();
		BigDecimal totalProfit = investment.getTotalProfit().getValue();

		// Then
		Assertions.assertThat(totalInvestment).isEqualByComparingTo(new BigDecimal("119880000000000000"));
		Assertions.assertThat(totalInterest).isEqualByComparingTo(new BigDecimal("598844561494500000000"));
		Assertions.assertThat(totalTax).isEqualByComparingTo(new BigDecimal("92222062470153000000"));
		Assertions.assertThat(totalProfit).isEqualByComparingTo(new BigDecimal("506742379024347000000"));
	}
}
