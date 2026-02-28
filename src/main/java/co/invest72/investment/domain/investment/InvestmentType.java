package co.invest72.investment.domain.investment;

import java.math.BigDecimal;
import java.time.LocalDate;

import co.invest72.financial_product.domain.FinancialProduct;
import lombok.Getter;

@Getter
public enum InvestmentType {
	CASH("현금", PeriodPolicy.INDEFINITE, BalancePolicy.FIXED),
	DEPOSIT("예금", PeriodPolicy.STANDARD, BalancePolicy.FIXED),
	SAVINGS("적금", PeriodPolicy.STANDARD, BalancePolicy.ACCUMULATIVE),
	;

	private final String typeName;
	private final PeriodStrategy periodStrategy;
	private final BalanceStrategy balanceStrategy;

	InvestmentType(String typeName, PeriodStrategy periodStrategy, BalanceStrategy balanceStrategy) {
		this.typeName = typeName;
		this.periodStrategy = periodStrategy;
		this.balanceStrategy = balanceStrategy;
	}

	public static InvestmentType from(String type) {
		for (InvestmentType investmentType : values()) {
			if (investmentType.typeName.equalsIgnoreCase(type)) {
				return investmentType;
			}
		}
		throw new IllegalArgumentException("Unknown investment type: " + type);
	}

	public LocalDate calculateExpirationDate(LocalDate startDate, int months) {
		return periodStrategy.calculateExpiration(startDate, months);
	}

	public BigDecimal calculateProgress(LocalDate startDate, LocalDate expirationDate, LocalDate today) {
		return periodStrategy.calculateProgress(startDate, expirationDate, today);
	}

	public long calculateRemainingDays(LocalDate today, LocalDate expirationDate) {
		return periodStrategy.remainingDays(today, expirationDate);
	}

	public BigDecimal calculateBalance(FinancialProduct product, LocalDate today) {
		return balanceStrategy.calculate(product, today);
	}
}
