package co.invest72.investment.domain.investment;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Getter;

@Getter
public enum InvestmentType {
	CASH("현금", PeriodPolicy.INDEFINITE),
	DEPOSIT("예금", PeriodPolicy.STANDARD),
	SAVINGS("적금", PeriodPolicy.STANDARD),
	;

	private final String typeName;
	private final PeriodStrategy periodStrategy;

	InvestmentType(String typeName, PeriodStrategy periodStrategy) {
		this.typeName = typeName;
		this.periodStrategy = periodStrategy;
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
}
