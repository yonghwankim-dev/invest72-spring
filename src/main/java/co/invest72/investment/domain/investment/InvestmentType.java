package co.invest72.investment.domain.investment;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import lombok.Getter;

@Getter
public enum InvestmentType {
	CASH(
		"현금",
		(startDate, months) -> LocalDate.MAX,
		(startDate, expirationDate, today) -> BigDecimal.ONE
	),
	DEPOSIT(
		"예금",
		LocalDate::plusMonths,
		(startDate, expirationDate, today) -> {
			if (today.isBefore(startDate)) {
				return BigDecimal.ZERO;
			}
			if (today.isAfter(expirationDate)) {
				return BigDecimal.ONE;
			}
			long totalDays = startDate.until(expirationDate, ChronoUnit.DAYS);
			long elapsedDays = startDate.until(today, ChronoUnit.DAYS);
			return BigDecimal.valueOf(elapsedDays)
				.divide(BigDecimal.valueOf(totalDays), 4, RoundingMode.HALF_EVEN);
		}
	),
	SAVINGS(
		"적금",
		LocalDate::plusMonths,
		(startDate, expirationDate, today) -> {
			if (today.isBefore(startDate)) {
				return BigDecimal.ZERO;
			}
			if (today.isAfter(expirationDate)) {
				return BigDecimal.ONE;
			}
			long totalMonths = startDate.until(expirationDate, ChronoUnit.DAYS);
			long elapsedMonths = startDate.until(today, ChronoUnit.DAYS);
			return BigDecimal.valueOf(elapsedMonths)
				.divide(BigDecimal.valueOf(totalMonths), 4, RoundingMode.HALF_EVEN);
		});

	private final String typeName;

	private final ExpirationCalculator expirationCalculator;
	private final ProgressCalculator progressCalculator;

	InvestmentType(String typeName, ExpirationCalculator expirationCalculator, ProgressCalculator progressCalculator) {
		this.typeName = typeName;
		this.expirationCalculator = expirationCalculator;
		this.progressCalculator = progressCalculator;
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
		return expirationCalculator.calculate(startDate, months);
	}

	public BigDecimal calculateProgress(LocalDate startDate, LocalDate expirationDate, LocalDate today) {
		return progressCalculator.calculate(startDate, expirationDate, today);
	}
}
