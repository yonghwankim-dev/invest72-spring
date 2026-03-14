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

class SimpleFixedInstallmentSavingTest {

	private Investment investment;

	@BeforeEach
	void setUp() {
		InstallmentInvestmentAmount investmentAmount = new MonthlyInstallmentInvestmentAmount(
			Money.won(BigDecimal.valueOf(1_000_000)));
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
		BigDecimal principal = investment.getPrincipal(month).getValue();
		BigDecimal interest = investment.getInterest(month).getValue();
		BigDecimal totalProfit = investment.getProfit(month).getValue();

		assertEquals(BigDecimal.valueOf(expectedPrincipal), principal);
		assertEquals(BigDecimal.valueOf(expectedInterest), interest);
		assertEquals(BigDecimal.valueOf(expectedTotalProfit), totalProfit);
	}

	@Test
	void getPrincipal_whenMonthsIsFinalMonth() {
		assertEquals(BigDecimal.valueOf(12_275_000), investment.getPrincipal().getValue());
	}

	@Test
	void getPrincipal() {
		assertEquals(BigDecimal.valueOf(0), investment.getPrincipal(-1).getValue());
		assertEquals(BigDecimal.valueOf(0), investment.getPrincipal(0).getValue());
		assertEquals(BigDecimal.valueOf(1_000_000), investment.getPrincipal(1).getValue());
		assertEquals(BigDecimal.valueOf(2_004_167), investment.getPrincipal(2).getValue());
		assertEquals(BigDecimal.valueOf(3_012_500), investment.getPrincipal(3).getValue());
		assertEquals(BigDecimal.valueOf(4_025_000), investment.getPrincipal(4).getValue());
		assertEquals(BigDecimal.valueOf(5_041_667), investment.getPrincipal(5).getValue());
		assertEquals(BigDecimal.valueOf(6_062_500), investment.getPrincipal(6).getValue());
		assertEquals(BigDecimal.valueOf(7_087_500), investment.getPrincipal(7).getValue());
		assertEquals(BigDecimal.valueOf(8_116_667), investment.getPrincipal(8).getValue());
		assertEquals(BigDecimal.valueOf(9_150_000), investment.getPrincipal(9).getValue());
		assertEquals(BigDecimal.valueOf(10_187_500), investment.getPrincipal(10).getValue());
		assertEquals(BigDecimal.valueOf(11_229_167), investment.getPrincipal(11).getValue());
		assertEquals(BigDecimal.valueOf(12_275_000), investment.getPrincipal(12).getValue());
		assertEquals(BigDecimal.valueOf(12_275_000), investment.getPrincipal(13).getValue());
	}

	@Test
	void getInterest_whenMonthIsFinalMonth() {
		assertEquals(BigDecimal.valueOf(50_000), investment.getInterest().getValue());
	}

	@Test
	void getInterest() {
		assertEquals(BigDecimal.valueOf(0), investment.getInterest(-1).getValue());
		assertEquals(BigDecimal.valueOf(0), investment.getInterest(0).getValue());
		assertEquals(BigDecimal.valueOf(4_167), investment.getInterest(1).getValue());
		assertEquals(BigDecimal.valueOf(8_333), investment.getInterest(2).getValue());
		assertEquals(BigDecimal.valueOf(12_500), investment.getInterest(3).getValue());
		assertEquals(BigDecimal.valueOf(16_667), investment.getInterest(4).getValue());
		assertEquals(BigDecimal.valueOf(20_833), investment.getInterest(5).getValue());
		assertEquals(BigDecimal.valueOf(25_000), investment.getInterest(6).getValue());
		assertEquals(BigDecimal.valueOf(29_167), investment.getInterest(7).getValue());
		assertEquals(BigDecimal.valueOf(33_333), investment.getInterest(8).getValue());
		assertEquals(BigDecimal.valueOf(37_500), investment.getInterest(9).getValue());
		assertEquals(BigDecimal.valueOf(41_667), investment.getInterest(10).getValue());
		assertEquals(BigDecimal.valueOf(45_833), investment.getInterest(11).getValue());
		assertEquals(BigDecimal.valueOf(50_000), investment.getInterest(12).getValue());
		assertEquals(BigDecimal.valueOf(50_000), investment.getInterest(13).getValue());
	}

	@Test
	void getProfit_whenMonthIsFinalMonth() {
		assertEquals(BigDecimal.valueOf(12_325_000), investment.getProfit().getValue());
	}

	@Test
	void getProfit() {
		assertEquals(BigDecimal.valueOf(0), investment.getProfit(-1).getValue());
		assertEquals(BigDecimal.valueOf(0), investment.getProfit(0).getValue());
		assertEquals(BigDecimal.valueOf(1_004_167), investment.getProfit(1).getValue());
		assertEquals(BigDecimal.valueOf(2_012_500), investment.getProfit(2).getValue());
		assertEquals(BigDecimal.valueOf(3_025_000), investment.getProfit(3).getValue());
		assertEquals(BigDecimal.valueOf(4_041_667), investment.getProfit(4).getValue());
		assertEquals(BigDecimal.valueOf(5_062_500), investment.getProfit(5).getValue());
		assertEquals(BigDecimal.valueOf(6_087_500), investment.getProfit(6).getValue());
		assertEquals(BigDecimal.valueOf(7_116_667), investment.getProfit(7).getValue());
		assertEquals(BigDecimal.valueOf(8_150_000), investment.getProfit(8).getValue());
		assertEquals(BigDecimal.valueOf(9_187_500), investment.getProfit(9).getValue());
		assertEquals(BigDecimal.valueOf(10_229_167), investment.getProfit(10).getValue());
		assertEquals(BigDecimal.valueOf(11_275_000), investment.getProfit(11).getValue());
		assertEquals(BigDecimal.valueOf(12_325_000), investment.getProfit(12).getValue());
		assertEquals(BigDecimal.valueOf(12_325_000), investment.getProfit(13).getValue());
	}

	@Test
	void getTotalInvestment() {
		assertEquals(BigDecimal.valueOf(12_000_000), investment.getTotalInvestment().getValue());
	}

	@Test
	void getTotalInterest() {
		assertEquals(BigDecimal.valueOf(325_000), investment.getTotalInterest().getValue());
	}

	@Test
	void getTotalInterest_whenPeriodIs25Months() {
		investment = ((SimpleFixedInstallmentSaving)investment).toBuilder()
			.investPeriod(new MonthlyInvestPeriod(25))
			.build();

		assertEquals(BigDecimal.valueOf(1_354_167), investment.getTotalInterest().getValue());
	}

	@Test
	void getTotalTax() {
		assertEquals(BigDecimal.valueOf(50_050), investment.getTotalTax().getValue());
	}

	@Test
	void getTotalProfit() {
		assertEquals(BigDecimal.valueOf(12_274_950), investment.getTotalProfit().getValue());
	}

	@Test
	void getTotalProfit_whenPeriodIs25Months() {
		investment = ((SimpleFixedInstallmentSaving)investment).toBuilder()
			.investPeriod(new MonthlyInvestPeriod(25))
			.build();

		assertEquals(BigDecimal.valueOf(26_145_625), investment.getTotalProfit().getValue());
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
	void getPrincipalForYear_whenPeriodIs3Year() {
		investment = ((SimpleFixedInstallmentSaving)investment).toBuilder()
			.investPeriod(new YearlyInvestPeriod(3))
			.build();

		assertEquals(BigDecimal.valueOf(0), investment.getPrincipalForYear(-1).getValue());
		assertEquals(BigDecimal.valueOf(0), investment.getPrincipalForYear(0).getValue());
		assertEquals(BigDecimal.valueOf(12_000_000), investment.getPrincipalForYear(1).getValue());
		assertEquals(BigDecimal.valueOf(24_325_000), investment.getPrincipalForYear(2).getValue());
		assertEquals(BigDecimal.valueOf(37_250_000), investment.getPrincipalForYear(3).getValue());
		assertEquals(BigDecimal.valueOf(37_250_000), investment.getPrincipalForYear(4).getValue());
	}

	@Test
	void getPrincipalForYear_whenPeriodIs25Months() {
		investment = ((SimpleFixedInstallmentSaving)investment).toBuilder()
			.investPeriod(new MonthlyInvestPeriod(25))
			.build();

		assertEquals(BigDecimal.valueOf(0), investment.getPrincipalForYear(-1).getValue());
		assertEquals(BigDecimal.valueOf(0), investment.getPrincipalForYear(0).getValue());
		assertEquals(BigDecimal.valueOf(12_000_000), investment.getPrincipalForYear(1).getValue());
		assertEquals(BigDecimal.valueOf(24_325_000), investment.getPrincipalForYear(2).getValue());
		assertEquals(BigDecimal.valueOf(26_250_000), investment.getPrincipalForYear(3).getValue());
	}

	@Test
	void getInterestForYear_whenPeriodIs3Year() {
		investment = ((SimpleFixedInstallmentSaving)investment).toBuilder()
			.investPeriod(new YearlyInvestPeriod(3))
			.build();

		assertEquals(BigDecimal.valueOf(0), investment.getInterestForYear(-1).getValue());
		assertEquals(BigDecimal.valueOf(0), investment.getInterestForYear(0).getValue());
		assertEquals(BigDecimal.valueOf(325_000), investment.getInterestForYear(1).getValue());
		assertEquals(BigDecimal.valueOf(925_000), investment.getInterestForYear(2).getValue());
		assertEquals(BigDecimal.valueOf(1_525_000), investment.getInterestForYear(3).getValue());
		assertEquals(BigDecimal.valueOf(1_525_000), investment.getInterestForYear(4).getValue());
	}

	@Test
	void getInterestForYear_whenPeriodIs25Months() {
		investment = ((SimpleFixedInstallmentSaving)investment).toBuilder()
			.investPeriod(new MonthlyInvestPeriod(25))
			.build();

		assertEquals(BigDecimal.valueOf(0), investment.getInterestForYear(-1).getValue());
		assertEquals(BigDecimal.valueOf(0), investment.getInterestForYear(0).getValue());
		assertEquals(BigDecimal.valueOf(325_000), investment.getInterestForYear(1).getValue());
		assertEquals(BigDecimal.valueOf(925_000), investment.getInterestForYear(2).getValue());
		assertEquals(BigDecimal.valueOf(104_167), investment.getInterestForYear(3).getValue());
	}

	@Test
	void getProfitForYear_whenPeriodIs3Year() {
		investment = ((SimpleFixedInstallmentSaving)investment).toBuilder()
			.investPeriod(new YearlyInvestPeriod(3))
			.build();

		assertEquals(BigDecimal.valueOf(0), investment.getProfitForYear(-1).getValue());
		assertEquals(BigDecimal.valueOf(0), investment.getProfitForYear(0).getValue());
		assertEquals(BigDecimal.valueOf(12_325_000), investment.getProfitForYear(1).getValue());
		assertEquals(BigDecimal.valueOf(25_250_000), investment.getProfitForYear(2).getValue());
		assertEquals(BigDecimal.valueOf(38_775_000), investment.getProfitForYear(3).getValue());
		assertEquals(BigDecimal.valueOf(38_775_000), investment.getProfitForYear(4).getValue());
	}

	@Test
	void getProfitForYear_whenPeriodIs25Months() {
		investment = ((SimpleFixedInstallmentSaving)investment).toBuilder()
			.investPeriod(new MonthlyInvestPeriod(25))
			.build();

		assertEquals(BigDecimal.valueOf(0), investment.getProfitForYear(-1).getValue());
		assertEquals(BigDecimal.valueOf(0), investment.getProfitForYear(0).getValue());
		assertEquals(BigDecimal.valueOf(12_325_000), investment.getProfitForYear(1).getValue());
		assertEquals(BigDecimal.valueOf(25_250_000), investment.getProfitForYear(2).getValue());
		assertEquals(BigDecimal.valueOf(26_354_167), investment.getProfitForYear(3).getValue());
	}
}
