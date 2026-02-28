package co.invest72.investment.domain.amount;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import co.invest72.investment.domain.InstallmentInvestmentAmount;
import co.invest72.investment.domain.InterestRate;
import co.invest72.investment.domain.interest.AnnualInterestRate;
import testutil.BigDecimalAssertion;

class YearlyInstallmentInvestmentAmountTest {

	private InstallmentInvestmentAmount investmentAmount;

	@BeforeEach
	void setUp() {
		investmentAmount = new YearlyInstallmentInvestmentAmount(12_000_000);
	}

	@Test
	void created() {
		assertNotNull(investmentAmount);
	}

	@Test
	void shouldReturnAmount() {
		BigDecimal monthlyAmount = investmentAmount.getMonthlyAmount();

		BigDecimal expectedAmount = BigDecimal.valueOf(1_000_000);
		assertEquals(expectedAmount, monthlyAmount);
	}

	@Test
	void shouldReturnInterest() {
		InterestRate interestRate = new AnnualInterestRate(0.05);

		double interest = investmentAmount.calAnnualInterest(interestRate);

		double expectedInterest = 600_000;
		assertEquals(expectedInterest, interest, 0.001);
	}

	@Test
	void calMonthlyInterest_shouldReturnMonthlyInterest() {
		InterestRate interestRate = new AnnualInterestRate(0.05);

		BigDecimal monthlyInterest = investmentAmount.calMonthlyInterest(interestRate);

		BigDecimal expectedMonthlyInterest = BigDecimal.valueOf(50_000);
		BigDecimalAssertion.assertBigDecimalEquals(expectedMonthlyInterest, monthlyInterest);
	}
}
