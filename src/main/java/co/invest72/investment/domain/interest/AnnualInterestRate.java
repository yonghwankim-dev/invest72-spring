package co.invest72.investment.domain.interest;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Objects;

import co.invest72.investment.domain.InterestRate;
import co.invest72.investment.domain.InvestPeriod;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnnualInterestRate implements InterestRate {

	private static final BigDecimal MAX_RATE = new BigDecimal("9.9999");

	private BigDecimal value;

	public AnnualInterestRate(double value) {
		this(BigDecimal.valueOf(value));
	}

	public AnnualInterestRate(BigDecimal value) {
		this.value = value;
		validate(this.value);
	}

	private void validate(BigDecimal value) {
		if (value == null || value.compareTo(BigDecimal.ZERO) < 0 || value.compareTo(MAX_RATE) > 0) {
			BigDecimal maxRatePercent = MAX_RATE.multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_EVEN);
			throw new IllegalArgumentException("금리는 0% 이상 " + maxRatePercent + "% 이하이어야 합니다.");
		}
	}

	@Override
	public BigDecimal getAnnualRate() {
		return value;
	}

	@Override
	public BigDecimal getMonthlyRate() {
		return value.divide(BigDecimal.valueOf(12), MathContext.DECIMAL64);
	}

	@Override
	public BigDecimal getAnnualInterest(BigDecimal amount) {
		return amount.multiply(getAnnualRate());
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
		return value.compareTo(that.value) == 0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(value == null ? null : value.stripTrailingZeros());
	}
}
