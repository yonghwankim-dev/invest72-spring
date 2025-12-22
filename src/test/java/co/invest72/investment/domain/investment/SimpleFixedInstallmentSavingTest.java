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
		assertEquals(12_278_855, investment.getPrincipal());
	}

	@Test
	void getPrincipal() {
		assertEquals(0, investment.getPrincipal(-1));
		assertEquals(0, investment.getPrincipal(0));
		assertEquals(1_000_000, investment.getPrincipal(1));
		assertEquals(2_004_167, investment.getPrincipal(2));
		assertEquals(3_012_517, investment.getPrincipal(3));
		assertEquals(4_025_070, investment.getPrincipal(4));
		assertEquals(5_041_841, investment.getPrincipal(5));
		assertEquals(6_062_848, investment.getPrincipal(6));
		assertEquals(7_088_110, investment.getPrincipal(7));
		assertEquals(8_117_644, investment.getPrincipal(8));
		assertEquals(9_151_467, investment.getPrincipal(9));
		assertEquals(10_189_599, investment.getPrincipal(10));
		assertEquals(11_232_055, investment.getPrincipal(11));
		assertEquals(12_278_855, investment.getPrincipal(12));
		assertEquals(12_278_855, investment.getPrincipal(13));
	}

	@Test
	void getInterest_whenMonthIsFinalMonth() {
		assertEquals(51_162, investment.getInterest());
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
		assertEquals(51_162, investment.getInterest(13));
	}

	@Test
	void getProfit_whenMonthIsFinalMonth() {
		assertEquals(12_330_017, investment.getProfit());
	}

	@Test
	void getProfit() {
		assertEquals(0, investment.getProfit(-1));
		assertEquals(0, investment.getProfit(0));
		assertEquals(1_004_167, investment.getProfit(1));
		assertEquals(2_012_517, investment.getProfit(2));
		assertEquals(3_025_070, investment.getProfit(3));
		assertEquals(4_041_841, investment.getProfit(4));
		assertEquals(5_062_848, investment.getProfit(5));
		assertEquals(6_088_110, investment.getProfit(6));
		assertEquals(7_117_644, investment.getProfit(7));
		assertEquals(8_151_467, investment.getProfit(8));
		assertEquals(9_189_599, investment.getProfit(9));
		assertEquals(10_232_055, investment.getProfit(10));
		assertEquals(11_278_855, investment.getProfit(11));
		assertEquals(12_330_017, investment.getProfit(12));
		assertEquals(12_330_017, investment.getProfit(13));
	}

	@Test
	void getTotalInvestment() {
		assertEquals(12_000_000, investment.getTotalInvestment());
	}

	@Test
	void getTotalPrincipal() {
		assertEquals(12_278_855, investment.getTotalPrincipal());
	}

	@Test
	void getTotalInterest() {
		assertEquals(330_017, investment.getTotalInterest());
	}

	@Test
	void getTotalTax() {
		assertEquals(50_823, investment.getTotalTax());
	}

	@Test
	void getTotalProfit() {
		assertEquals(12_558_049, investment.getTotalProfit());
	}

	@Test
	void getFinalMonth() {
		assertEquals(12, investment.getFinalMonth());
	}

	@Test
	void getTaxType() {
		assertEquals(TaxType.STANDARD.getDescription(), investment.getTaxType());
	}

	@Test
	void getInterestForYear() {
		assertEquals(0, investment.getInterestForYear(0));
		assertEquals(325_000, investment.getInterestForYear(1));
	}

	@Test
	void getPrincipalForYear() {
		assertEquals(0, investment.getPrincipalForYear(0));
		assertEquals(12_000_000, investment.getPrincipalForYear(1));
	}
}
