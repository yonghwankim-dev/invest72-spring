package co.invest72.investment.domain.interest;

import static testutil.BigDecimalAssertion.*;

import java.math.BigDecimal;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import co.invest72.investment.domain.InterestRate;
import co.invest72.investment.domain.InvestPeriod;
import co.invest72.investment.domain.period.MonthlyInvestPeriod;
import co.invest72.money.domain.Money;

class AnnualInterestRateTest {

	private InterestRate interestRate;

	public static Stream<Arguments> totalGrowthFactorSource() {
		return Stream.of(
			Arguments.of(1, BigDecimal.valueOf(1.00416666667)),
			Arguments.of(2, BigDecimal.valueOf(1.00835069444)),
			Arguments.of(3, BigDecimal.valueOf(1.01255215567)),
			Arguments.of(12, BigDecimal.valueOf(1.05116189788)),
			Arguments.of(13, BigDecimal.valueOf(1.05554173912))
		);
	}

	@BeforeEach
	void setUp() {
		double annualRate = 0.05;
		interestRate = new AnnualInterestRate(annualRate);
	}

	@Test
	void shouldReturnAnnualRate_givenAnnualRateValue() {
		BigDecimal annualRate = interestRate.getAnnualRate();

		BigDecimal expectedAnnualRate = BigDecimal.valueOf(0.05);
		assertBigDecimalEquals(expectedAnnualRate, annualRate);
	}

	@Test
	void shouldReturnMonthlyRate_givenAnnualRateValue() {
		BigDecimal actualMonthlyRate = interestRate.getMonthlyRate();

		BigDecimal expectedMonthlyRate = BigDecimal.valueOf(0.004167);
		assertBigDecimalEquals(expectedMonthlyRate, actualMonthlyRate);
	}

	@Test
	void shouldThrowException_whenAnnualRateIsNegative() {
		Assertions.assertThrows(IllegalArgumentException.class,
			() -> new AnnualInterestRate(-0.01));
	}

	@DisplayName("객체 최대값 검증 - 연이율은 최대 999.99%까지 허용")
	@Test
	void shouldCreateAnnualInterestRate_givenMaxAnnualRate() {
		BigDecimal maxAnnualRate = BigDecimal.valueOf(9.9999);
		AnnualInterestRate annualInterestRate = new AnnualInterestRate(maxAnnualRate);

		assertBigDecimalEquals(maxAnnualRate, annualInterestRate.getAnnualRate());
	}

	@DisplayName("객체 최대값 검증 - 연이율은 최대 9.9999%까지 허용, 초과 시 예외 발생")
	@Test
	void shouldThrowException_whenAnnualRateExceedsMax() {
		Assertions.assertThrows(IllegalArgumentException.class,
			() -> new AnnualInterestRate(10.0));

	}

	@Test
	void shouldReturnGrowthFactor() {
		assertBigDecimalEquals(BigDecimal.valueOf(1.00416666667), interestRate.calTotalGrowthFactor(1));
		assertBigDecimalEquals(BigDecimal.valueOf(1.00835069444), interestRate.calTotalGrowthFactor(2));
		assertBigDecimalEquals(BigDecimal.valueOf(1.01255215567), interestRate.calTotalGrowthFactor(3));
		assertBigDecimalEquals(BigDecimal.valueOf(1.05116189788), interestRate.calTotalGrowthFactor(12));
		assertBigDecimalEquals(BigDecimal.valueOf(1.05554173912), interestRate.calTotalGrowthFactor(13));
	}

	@Test
	void shouldReturnMonthlyInterest() {
		int amount = 1_000_000;

		BigDecimal actualMonthlyInterest = interestRate.calMonthlyInterest(amount);

		BigDecimal expectedMonthlyInterest = BigDecimal.valueOf(4166.66666667);
		assertBigDecimalEquals(expectedMonthlyInterest, actualMonthlyInterest);
	}

	@Test
	void calMonthlyInterest_givenBigDecimalAmount() {
		BigDecimal amount = BigDecimal.valueOf(1_000_000);

		BigDecimal actualMonthlyInterest = interestRate.calMonthlyInterest(amount);

		BigDecimal expectedMonthlyInterest = BigDecimal.valueOf(4166.66666667);
		assertBigDecimalEquals(expectedMonthlyInterest, actualMonthlyInterest);
	}

	@Test
	void shouldReturnAnnualInterest() {
		Money amount = Money.won(BigDecimal.valueOf(1_000_000));

		Money actualAnnualInterest = interestRate.getAnnualInterest(amount);

		Money expectedAnnualInterest = Money.won(BigDecimal.valueOf(50_000));
		org.assertj.core.api.Assertions.assertThat(actualAnnualInterest).isEqualTo(expectedAnnualInterest);
	}

	@ParameterizedTest
	@MethodSource(value = "totalGrowthFactorSource")
	void shouldReturnTotalGrowthFactor(int month, BigDecimal expectedTotalGrowthFactor) {
		InvestPeriod investPeriod = new MonthlyInvestPeriod(month);

		assertBigDecimalEquals(expectedTotalGrowthFactor, interestRate.calTotalGrowthFactor(investPeriod));
	}
}
