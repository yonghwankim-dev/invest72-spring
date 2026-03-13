package co.invest72.investment.domain.amount;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import co.invest72.investment.domain.InterestRate;
import co.invest72.investment.domain.interest.AnnualInterestRate;
import co.invest72.money.domain.Money;
import testutil.BigDecimalAssertion;

class MonthlyInstallmentInvestmentAmountTest {

	private MonthlyInstallmentInvestmentAmount investmentAmount;

	@BeforeEach
	void setUp() {
		investmentAmount = new MonthlyInstallmentInvestmentAmount(Money.won(BigDecimal.valueOf(1_000_000)));
	}

	@Test
	void shouldReturnAnnualInterest() {
		InterestRate interestRate = new AnnualInterestRate(0.05);

		Money annualInterest = investmentAmount.calAnnualInterestMoney(interestRate);

		Money expectedAnnualInterest = Money.won(BigDecimal.valueOf(50_000));
		Assertions.assertEquals(expectedAnnualInterest, annualInterest);
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
		Money amount = investmentAmount.getMonthlyAmount();

		Money expected = Money.won(BigDecimal.valueOf(1_000_000));
		Assertions.assertEquals(expected, amount);
	}

	@Test
	void shouldThrowException_whenAmountIsNegative() {
		Assertions.assertThrows(IllegalArgumentException.class,
			() -> new MonthlyInstallmentInvestmentAmount(Money.won(BigDecimal.valueOf(-1_000_000))));
	}
}
