package co.invest72.investment.domain.investment;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import co.invest72.investment.domain.InstallmentInvestmentAmount;
import co.invest72.investment.domain.InterestRate;
import co.invest72.investment.domain.InvestPeriod;
import co.invest72.investment.domain.Investment;
import co.invest72.investment.domain.Taxable;
import co.invest72.investment.domain.TaxableFactory;
import co.invest72.investment.domain.amount.MonthlyInstallmentInvestmentAmount;
import co.invest72.investment.domain.interest.AnnualInterestRate;
import co.invest72.investment.domain.period.MonthlyInvestPeriod;
import co.invest72.investment.domain.tax.FixedTaxRate;
import co.invest72.investment.domain.tax.KoreanTaxableFactory;
import co.invest72.investment.domain.tax.TaxType;

class SimpleFixedInstallmentSavingTest {

	private Investment investment;

	@BeforeEach
	void setUp() {
		InstallmentInvestmentAmount investmentAmount = new MonthlyInstallmentInvestmentAmount(1_000_000);
		InvestPeriod investPeriod = new MonthlyInvestPeriod(12);
		InterestRate annualInterestRateRate = new AnnualInterestRate(0.05);
		TaxableFactory taxableFactory = new KoreanTaxableFactory();
		Taxable taxable = taxableFactory.createStandardTax(new FixedTaxRate(0.154));
		investment = new SimpleFixedInstallmentSaving(
			investmentAmount,
			investPeriod,
			annualInterestRateRate,
			taxable
		);
	}

	@ParameterizedTest
	@CsvFileSource(files = "src/test/resources/simple_fixed_installment_saving_1y_5percent_standard_tax.csv", numLinesToSkip = 1)
	void shouldReturnInvestmentAmount(int month, int expectedPrincipal, int expectedInterest, int expectedTotalProfit) {
		int principal = investment.getPrincipal(month);
		int interest = investment.getInterest(month);
		int totalProfit = investment.getProfit(month);

		assertEquals(expectedPrincipal, principal);
		assertEquals(expectedInterest, interest);
		assertEquals(expectedTotalProfit, totalProfit);
	}

	@Test
	void getPrincipal_whenMonthsIsFinalMonth() {
		int principalAmount = investment.getPrincipal();

		int expectedPrincipalAmount = 12_000_000;
		assertEquals(expectedPrincipalAmount, principalAmount);
	}

	@Test
	void getPrincipal() {
		assertEquals(0, investment.getPrincipal(-1));
		assertEquals(0, investment.getPrincipal(0));
		assertEquals(1_000_000, investment.getPrincipal(1));
	}

	@Test
	void getPrincipal_whenMonthIsGreaterThanFinalMonth_thenReturnPrincipal() {
		int months = 13;

		int principal = investment.getPrincipal(months);

		int expectedPrincipalAmount = 12_000_000;
		assertEquals(expectedPrincipalAmount, principal);
	}

	@Test
	void getInterest_whenMonthIsFinalMonth() {
		int interest = investment.getInterest();

		assertEquals(50_000, interest);
	}

	@Test
	void getInterest() {
		assertEquals(0, investment.getInterest(-1));
		assertEquals(0, investment.getInterest(0));
		assertEquals(4_167, investment.getInterest(1));
		assertEquals(8_351, investment.getInterest(2));
		assertEquals(12_552, investment.getInterest(3));
		assertEquals(16_771, investment.getInterest(4));
		assertEquals(21_008, investment.getInterest(5));
		assertEquals(25_262, investment.getInterest(6));
		assertEquals(29_534, investment.getInterest(7));
		assertEquals(33_824, investment.getInterest(8));
		assertEquals(38_131, investment.getInterest(9));
		assertEquals(42_457, investment.getInterest(10));
		assertEquals(46_800, investment.getInterest(11));
		assertEquals(51_162, investment.getInterest(12));
	}

	@Test
	void getInterest_whenMonthsIsNegative__thenReturnZeroInterest() {
		int months = -1;

		int interest = investment.getInterest(months);

		assertEquals(0, interest);
	}

	@Test
	void getInterest_whenMonthsIsZero_thenReturnZeroInterest() {
		int months = 0;

		int interest = investment.getInterest(months);

		assertEquals(0, interest);
	}

	@Test
	void getInterest_whenMonthsIsGreaterThanFinalMonth_thenReturnInterest() {
		int months = 13;

		int interest = investment.getInterest(months);

		assertEquals(50_000, interest);
	}

	@Test
	void getProfit() {
		int amount = investment.getProfit();

		assertEquals(12_050_000, amount);
	}

	@Test
	void getProfit_whenMonthsIsNegative__thenReturnZeroTotalProfit() {
		int months = -1;

		int profit = investment.getProfit(months);

		assertEquals(0, profit);
	}

	@Test
	void getProfit_whenMonthsIsZero_thenReturnZeroTotalProfit() {
		int months = 0;

		int totalProfit = investment.getProfit(months);

		assertEquals(0, totalProfit);
	}

	@Test
	void getProfit_whenMonthsIsGreaterThanFinalMonth_thenReturnTotalProfit() {
		int months = 13;

		int profit = investment.getProfit(months);

		int expectedTotalProfit = 12_050_000;
		assertEquals(expectedTotalProfit, profit);
	}

	@Test
	void getTotalInvestment() {
		int totalInvestment = investment.getTotalInvestment();

		int expectedTotalInvestment = 12_000_000;
		assertEquals(expectedTotalInvestment, totalInvestment);
	}

	@Test
	void getTotalPrincipal() {
		int totalPrincipal = investment.getTotalPrincipal();

		int expectedTotalPrincipal = 12_000_000;
		assertEquals(expectedTotalPrincipal, totalPrincipal);
	}

	@Test
	void getTotalInterest() {
		int totalInterest = investment.getTotalInterest();

		int expectedTotalInterest = 325_000;
		assertEquals(expectedTotalInterest, totalInterest);
	}

	@Test
	void getTotalTax() {
		int totalTax = investment.getTotalTax();

		int expectedTotalTax = 50_050;
		assertEquals(expectedTotalTax, totalTax);
	}

	@Test
	void getTotalProfit() {
		int totalProfit = investment.getTotalProfit();

		assertEquals(12_274_950, totalProfit);
	}

	@Test
	void getFinalMonth() {
		assertEquals(12, investment.getFinalMonth());
	}

	@Test
	void getTaxType() {
		String taxType = investment.getTaxType();

		assertEquals(TaxType.STANDARD.getDescription(), taxType);
	}
}
