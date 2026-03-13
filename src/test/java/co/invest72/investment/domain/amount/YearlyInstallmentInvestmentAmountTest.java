package co.invest72.investment.domain.amount;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import co.invest72.investment.domain.InstallmentInvestmentAmount;
import co.invest72.investment.domain.InterestRate;
import co.invest72.investment.domain.interest.AnnualInterestRate;
import co.invest72.money.domain.Money;

class YearlyInstallmentInvestmentAmountTest {

	private InstallmentInvestmentAmount investmentAmount;

	@BeforeEach
	void setUp() {
		investmentAmount = new YearlyInstallmentInvestmentAmount(Money.won(BigDecimal.valueOf(12_000_000)));
	}

	@Test
	void created() {
		assertNotNull(investmentAmount);
	}

	@Test
	void shouldReturnAmount() {
		Money monthlyAmount = investmentAmount.getMonthlyAmount();

		Money expectedAmount = Money.won(BigDecimal.valueOf(1_000_000));
		assertEquals(expectedAmount, monthlyAmount);
	}

	@Test
	void shouldReturnInterest() {
		InterestRate interestRate = new AnnualInterestRate(0.05);

		Money interest = investmentAmount.calAnnualInterest(interestRate);

		Money expectedInterest = Money.won(BigDecimal.valueOf(600_000));
		Assertions.assertThat(interest).isEqualTo(expectedInterest);
	}

	@Test
	void calMonthlyInterest_shouldReturnMonthlyInterest() {
		InterestRate interestRate = new AnnualInterestRate(0.05);

		Money monthlyInterest = investmentAmount.calMonthlyInterest(interestRate);

		Money expectedMonthlyInterest = Money.won(BigDecimal.valueOf(50_000));
		Assertions.assertThat(monthlyInterest).isEqualTo(expectedMonthlyInterest);
	}
}
