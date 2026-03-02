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
		super.update(updateDeposit);
	}

	private void validateUpdate(FinancialProduct updatedProduct) {
		if (!(updatedProduct instanceof DepositProduct)) {
			throw new IllegalArgumentException("업데이트된 상품은 DepositProduct여야 합니다.");
		}
		// 변경 불가능한 필드의 변경을 요청하는 경우 예외가 발생해야 한다. 단, id와 createdAt 필드는 업데이트 시 무시되고 기존 값이 유지되므로,
		// id, createdAt 변경 요청은 예외가 발생하지 않고 무시된다.
		if (!getUserId().equals(updatedProduct.getUserId())) {
			throw new IllegalArgumentException("상품 소유자(userId)는 변경할 수 없습니다.");
		}
		if (!getInvestmentType().equals(updatedProduct.getInvestmentType())) {
			throw new IllegalArgumentException("투자 유형(InvestmentType)은 변경할 수 없습니다.");
		}
	}
}
