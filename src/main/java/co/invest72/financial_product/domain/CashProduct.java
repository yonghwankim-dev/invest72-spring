package co.invest72.financial_product.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import co.invest72.investment.domain.interest.InterestType;
import co.invest72.investment.domain.investment.InvestmentType;
import co.invest72.investment.domain.tax.TaxType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("CASH")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CashProduct extends FinancialProduct {
	@Builder(toBuilder = true)
	protected CashProduct(
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
		LocalDateTime createdAt) {
		super(userId, name, investmentType, amount, months, interestRate, interestType, taxType, taxRate, startDate,
			createdAt);
	}

	@Override
	public BigDecimal getBalanceByLocalDate(LocalDate today) {
		return getInvestmentType().calculateBalance(this, today);
	}
}
