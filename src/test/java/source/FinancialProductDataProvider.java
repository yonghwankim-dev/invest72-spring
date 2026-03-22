package source;

import java.math.BigDecimal;
import java.time.LocalDate;

import co.invest72.financial_product.domain.CashProduct;
import co.invest72.financial_product.domain.DepositProduct;
import co.invest72.financial_product.domain.FinancialProduct;
import co.invest72.financial_product.domain.ProductAmount;
import co.invest72.financial_product.domain.ProductAnnualInterestRate;
import co.invest72.financial_product.domain.ProductInterestType;
import co.invest72.financial_product.domain.ProductInvestmentType;
import co.invest72.financial_product.domain.ProductMonths;
import co.invest72.financial_product.domain.ProductTaxType;
import co.invest72.financial_product.domain.SavingsProduct;
import co.invest72.investment.domain.interest.InterestType;
import co.invest72.investment.domain.investment.InvestmentType;
import co.invest72.investment.domain.investment.PaymentDay;
import co.invest72.investment.domain.tax.FixedTaxRate;
import co.invest72.investment.domain.tax.TaxType;

public class FinancialProductDataProvider {
	public static FinancialProduct createCashProduct(String userId) {
		return CashProduct.builder()
			.userId(userId)
			.name("현금 상품")
			.productInvestmentType(ProductInvestmentType.from(InvestmentType.CASH))
			.amount(ProductAmount.won(BigDecimal.valueOf(1_000_000L)))
			.months(new ProductMonths(0))
			.productAnnualInterestRate(new ProductAnnualInterestRate(BigDecimal.ZERO))
			.productInterestType(ProductInterestType.from(InterestType.NONE))
			.productTaxType(ProductTaxType.from(TaxType.NONE))
			.taxRate(new FixedTaxRate(BigDecimal.ZERO))
			.startDate(LocalDate.of(2026, 1, 1))
			.createdAt(LocalDate.of(2026, 1, 1).atStartOfDay())
			.build();
	}

	/**
	 * 예금 상품 생성 - 단리로 생성
	 * @param userId 사용자 ID
	 * @return 예금 상품 객체
	 */
	public static FinancialProduct createDepositProduct(String userId) {
		return createDepositProduct(userId, InterestType.SIMPLE);
	}

	/**
	 * 예금 상품 생성 - 이자 유형을 지정하여 생성
	 * @param userId 사용자 ID
	 * @param interestType 이자 유형 (단리 또는 복리)
	 * @return 예금 상품 객체
	 */
	public static FinancialProduct createDepositProduct(String userId, InterestType interestType) {
		return DepositProduct.builder()
			.userId(userId)
			.name("예금 상품")
			.productInvestmentType(ProductInvestmentType.from(InvestmentType.DEPOSIT))
			.amount(ProductAmount.won(BigDecimal.valueOf(1_000_000L)))
			.months(new ProductMonths(12))
			.productAnnualInterestRate(new ProductAnnualInterestRate(BigDecimal.valueOf(0.05)))
			.productInterestType(ProductInterestType.from(interestType))
			.productTaxType(ProductTaxType.from(TaxType.STANDARD))
			.taxRate(new FixedTaxRate(BigDecimal.valueOf(0.154)))
			.startDate(LocalDate.of(2026, 1, 1))
			.createdAt(LocalDate.of(2026, 1, 1).atStartOfDay())
			.build();
	}

	/**
	 * 적금 상품 생성 - 단리로 생성
	 * @param userId 사용자 ID
	 * @return 적금 상품 객체
	 */
	public static FinancialProduct createSavingsProduct(String userId) {
		return createSavingsProduct(userId, InterestType.SIMPLE);
	}

	/**
	 * 적금 상품 생성 - 이자 유형을 지정하여 생성<br>
	 * 매월 적립일은 기본적으로 15일
	 *
	 * @param userId 사용자 ID
	 * @param interestType 이자 유형 (단리 또는 복리)
	 * @return 적금 상품 객체
	 */
	public static FinancialProduct createSavingsProduct(String userId, InterestType interestType) {
		return SavingsProduct.builder()
			.userId(userId)
			.name("적금 상품")
			.productInvestmentType(ProductInvestmentType.from(InvestmentType.SAVINGS))
			.amount(ProductAmount.won(BigDecimal.valueOf(1_000_000L)))
			.months(new ProductMonths(12))
			.paymentDay(new PaymentDay(15)) // 매월 5일 납입
			.productAnnualInterestRate(new ProductAnnualInterestRate(BigDecimal.valueOf(0.05)))
			.productInterestType(ProductInterestType.from(interestType))
			.productTaxType(ProductTaxType.from(TaxType.STANDARD))
			.taxRate(new FixedTaxRate(BigDecimal.valueOf(0.154)))
			.startDate(LocalDate.of(2026, 1, 1))
			.createdAt(LocalDate.of(2026, 1, 1).atStartOfDay())
			.build();
	}
}
