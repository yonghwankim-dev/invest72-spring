package co.invest72.investment.domain.amount;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import co.invest72.investment.domain.InterestRate;
import co.invest72.investment.domain.interest.AnnualInterestRate;
import testutil.BigDecimalAssertion;

class MonthlyInstallmentInvestmentAmountTest {

	private MonthlyInstallmentInvestmentAmount investmentAmount;

	@BeforeEach
	void setUp() {
		investmentAmount = new MonthlyInstallmentInvestmentAmount(1_000_000);
	}

	@Test
	void shouldReturnAnnualInterest() {
		InterestRate interestRate = new AnnualInterestRate(0.05);

		double annualInterest = investmentAmount.calAnnualInterest(interestRate);

		double expectedAnnualInterest = 50_000;
		assertEquals(expectedAnnualInterest, annualInterest, 0.001);
	}

	@Test
	void calMonthlyInterest_shouldReturnMonthlyInterest() {
		InterestRate interestRate = new AnnualInterestRate(0.05);

		BigDecimal monthlyInterest = investmentAmount.calMonthlyInterest(interestRate);

		BigDecimal expectedMonthlyInterest = BigDecimal.valueOf(4166.666667);
		BigDecimalAssertion.assertBigDecimalEquals(expectedMonthlyInterest, monthlyInterest);
	}

	@Test
	void shouldReturnAmount() {
		BigDecimal amount = investmentAmount.getMonthlyAmount();

		BigDecimal expected = BigDecimal.valueOf(1_000_000);
		Assertions.assertEquals(expected, amount);
	}

	@Test
	void shouldThrowException_whenAmountIsNegative() {
		Assertions.assertThrows(IllegalArgumentException.class,
			() -> new MonthlyInstallmentInvestmentAmount(-1_000_000));
	}
}
