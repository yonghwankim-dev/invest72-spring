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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("DEPOSIT")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SuperBuilder(toBuilder = true)
public class DepositProduct extends FinancialProduct {
	protected DepositProduct(
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

	public DepositProduct(DepositProductBuilder<?, ?> b) {
		super(b);
	}

	@Override
	public BigDecimal getBalanceByLocalDate(LocalDate today) {
		return getInvestmentType().calculateBalance(this, today);
	}
}
