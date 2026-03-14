package co.invest72.investment.domain.investment;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import co.invest72.investment.domain.InterestRate;
import co.invest72.investment.domain.InvestPeriod;
import co.invest72.investment.domain.Investment;
import co.invest72.investment.domain.LumpSumInvestmentAmount;
import co.invest72.investment.domain.Taxable;
import co.invest72.investment.domain.TaxableFactory;
import co.invest72.investment.domain.amount.FixedDepositAmount;
import co.invest72.investment.domain.interest.AnnualInterestRate;
import co.invest72.investment.domain.period.YearlyInvestPeriod;
import co.invest72.investment.domain.tax.FixedTaxRate;
import co.invest72.investment.domain.tax.KoreanTaxableFactory;
import co.invest72.investment.domain.tax.TaxType;

class CompoundFixedDepositTest {

	private Investment investment;

	@BeforeEach
	void setUp() {
		LumpSumInvestmentAmount depositAmount = new FixedDepositAmount(BigDecimal.valueOf(1_000_000), "KRW");
		InvestPeriod investPeriod = new YearlyInvestPeriod(1);
		InterestRate interestRate = new AnnualInterestRate(0.05);
		TaxableFactory taxableFactory = new KoreanTaxableFactory();
		Taxable taxable = taxableFactory.createStandardTax(new FixedTaxRate(0.154));
		investment = CompoundFixedDeposit.builder()
			.investmentAmount(depositAmount)
			.investPeriod(investPeriod)
			.interestRate(interestRate)
			.taxable(taxable)
			.build();
	}

	@ParameterizedTest
	@CsvFileSource(files = "src/test/resources/compound_fixed_deposit_1y_5percent_standard_tax.csv", numLinesToSkip = 1)
	void shouldReturnInvestmentAmount(int month, int expectedPrincipal, int expectedInterest, int expectedTotalProfit) {
		BigDecimal principal = investment.getPrincipal(month).getValue();
		BigDecimal interest = investment.getInterest(month).getValue();
		BigDecimal totalProfit = investment.getProfitMoney(month).getValue();

		assertEquals(BigDecimal.valueOf(expectedPrincipal), principal);
		assertEquals(BigDecimal.valueOf(expectedInterest), interest);
		assertEquals(BigDecimal.valueOf(expectedTotalProfit), totalProfit);
	}

	@Test
	void getPrincipal() {
		BigDecimal principal = investment.getPrincipal().getValue();

		assertEquals(BigDecimal.valueOf(1_046_800), principal);
	}

	@Test
	void getPrincipal_whenMonthIsNegative() {
		BigDecimal principal = investment.getPrincipal(-1).getValue();

		assertEquals(BigDecimal.valueOf(1_000_000), principal);
	}

	@Test
	void getPrincipal_whenMonthIsZero() {
		BigDecimal principal = investment.getPrincipal(0).getValue();

		assertEquals(BigDecimal.valueOf(1_000_000), principal);
	}

	@Test
	void getPrincipal_whenMonthGreaterThanFinalMonth() {
		BigDecimal principal = investment.getPrincipal(13).getValue();

		assertEquals(BigDecimal.valueOf(1_046_800), principal);
	}

	@Test
	void getInterest() {
		BigDecimal interest = investment.getInterest().getValue();

		assertEquals(BigDecimal.valueOf(4_362), interest);
	}

	@Test
	void getInterest_whenMonthIsNegative() {
		BigDecimal interest = investment.getInterest(-1).getValue();

		assertEquals(BigDecimal.valueOf(0), interest);
	}

	@Test
	void getInterest_whenMonthIsZero() {
		BigDecimal interest = investment.getInterest(0).getValue();

		assertEquals(BigDecimal.valueOf(0), interest);
	}

	@Test
	void getInterest_whenMonthGreaterThanFinalMonth() {
		BigDecimal interest = investment.getInterest(13).getValue();

		assertEquals(BigDecimal.valueOf(4_362), interest);
	}

	@Test
	void getProfit() {
		BigDecimal totalProfit = investment.getProfit();

		assertEquals(BigDecimal.valueOf(1_051_162), totalProfit);
	}

	@Test
	void getTotalInvestment() {
		BigDecimal totalInvestment = investment.getTotalInvestment();

		assertEquals(BigDecimal.valueOf(1_000_000), totalInvestment);
	}

	@Test
	void getTotalInterest() {
		BigDecimal totalInterest = investment.getTotalInterest();

		assertEquals(BigDecimal.valueOf(51_162), totalInterest);
	}

	@Test
	void getTotalTax() {
		BigDecimal totalTax = investment.getTotalTax();

		assertEquals(BigDecimal.valueOf(7_879), totalTax);
	}

	@Test
	void getTaxType() {
		String taxType = investment.getTaxType();

		assertEquals(TaxType.STANDARD.getDescription(), taxType);
	}

	@Test
	void getPrincipalForYear_whenPeriodIs3Year() {
		investment = ((CompoundFixedDeposit)investment).toBuilder()
			.investPeriod(new YearlyInvestPeriod(3))
			.build();

		assertEquals(BigDecimal.valueOf(1_000_000), investment.getPrincipalForYear(-1));
		assertEquals(BigDecimal.valueOf(1_000_000), investment.getPrincipalForYear(0));
		assertEquals(BigDecimal.valueOf(1_000_000), investment.getPrincipalForYear(1));
		assertEquals(BigDecimal.valueOf(1_051_162), investment.getPrincipalForYear(2));
		assertEquals(BigDecimal.valueOf(1_104_941), investment.getPrincipalForYear(3));
		assertEquals(BigDecimal.valueOf(1_104_941), investment.getPrincipalForYear(4));
	}

	@Test
	void getInterestForYear_whenPeriodIs3Year() {
		investment = ((CompoundFixedDeposit)investment).toBuilder()
			.investPeriod(new YearlyInvestPeriod(3))
			.build();

		assertEquals(BigDecimal.valueOf(0), investment.getInterestForYear(-1));
		assertEquals(BigDecimal.valueOf(0), investment.getInterestForYear(0));
		assertEquals(BigDecimal.valueOf(51_162), investment.getInterestForYear(1));
		assertEquals(BigDecimal.valueOf(53_779), investment.getInterestForYear(2));
		assertEquals(BigDecimal.valueOf(56_531), investment.getInterestForYear(3));
		assertEquals(BigDecimal.valueOf(56_531), investment.getInterestForYear(4));
	}

	@Test
	void getProfitForYear_whenPeriodIs3Year() {
		investment = ((CompoundFixedDeposit)investment).toBuilder()
			.investPeriod(new YearlyInvestPeriod(3))
			.build();

		assertEquals(BigDecimal.valueOf(1_000_000), investment.getProfitForYear(-1));
		assertEquals(BigDecimal.valueOf(1_000_000), investment.getProfitForYear(0));
		assertEquals(BigDecimal.valueOf(1_051_162), investment.getProfitForYear(1));
		assertEquals(BigDecimal.valueOf(1_104_941), investment.getProfitForYear(2));
		assertEquals(BigDecimal.valueOf(1_161_472), investment.getProfitForYear(3));
		assertEquals(BigDecimal.valueOf(1_161_472), investment.getProfitForYear(4));
	}
}
