package co.invest72.financial_product.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("DEPOSIT")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SuperBuilder(toBuilder = true)
public class DepositProduct extends FinancialProduct {

	@Override
	public BigDecimal getBalanceByLocalDate(LocalDate today) {
		return getInvestmentType().calculateBalance(this, today);
	}

	@Override
	public void update(FinancialProduct updatedProduct) {
		validateUpdate(updatedProduct);
		// 수정 가능한 필드만 업데이트
		FinancialProduct updateDeposit = this.toBuilder()
			.name(updatedProduct.getName())
			.amount(updatedProduct.getAmount())
			.months(updatedProduct.getMonths())
			.interestRate(updatedProduct.getInterestRate())
			.interestType(updatedProduct.getInterestType())
			.taxType(updatedProduct.getTaxType())
			.taxRate(updatedProduct.getTaxRate())
			.startDate(updatedProduct.getStartDate())
			.build();
		super.update(updatedProduct);
	}

	private void validateUpdate(FinancialProduct updatedProduct) {
		if (!(updatedProduct instanceof DepositProduct)) {
			throw new IllegalArgumentException("업데이트된 상품은 DepositProduct여야 합니다.");
		}
	}
}
