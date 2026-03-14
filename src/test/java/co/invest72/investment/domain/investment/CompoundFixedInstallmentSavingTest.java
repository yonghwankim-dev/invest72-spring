package co.invest72.investment.domain.investment;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

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
import co.invest72.investment.domain.period.YearlyInvestPeriod;
import co.invest72.investment.domain.tax.FixedTaxRate;
import co.invest72.investment.domain.tax.KoreanTaxableFactory;
import co.invest72.investment.domain.tax.TaxType;
import co.invest72.money.domain.Money;
import testutil.BigDecimalAssertion;

class CompoundFixedInstallmentSavingTest {

	private Investment investment;
	private TaxableFactory taxableFactory;

	@BeforeEach
	void setUp() {
		InstallmentInvestmentAmount investmentAmount = new MonthlyInstallmentInvestmentAmount(
			Money.won(BigDecimal.valueOf(1_000_000)));
		InvestPeriod investPeriod = new MonthlyInvestPeriod(12);
		InterestRate annualInterestRateRate = new AnnualInterestRate(0.05);
		taxableFactory = new KoreanTaxableFactory();
		Taxable taxable = taxableFactory.createStandardTax(new FixedTaxRate(0.154));
		investment = CompoundFixedInstallmentSaving.builder()
			.investmentAmount(investmentAmount)
			.investPeriod(investPeriod)
			.interestRate(annualInterestRateRate)
			.taxable(taxable)
			.build();

	}

	@ParameterizedTest
	@CsvFileSource(files = "src/test/resources/compound_fixed_installment_saving_1y_5percent_standard_tax.csv", numLinesToSkip = 1)
	void shouldReturnInvestmentAmount(int month, int expectedPrincipal, int expectedInterest, int expectedTotalProfit) {
		BigDecimal principal = investment.getPrincipal(month).getValue();
		BigDecimal interest = investment.getInterest(month).getValue();
		BigDecimal totalProfit = investment.getProfit(month).getValue();

		assertEquals(BigDecimal.valueOf(expectedPrincipal), principal);
		assertEquals(BigDecimal.valueOf(expectedInterest), interest);
		assertEquals(BigDecimal.valueOf(expectedTotalProfit), totalProfit);
	}

	@Test
	void getPrincipal() {
		BigDecimalAssertion.assertBigDecimalEquals(BigDecimal.valueOf(12_278_855),
			investment.getPrincipal().getValue());
	}

	@Test
	void getPrincipal_whenMonthIsZero_thenReturnPrincipal() {
		int months = 0;

		BigDecimal principal = investment.getPrincipal(months).getValue();

		assertEquals(BigDecimal.ZERO, principal);
	}

	@Test
	void getInterest() {
		assertEquals(BigDecimal.valueOf(51_162), investment.getInterest().getValue());
	}

	@Test
	void getInterest_whenMonthIsZero_thenReturnZeroInterest() {
		int months = 0;

		BigDecimal interest = investment.getInterest(months).getValue();

		assertEquals(BigDecimal.ZERO, interest);
	}

	@Test
	void getProfit() {
		assertEquals(BigDecimal.valueOf(12_330_017), investment.getProfit().getValue());
	}

	@Test
	void getTotalProfit_whenMonthIsZero_thenReturnZeroTotalProfit() {
		int months = 0;

		BigDecimal totalProfit = investment.getProfit(months).getValue();

		assertEquals(BigDecimal.ZERO, totalProfit);
	}

	@Test
	void getTotalInvestment() {
		assertEquals(BigDecimal.valueOf(12_000_000), investment.getTotalInvestment().getValue());
	}

	@Test
	void getTotalInterest() {
		assertEquals(BigDecimal.valueOf(330_017), investment.getTotalInterest().getValue());
	}

	@Test
	void getTotalTax() {
		assertEquals(BigDecimal.valueOf(50_823), investment.getTotalTax().getValue());
	}

	@Test
	void getTotalProfit() {
		assertEquals(BigDecimal.valueOf(12_330_017), investment.getProfit().getValue());
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

	@Test
	void getPrincipalForYear() {
		investment = ((CompoundFixedInstallmentSaving)investment).toBuilder()
			.investPeriod(new YearlyInvestPeriod(3))
			.build();

		assertEquals(BigDecimal.valueOf(0), investment.getPrincipalForYear(-1));
		assertEquals(BigDecimal.valueOf(0), investment.getPrincipalForYear(0));
		assertEquals(BigDecimal.valueOf(12_000_000), investment.getPrincipalForYear(1));
		assertEquals(BigDecimal.valueOf(24_330_017), investment.getPrincipalForYear(2));
		assertEquals(BigDecimal.valueOf(37_290_862), investment.getPrincipalForYear(3));
		assertEquals(BigDecimal.valueOf(37_290_862), investment.getPrincipalForYear(4));
	}

	@Test
	void getPrincipalForYear_whenPeriodIs25Months() {
		investment = ((CompoundFixedInstallmentSaving)investment).toBuilder()
			.investPeriod(new MonthlyInvestPeriod(25))
			.build();

		assertEquals(BigDecimal.valueOf(0), investment.getPrincipalForYear(-1));
		assertEquals(BigDecimal.valueOf(0), investment.getPrincipalForYear(0));
		assertEquals(BigDecimal.valueOf(12_000_000), investment.getPrincipalForYear(1));
		assertEquals(BigDecimal.valueOf(24_330_017), investment.getPrincipalForYear(2));
		assertEquals(BigDecimal.valueOf(26_290_862), investment.getPrincipalForYear(3));
		assertEquals(BigDecimal.valueOf(26_290_862), investment.getPrincipalForYear(4));
	}

	@Test
	void getInterestForYear() {
		investment = ((CompoundFixedInstallmentSaving)investment).toBuilder()
			.investPeriod(new YearlyInvestPeriod(3))
			.build();

		assertEquals(BigDecimal.valueOf(0), investment.getInterestForYear(-1));
		assertEquals(BigDecimal.valueOf(0), investment.getInterestForYear(0));
		assertEquals(BigDecimal.valueOf(330_017), investment.getInterestForYear(1));
		assertEquals(BigDecimal.valueOf(960_844), investment.getInterestForYear(2));
		assertEquals(BigDecimal.valueOf(1_623_946), investment.getInterestForYear(3));
		assertEquals(BigDecimal.valueOf(1_623_946), investment.getInterestForYear(4));
	}

	@Test
	void getInterestForYear_whenPeriodIs25Months() {
		Taxable nonTax = taxableFactory.createNonTax();
		investment = ((CompoundFixedInstallmentSaving)investment).toBuilder()
			.investPeriod(new MonthlyInvestPeriod(25))
			.taxable(nonTax)
			.build();

		assertEquals(BigDecimal.valueOf(0), investment.getInterestForYear(-1));
		assertEquals(BigDecimal.valueOf(0), investment.getInterestForYear(0));
		assertEquals(BigDecimal.valueOf(330_017), investment.getInterestForYear(1));
		assertEquals(BigDecimal.valueOf(960_844), investment.getInterestForYear(2));
		assertEquals(BigDecimal.valueOf(109_545), investment.getInterestForYear(3));
		assertEquals(BigDecimal.valueOf(109_545), investment.getInterestForYear(4));
	}

	@Test
	void getProfitForYear() {
		investment = ((CompoundFixedInstallmentSaving)investment).toBuilder()
			.investPeriod(new YearlyInvestPeriod(3))
			.build();

		assertEquals(BigDecimal.valueOf(0), investment.getProfitForYear(-1));
		assertEquals(BigDecimal.valueOf(0), investment.getProfitForYear(0));
		assertEquals(BigDecimal.valueOf(12_330_017), investment.getProfitForYear(1));
		assertEquals(BigDecimal.valueOf(25_290_862), investment.getProfitForYear(2));
		assertEquals(BigDecimal.valueOf(38_914_808), investment.getProfitForYear(3));
		assertEquals(BigDecimal.valueOf(38_914_808), investment.getProfitForYear(4));
	}

	@Test
	void getProfitForYear_whenPeriodIs25Months() {
		Taxable nonTax = taxableFactory.createNonTax();
		investment = ((CompoundFixedInstallmentSaving)investment).toBuilder()
			.investPeriod(new MonthlyInvestPeriod(25))
			.taxable(nonTax)
			.build();

		assertEquals(BigDecimal.valueOf(0), investment.getProfitForYear(-1));
		assertEquals(BigDecimal.valueOf(0), investment.getProfitForYear(0));
		assertEquals(BigDecimal.valueOf(12_330_017), investment.getProfitForYear(1));
		assertEquals(BigDecimal.valueOf(25_290_862), investment.getProfitForYear(2));
		assertEquals(BigDecimal.valueOf(26_400_407), investment.getProfitForYear(3));
		assertEquals(BigDecimal.valueOf(26_400_407), investment.getProfitForYear(4));
	}
}
