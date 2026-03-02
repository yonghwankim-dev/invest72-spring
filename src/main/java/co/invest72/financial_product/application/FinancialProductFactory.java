package co.invest72.financial_product.application;

import org.springframework.stereotype.Component;

import co.invest72.common.time.LocalDateProvider;
import co.invest72.financial_product.domain.CashProduct;
import co.invest72.financial_product.domain.DepositProduct;
import co.invest72.financial_product.domain.FinancialProduct;
import co.invest72.financial_product.domain.ProductAmount;
import co.invest72.financial_product.domain.ProductMonths;
import co.invest72.financial_product.domain.ProductRate;
import co.invest72.financial_product.domain.SavingsProduct;
import co.invest72.financial_product.presentation.dto.request.FinancialProductRequestDto;
import co.invest72.investment.domain.interest.InterestType;
import co.invest72.investment.domain.investment.InvestmentType;
import co.invest72.investment.domain.investment.PaymentDay;
import co.invest72.investment.domain.tax.TaxType;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FinancialProductFactory {

	private final LocalDateProvider localDateProvider;

	/**
	 * 상품 ID 없이 금융 상품 생성 (신규 생성 시 사용)
	 * @param userId 사용자 ID
	 * @param dto 금융 상품 생성에 필요한 정보가 담긴 DTO
	 * @return 생성된 금융 상품 객체
	 */
	public FinancialProduct create(String userId, FinancialProductRequestDto dto) {
		String productId = null;
		return create(productId, userId, dto);
	}

	/**
	 * 상품 ID를 포함하여 금융 상품 생성 (업데이트 시 사용)
	 * @param productId 상품 ID (업데이트 시 기존 상품의 ID를 유지하기 위해 사용)
	 * @param userId 사용자 ID
	 * @param dto 금융 상품 생성에 필요한 정보가 담긴 DTO
	 * @return 생성된 금융 상품 객체
	 */
	public FinancialProduct create(String productId, String userId, FinancialProductRequestDto dto) {
		InvestmentType investmentType = InvestmentType.valueOf(dto.getInvestmentType());
		return switch (investmentType) {
			case CASH -> createCashProduct(productId, userId, dto);
			case DEPOSIT -> createDepositProduct(productId, userId, dto);
			case SAVINGS -> createSavingsProduct(productId, userId, dto);
		};
	}

	private FinancialProduct createCashProduct(String productId, String userId, FinancialProductRequestDto dto) {
		return CashProduct.builder()
			.id(productId)
			.userId(userId)
			.name(dto.getName())
			.investmentType(InvestmentType.valueOf(dto.getInvestmentType()))
			.amount(new ProductAmount(dto.getAmount()))
			.months(new ProductMonths(dto.getMonths()))
			.interestRate(new ProductRate(dto.getInterestRate()))
			.interestType(InterestType.valueOf(dto.getInterestType()))
			.taxType(TaxType.valueOf(dto.getTaxType()))
			.taxRate(new ProductRate(dto.getTaxRate()))
			.startDate(dto.getStartDate())
			.createdAt(localDateProvider.nowDateTime())
			.build();
	}

	private FinancialProduct createDepositProduct(String productId, String userId, FinancialProductRequestDto dto) {
		return DepositProduct.builder()
			.id(productId)
			.userId(userId)
			.name(dto.getName())
			.investmentType(InvestmentType.valueOf(dto.getInvestmentType()))
			.amount(new ProductAmount(dto.getAmount()))
			.months(new ProductMonths(dto.getMonths()))
			.interestRate(new ProductRate(dto.getInterestRate()))
			.interestType(InterestType.valueOf(dto.getInterestType()))
			.taxType(TaxType.valueOf(dto.getTaxType()))
			.taxRate(new ProductRate(dto.getTaxRate()))
			.startDate(dto.getStartDate())
			.createdAt(localDateProvider.nowDateTime())
			.build();
	}

	private FinancialProduct createSavingsProduct(String productId, String userId, FinancialProductRequestDto dto) {
		return SavingsProduct.builder()
			.id(productId)
			.userId(userId)
			.name(dto.getName())
			.investmentType(InvestmentType.valueOf(dto.getInvestmentType()))
			.amount(new ProductAmount(dto.getAmount()))
			.months(new ProductMonths(dto.getMonths()))
			.paymentDay(new PaymentDay(dto.getPaymentDay()))
			.interestRate(new ProductRate(dto.getInterestRate()))
			.interestType(InterestType.valueOf(dto.getInterestType()))
			.taxType(TaxType.valueOf(dto.getTaxType()))
			.taxRate(new ProductRate(dto.getTaxRate()))
			.startDate(dto.getStartDate())
			.createdAt(localDateProvider.nowDateTime())
			.build();
	}
}
