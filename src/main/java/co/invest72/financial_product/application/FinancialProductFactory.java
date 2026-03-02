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

	public FinancialProduct create(String userId, FinancialProductRequestDto dto) {
		InvestmentType investmentType = InvestmentType.valueOf(dto.getInvestmentType());
		return switch (investmentType) {
			case CASH -> createCashProduct(userId, dto);
			case DEPOSIT -> createDepositProduct(userId, dto);
			case SAVINGS -> createSavingsProduct(userId, dto);
		};
	}

	private FinancialProduct createCashProduct(String userId, FinancialProductRequestDto dto) {
		return CashProduct.builder()
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

	private FinancialProduct createDepositProduct(String userId, FinancialProductRequestDto dto) {
		return DepositProduct.builder()
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

	private FinancialProduct createSavingsProduct(String userId, FinancialProductRequestDto dto) {
		return SavingsProduct.builder()
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
