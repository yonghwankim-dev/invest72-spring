package co.invest72.investment.domain.interest;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Objects;

import co.invest72.investment.domain.InterestRate;
import co.invest72.investment.domain.InvestPeriod;

public class AnnualInterestRate implements InterestRate {

	private final double annualRate;

	public AnnualInterestRate(double annualRate) {
		this.annualRate = annualRate;
		if (this.annualRate < 0) {
			throw new IllegalArgumentException("Annual totalInterest rate must be non-negative.");
		}
		if (this.annualRate >= 1.0) {
			throw new IllegalArgumentException("Annual totalInterest rate must be less than 100%.");
		}
	}

	@Override
	public BigDecimal getAnnualRate() {
		return BigDecimal.valueOf(this.annualRate);
	}

	@Override
	public BigDecimal getMonthlyRate() {
		BigDecimal dividend = BigDecimal.valueOf(this.annualRate);
		BigDecimal divisor = BigDecimal.valueOf(12);
		return dividend.divide(divisor, MathContext.DECIMAL64);
	}

	@Override
	public BigDecimal getAnnualInterest(int amount) {
		return BigDecimal.valueOf(amount * this.annualRate);
	}

	@Override
	public BigDecimal calTotalGrowthFactor(InvestPeriod investPeriod) {
		return calTotalGrowthFactor(investPeriod.getMonths());
	}

	@Override
	public BigDecimal calTotalGrowthFactor(int month) {
		return calGrowthFactor().pow(month, MathContext.DECIMAL64);
	}

	/**
	 * 월 이자율을 적용한 성장 계수를 반환합니다.
	 * 성장 계수 = 1 + 월 이자율
	 */
	@Override
	public BigDecimal calGrowthFactor() {
		return getMonthlyRate().add(BigDecimal.ONE);
	}

	@Override
	public BigDecimal calMonthlyInterest(int amount) {
		return calMonthlyInterest(BigDecimal.valueOf(amount));
	}

	@Override
	public BigDecimal calMonthlyInterest(BigDecimal amount) {
		return amount.multiply(getMonthlyRate());
	}

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (object == null || getClass() != object.getClass())
			return false;
		AnnualInterestRate that = (AnnualInterestRate)object;
		return Double.compare(annualRate, that.annualRate) == 0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(annualRate);
	}
}
