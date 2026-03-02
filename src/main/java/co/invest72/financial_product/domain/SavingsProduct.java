package co.invest72.financial_product.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import co.invest72.investment.domain.interest.InterestType;
import co.invest72.investment.domain.investment.InvestmentType;
import co.invest72.investment.domain.investment.PaymentDay;
import co.invest72.investment.domain.tax.TaxType;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("SAVINGS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SuperBuilder(toBuilder = true)
public class SavingsProduct extends FinancialProduct {
	@Embedded
	@AttributeOverride(name = "value", column = @Column(name = "payment_day", nullable = false))
	private PaymentDay paymentDay;

	public SavingsProduct(
		String userId,
		String name,
		InvestmentType investmentType,
		ProductAmount amount,
		ProductMonths months,
		ProductRate interestRate,
		InterestType interestType,
		TaxType taxType,
		ProductRate taxRate,
		LocalDate startDate,
		LocalDateTime createdAt,
		PaymentDay paymentDay) {
		super(userId, name, investmentType, amount, months, interestRate, interestType, taxType, taxRate,
			startDate, createdAt);
		this.paymentDay = paymentDay;
		validatePaymentDay();
	}

	private void validatePaymentDay() {
		this.getInvestmentType().validate(paymentDay);
	}

	@Override
	public BigDecimal getBalanceByLocalDate(LocalDate today) {
		return getInvestmentType().calculateBalance(this, today);
	}

	@Override
	public boolean isPaidOn(LocalDate today) {
		return paymentDay.isPaidOn(today);
	}
}
