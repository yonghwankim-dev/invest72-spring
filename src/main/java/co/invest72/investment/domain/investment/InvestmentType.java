package co.invest72.investment.domain.investment;

import java.math.BigDecimal;
import java.time.LocalDate;

import co.invest72.financial_product.domain.FinancialProduct;
import lombok.Getter;

@Getter
public enum InvestmentType {
	CASH("현금", PeriodPolicy.INDEFINITE, BalancePolicy.FIXED, Boolean.FALSE),
	DEPOSIT("예금", PeriodPolicy.STANDARD, BalancePolicy.FIXED, Boolean.FALSE),
	SAVINGS("적금", PeriodPolicy.STANDARD, BalancePolicy.ACCUMULATIVE, Boolean.TRUE),
	;

	private final String typeName;
	private final PeriodStrategy periodStrategy;
	private final BalanceStrategy balanceStrategy;
	private final Boolean requiresPaymentDay; // paymentDay 요구 여부

	InvestmentType(String typeName, PeriodStrategy periodStrategy, BalanceStrategy balanceStrategy,
		Boolean requiresPaymentDay) {
		this.typeName = typeName;
		this.periodStrategy = periodStrategy;
		this.balanceStrategy = balanceStrategy;
		this.requiresPaymentDay = requiresPaymentDay;
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

	/**
	 * 투자 유형에 따른 납입일 유효성 검사
	 * @param paymentDay 검사할 납입일 (null 가능)
	 * @throws IllegalArgumentException 납입일이 요구되는 경우 null이거나, 요구되지 않는 경우 null이 아닌 경우 예외 발생
	 */
	public void validate(PaymentDay paymentDay) throws IllegalArgumentException {
		if (Boolean.TRUE.equals(requiresPaymentDay) && paymentDay == null) {
			throw new IllegalArgumentException(typeName + " 상품은 납입일이 반드시 필요합니다.");
		} else if (Boolean.TRUE.equals(!requiresPaymentDay) && paymentDay != null) {
			throw new IllegalArgumentException(typeName + " 상품은 납입일이 없어야 합니다.");
		}
	}
}
