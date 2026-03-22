package co.invest72.financial_product.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

import co.invest72.investment.domain.interest.AnnualInterestRate;
import co.invest72.investment.domain.investment.InvestmentType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("CASH")
@Getter
@SuperBuilder(toBuilder = true)
public class CashProduct extends FinancialProduct {

	protected CashProduct() {
	}

	@Override
	public BigDecimal getBalanceByLocalDate(LocalDate today) {
		return InvestmentType.valueOf(getProductInvestmentType().getName()).calculateBalance(this, today);
	}

	/**
	 * 현금 상품 수정
	 * - 현금 상품은 납입일이 없으므로, update 시 납입일은 무시되고 기존 값이 유지됩니다.
	 * - ID, userId, investmentType, createdAt는 수정 불가능하며, update 시 무시되고 기존 값이 유지됩니다.
	 * - 현금 상품은 투자 기간(months), 이자율(interestRate), 이자 유형(interestType), 세금 유형(taxType), 세율(taxRate)은 수정 불가능합니다.
	 * - 수정 가능한 필드는 상품명(name), 금액(amount), 시작 날짜(startDate)입니다.
	 * @param updatedProduct 수정된 현금 상품
	 */
	@Override
	public void update(FinancialProduct updatedProduct) {
		validateOnUpdate(updatedProduct);
		super.update(updatedProduct);
	}

	private void validateOnUpdate(FinancialProduct updatedProduct) {
		if (!(updatedProduct instanceof CashProduct)) {
			throw new IllegalArgumentException("업데이트된 상품은 CashProduct여야 합니다.");
		}
		if (!getMonths().equals(updatedProduct.getMonths())) {
			throw new IllegalArgumentException("투자 기간(months)은 변경할 수 없습니다.");
		}
		if (!new AnnualInterestRate(getProductAnnualInterestRate().getValue()).equals(
			new AnnualInterestRate(updatedProduct.getProductAnnualInterestRate().getValue()))) {
			throw new IllegalArgumentException("이자율(interestRate)은 변경할 수 없습니다.");
		}
		if (!getProductInterestType().equals(updatedProduct.getProductInterestType())) {
			throw new IllegalArgumentException("이자 유형(interestType)은 변경할 수 없습니다.");
		}
		if (!getTaxType().equals(updatedProduct.getTaxType())) {
			throw new IllegalArgumentException("세금 유형(taxType)은 변경할 수 없습니다.");
		}
		if (!getTaxRate().equals(updatedProduct.getTaxRate())) {
			throw new IllegalArgumentException("세율(taxRate)은 변경할 수 없습니다.");
		}
	}
}
