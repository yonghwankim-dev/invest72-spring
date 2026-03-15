package co.invest72.investment.domain.period;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import co.invest72.investment.domain.InstallmentInvestmentAmount;
import co.invest72.investment.domain.InvestPeriod;
import co.invest72.investment.domain.amount.MonthlyInstallmentInvestmentAmount;
import co.invest72.money.domain.Money;

class MonthlyInvestPeriodTest {

	private InvestPeriod investPeriod;

	@BeforeEach
	void setUp() {
		investPeriod = new MonthlyInvestPeriod(12);
	}

	@Test
	void created() {
		assertNotNull(investPeriod);
	}

	@Test
	void shouldCreated_whenMonthsIsZero() {
		investPeriod = new MonthlyInvestPeriod(0);
		assertNotNull(investPeriod);
	}

	@Test
	void shouldReturnMonths() {
		int actualMonths = investPeriod.getMonths();

		int expectedMonths = 12;
		assertEquals(expectedMonths, actualMonths);
	}

	@Test
	void shouldThrowException_whenInvestmentPeriodIsNegative() {
		assertThrows(IllegalArgumentException.class,
			() -> new MonthlyInvestPeriod(-1));
	}

	@Test
	void shouldReturnTotalPrincipal_givenMonthlyInvestment() {
		investPeriod = new MonthlyInvestPeriod(12);
		InstallmentInvestmentAmount investmentAmount = new MonthlyInstallmentInvestmentAmount(
			Money.won(BigDecimal.valueOf(1_000_000)));

		int totalPrincipal = investPeriod.getTotalPrincipal(investmentAmount);

		int expected = 12_000_000;
		assertEquals(expected, totalPrincipal);
	}
}
