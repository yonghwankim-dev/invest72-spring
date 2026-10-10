package co.invest72.investment.domain.amount;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import co.invest72.investment.domain.InterestRate;
import co.invest72.investment.domain.interest.AnnualInterestRate;
import co.invest72.money.domain.Money;

class MonthlyInstallmentInvestmentAmountTest {

	private MonthlyInstallmentInvestmentAmount investmentAmount;

	@BeforeEach
	void setUp() {
		investmentAmount = new MonthlyInstallmentInvestmentAmount(Money.won(BigDecimal.valueOf(1_000_000)));
	}

	@Test
	void shouldReturnAnnualInterest() {
		InterestRate interestRate = new AnnualInterestRate(0.05);

		Money annualInterest = investmentAmount.calAnnualInterest(interestRate);

		Money expectedAnnualInterest = Money.won(BigDecimal.valueOf(50_000));
		assertThat(annualInterest).isEqualTo(expectedAnnualInterest);
	}

	@Test
	void calMonthlyInterest_shouldReturnMonthlyInterest() {
		InterestRate interestRate = new AnnualInterestRate(0.05);

		Money monthlyInterest = investmentAmount.calMonthlyInterest(interestRate);

		Money expectedMonthlyInterest = Money.won(new BigDecimal("4166.666666666667"));
		assertThat(monthlyInterest).isEqualTo(expectedMonthlyInterest);
	}

	@Test
	void shouldReturnAmount() {
		Money amount = investmentAmount.getMonthlyAmount();

		Money expected = Money.won(BigDecimal.valueOf(1_000_000));
		assertThat(amount).isEqualTo(expected);
	}

	@Test
	void shouldThrowException_whenAmountIsNegative() {
		// given
		Money won = Money.won(BigDecimal.valueOf(-1_000_000));
		// when & then
		assertThatThrownBy(() -> new MonthlyInstallmentInvestmentAmount(won))
			.isInstanceOf(IllegalArgumentException.class);
	}
}
