package co.invest72.investment.domain.investment;

import java.time.LocalDate;

import lombok.Getter;

@Getter
public enum InvestmentType {
	CASH("현금", (startDate, months) -> LocalDate.MAX),
	DEPOSIT("예금", LocalDate::plusMonths),
	SAVINGS("적금", LocalDate::plusMonths);

	private final String typeName;

	private final ExpirationCalculator expirationCalculator;

	InvestmentType(String typeName, ExpirationCalculator expirationCalculator) {
		this.typeName = typeName;
		this.expirationCalculator = expirationCalculator;
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
		return expirationCalculator.calculateExpirationDate(startDate, months);
	}
}
