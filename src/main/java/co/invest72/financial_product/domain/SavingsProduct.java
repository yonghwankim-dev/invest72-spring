package co.invest72.financial_product.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

import co.invest72.investment.domain.investment.InvestmentType;
import co.invest72.investment.domain.investment.PaymentDay;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("SAVINGS")
@Getter
@SuperBuilder(toBuilder = true)
public class SavingsProduct extends FinancialProduct {
	@Embedded
	@AttributeOverride(name = "value", column = @Column(name = "payment_day"))
	private PaymentDay paymentDay;

	protected SavingsProduct() {
	}

	// 빌더 패턴을 사용할 때 부모 클래스의 필드와 자식 클래스의 필드를 모두 초기화할 수 있도록 생성자 정의
	protected SavingsProduct(SavingsProductBuilder<?, ?> b) {
		super(b); // 부모 필드 초기화
		this.paymentDay = b.paymentDay; // 자식 필드 초기화
		validatePaymentDay();
	}

	private void validatePaymentDay() {
		InvestmentType.valueOf(getProductInvestmentType().getName()).validate(paymentDay);
	}

	@Override
	public void update(FinancialProduct updatedProduct) {
		SavingsProduct updatedSavings = validateOnUpdate(updatedProduct);
		super.update(updatedProduct);
		this.paymentDay = updatedSavings.getPaymentDay();
		validatePaymentDay();
	}

	private SavingsProduct validateOnUpdate(FinancialProduct updatedProduct) {
		if (!(updatedProduct instanceof SavingsProduct updatedSavings)) {
			throw new IllegalArgumentException("업데이트된 상품은 SavingsProduct여야 합니다.");
		}
		return updatedSavings;
	}

	@Override
	public BigDecimal getBalanceByLocalDate(LocalDate today) {
		return InvestmentType.valueOf(getProductInvestmentType().getName()).calculateBalance(this, today);
	}

	@Override
	public boolean isPaidOn(LocalDate today) {
		return paymentDay.isPaidOn(today);
	}

	@Override
	public Integer getPaymentDayValue() {
		return paymentDay.getValue();
	}
}
