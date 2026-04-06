package co.invest72.investment.domain.investment;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
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
import co.invest72.investment.domain.interest.InterestType;
import co.invest72.investment.domain.period.MonthlyInvestPeriod;
import co.invest72.investment.domain.period.YearlyInvestPeriod;
import co.invest72.investment.domain.tax.FixedTaxRate;
import co.invest72.investment.domain.tax.KoreanTaxableFactory;
import co.invest72.investment.domain.tax.TaxType;
import co.invest72.money.domain.Money;

class FixedSavingTest {

	@Nested
	class SimpleFixedSavingTest {
		private Investment investment;

		@BeforeEach
		void setUp() {
			InstallmentInvestmentAmount investmentAmount = new MonthlyInstallmentInvestmentAmount(
				Money.won(BigDecimal.valueOf(1_000_000)));
			InvestPeriod investPeriod = new MonthlyInvestPeriod(12);
			InterestRate annualInterestRateRate = new AnnualInterestRate(0.05);
			TaxableFactory taxableFactory = new KoreanTaxableFactory();
			Taxable taxable = taxableFactory.createStandardTax(new FixedTaxRate(0.154));
			investment = FixedSaving.builder()
				.investmentAmount(investmentAmount)
				.investPeriod(investPeriod)
				.interestRate(annualInterestRateRate)
				.interestType(InterestType.SIMPLE)
				.taxable(taxable)
				.build();
		}

		@ParameterizedTest
		@CsvFileSource(files = "src/test/resources/simple_fixed_installment_saving_1y_5percent_standard_tax.csv", numLinesToSkip = 1)
		void shouldReturnInvestmentAmount(int month, int expectedPrincipal, int expectedInterest,
			int expectedTotalProfit) {
			Money principal = investment.getPrincipal(month);
			Money interest = investment.getInterest(month);
			Money totalProfit = investment.getProfit(month);

			assertEquals(Money.won(BigDecimal.valueOf(expectedPrincipal)), principal);
			assertEquals(Money.won(BigDecimal.valueOf(expectedInterest)), interest);
			assertEquals(Money.won(BigDecimal.valueOf(expectedTotalProfit)), totalProfit);
		}

		@Test
		void getPrincipal_whenMonthsIsFinalMonth() {
			assertEquals(Money.won(BigDecimal.valueOf(12_275_000)), investment.getPrincipal());
		}

		@Test
		void getPrincipal() {
			assertEquals(Money.won(BigDecimal.valueOf(0)), investment.getPrincipal(-1));
			assertEquals(Money.won(BigDecimal.valueOf(0)), investment.getPrincipal(0));
			assertEquals(Money.won(BigDecimal.valueOf(1_000_000)), investment.getPrincipal(1));
			assertEquals(Money.won(BigDecimal.valueOf(2_004_167)), investment.getPrincipal(2));
			assertEquals(Money.won(BigDecimal.valueOf(3_012_500)), investment.getPrincipal(3));
			assertEquals(Money.won(BigDecimal.valueOf(4_025_000)), investment.getPrincipal(4));
			assertEquals(Money.won(BigDecimal.valueOf(5_041_667)), investment.getPrincipal(5));
			assertEquals(Money.won(BigDecimal.valueOf(6_062_500)), investment.getPrincipal(6));
			assertEquals(Money.won(BigDecimal.valueOf(7_087_500)), investment.getPrincipal(7));
			assertEquals(Money.won(BigDecimal.valueOf(8_116_667)), investment.getPrincipal(8));
			assertEquals(Money.won(BigDecimal.valueOf(9_150_000)), investment.getPrincipal(9));
			assertEquals(Money.won(BigDecimal.valueOf(10_187_500)), investment.getPrincipal(10));
			assertEquals(Money.won(BigDecimal.valueOf(11_229_167)), investment.getPrincipal(11));
			assertEquals(Money.won(BigDecimal.valueOf(12_275_000)), investment.getPrincipal(12));
			assertEquals(Money.won(BigDecimal.valueOf(12_275_000)), investment.getPrincipal(13));
		}

		@Test
		void getInterest_whenMonthIsFinalMonth() {
			assertEquals(Money.won(BigDecimal.valueOf(50_000)), investment.getInterest());
		}

		@Test
		void getInterest() {
			assertEquals(Money.won(BigDecimal.valueOf(0)), investment.getInterest(-1));
			assertEquals(Money.won(BigDecimal.valueOf(0)), investment.getInterest(0));
			assertEquals(Money.won(BigDecimal.valueOf(4_167)), investment.getInterest(1));
			assertEquals(Money.won(BigDecimal.valueOf(8_333)), investment.getInterest(2));
			assertEquals(Money.won(BigDecimal.valueOf(12_500)), investment.getInterest(3));
			assertEquals(Money.won(BigDecimal.valueOf(16_667)), investment.getInterest(4));
			assertEquals(Money.won(BigDecimal.valueOf(20_833)), investment.getInterest(5));
			assertEquals(Money.won(BigDecimal.valueOf(25_000)), investment.getInterest(6));
			assertEquals(Money.won(BigDecimal.valueOf(29_167)), investment.getInterest(7));
			assertEquals(Money.won(BigDecimal.valueOf(33_333)), investment.getInterest(8));
			assertEquals(Money.won(BigDecimal.valueOf(37_500)), investment.getInterest(9));
			assertEquals(Money.won(BigDecimal.valueOf(41_667)), investment.getInterest(10));
			assertEquals(Money.won(BigDecimal.valueOf(45_833)), investment.getInterest(11));
			assertEquals(Money.won(BigDecimal.valueOf(50_000)), investment.getInterest(12));
			assertEquals(Money.won(BigDecimal.valueOf(50_000)), investment.getInterest(13));
		}

		@Test
		void getProfit_whenMonthIsFinalMonth() {
			assertEquals(Money.won(BigDecimal.valueOf(12_325_000)), investment.getProfit());
		}

		@Test
		void getProfit() {
			assertEquals(Money.won(BigDecimal.valueOf(0)), investment.getProfit(-1));
			assertEquals(Money.won(BigDecimal.valueOf(0)), investment.getProfit(0));
			assertEquals(Money.won(BigDecimal.valueOf(1_004_167)), investment.getProfit(1));
			assertEquals(Money.won(BigDecimal.valueOf(2_012_500)), investment.getProfit(2));
			assertEquals(Money.won(BigDecimal.valueOf(3_025_000)), investment.getProfit(3));
			assertEquals(Money.won(BigDecimal.valueOf(4_041_667)), investment.getProfit(4));
			assertEquals(Money.won(BigDecimal.valueOf(5_062_500)), investment.getProfit(5));
			assertEquals(Money.won(BigDecimal.valueOf(6_087_500)), investment.getProfit(6));
			assertEquals(Money.won(BigDecimal.valueOf(7_116_667)), investment.getProfit(7));
			assertEquals(Money.won(BigDecimal.valueOf(8_150_000)), investment.getProfit(8));
			assertEquals(Money.won(BigDecimal.valueOf(9_187_500)), investment.getProfit(9));
			assertEquals(Money.won(BigDecimal.valueOf(10_229_167)), investment.getProfit(10));
			assertEquals(Money.won(BigDecimal.valueOf(11_275_000)), investment.getProfit(11));
			assertEquals(Money.won(BigDecimal.valueOf(12_325_000)), investment.getProfit(12));
			assertEquals(Money.won(BigDecimal.valueOf(12_325_000)), investment.getProfit(13));
		}

		@Test
		void getTotalInvestment() {
			assertEquals(Money.won(BigDecimal.valueOf(12_000_000)), investment.getTotalInvestment());
		}

		@Test
		void getTotalInterest() {
			assertEquals(Money.won(BigDecimal.valueOf(325_000)), investment.getTotalInterest());
		}

		@Test
		void getTotalInterest_whenPeriodIs25Months() {
			investment = ((FixedSaving)investment).toBuilder()
				.investPeriod(new MonthlyInvestPeriod(25))
				.build();

			assertEquals(Money.won(BigDecimal.valueOf(1_354_167)), investment.getTotalInterest());
		}

		@Test
		void getTotalTax() {
			assertEquals(Money.won(BigDecimal.valueOf(50_050)), investment.getTotalTax());
		}

		@Test
		void getTotalProfit() {
			assertEquals(Money.won(BigDecimal.valueOf(12_274_950)), investment.getTotalProfit());
		}

		@Test
		void getTotalProfit_whenPeriodIs25Months() {
			investment = ((FixedSaving)investment).toBuilder()
				.investPeriod(new MonthlyInvestPeriod(25))
				.build();

			assertEquals(Money.won(BigDecimal.valueOf(26_145_625)), investment.getTotalProfit());
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
			investment = ((FixedSaving)investment).toBuilder()
				.investPeriod(new YearlyInvestPeriod(3))
				.build();

			assertEquals(Money.won(BigDecimal.valueOf(0)), investment.getPrincipalForYear(-1));
			assertEquals(Money.won(BigDecimal.valueOf(0)), investment.getPrincipalForYear(0));
			assertEquals(Money.won(BigDecimal.valueOf(12_000_000)), investment.getPrincipalForYear(1));
			assertEquals(Money.won(BigDecimal.valueOf(24_325_000)), investment.getPrincipalForYear(2));
			assertEquals(Money.won(BigDecimal.valueOf(37_250_000)), investment.getPrincipalForYear(3));
			assertEquals(Money.won(BigDecimal.valueOf(37_250_000)), investment.getPrincipalForYear(4));
		}

		@Test
		void getPrincipalForYear_whenPeriodIs25Months() {
			investment = ((FixedSaving)investment).toBuilder()
				.investPeriod(new MonthlyInvestPeriod(25))
				.build();

			assertEquals(Money.won(BigDecimal.valueOf(0)), investment.getPrincipalForYear(-1));
			assertEquals(Money.won(BigDecimal.valueOf(0)), investment.getPrincipalForYear(0));
			assertEquals(Money.won(BigDecimal.valueOf(12_000_000)), investment.getPrincipalForYear(1));
			assertEquals(Money.won(BigDecimal.valueOf(24_325_000)), investment.getPrincipalForYear(2));
			assertEquals(Money.won(BigDecimal.valueOf(26_250_000)), investment.getPrincipalForYear(3));
		}

		@Test
		void getInterestForYear_whenPeriodIs3Year() {
			investment = ((FixedSaving)investment).toBuilder()
				.investPeriod(new YearlyInvestPeriod(3))
				.build();

			assertEquals(Money.won(BigDecimal.valueOf(0)), investment.getInterestForYear(-1));
			assertEquals(Money.won(BigDecimal.valueOf(0)), investment.getInterestForYear(0));
			assertEquals(Money.won(BigDecimal.valueOf(325_000)), investment.getInterestForYear(1));
			assertEquals(Money.won(BigDecimal.valueOf(925_000)), investment.getInterestForYear(2));
			assertEquals(Money.won(BigDecimal.valueOf(1_525_000)), investment.getInterestForYear(3));
			assertEquals(Money.won(BigDecimal.valueOf(1_525_000)), investment.getInterestForYear(4));
		}

		@Test
		void getInterestForYear_whenPeriodIs25Months() {
			investment = ((FixedSaving)investment).toBuilder()
				.investPeriod(new MonthlyInvestPeriod(25))
				.build();

			assertEquals(Money.won(BigDecimal.valueOf(0)), investment.getInterestForYear(-1));
			assertEquals(Money.won(BigDecimal.valueOf(0)), investment.getInterestForYear(0));
			assertEquals(Money.won(BigDecimal.valueOf(325_000)), investment.getInterestForYear(1));
			assertEquals(Money.won(BigDecimal.valueOf(925_000)), investment.getInterestForYear(2));
			assertEquals(Money.won(BigDecimal.valueOf(104_167)), investment.getInterestForYear(3));
		}

		@Test
		void getProfitForYear_whenPeriodIs3Year() {
			investment = ((FixedSaving)investment).toBuilder()
				.investPeriod(new YearlyInvestPeriod(3))
				.build();

			assertEquals(Money.won(BigDecimal.valueOf(0)), investment.getProfitForYear(-1));
			assertEquals(Money.won(BigDecimal.valueOf(0)), investment.getProfitForYear(0));
			assertEquals(Money.won(BigDecimal.valueOf(12_325_000)), investment.getProfitForYear(1));
			assertEquals(Money.won(BigDecimal.valueOf(25_250_000)), investment.getProfitForYear(2));
			assertEquals(Money.won(BigDecimal.valueOf(38_775_000)), investment.getProfitForYear(3));
			assertEquals(Money.won(BigDecimal.valueOf(38_775_000)), investment.getProfitForYear(4));
		}

		@Test
		void getProfitForYear_whenPeriodIs25Months() {
			investment = ((FixedSaving)investment).toBuilder()
				.investPeriod(new MonthlyInvestPeriod(25))
				.build();

			assertEquals(Money.won(BigDecimal.valueOf(0)), investment.getProfitForYear(-1));
			assertEquals(Money.won(BigDecimal.valueOf(0)), investment.getProfitForYear(0));
			assertEquals(Money.won(BigDecimal.valueOf(12_325_000)), investment.getProfitForYear(1));
			assertEquals(Money.won(BigDecimal.valueOf(25_250_000)), investment.getProfitForYear(2));
			assertEquals(Money.won(BigDecimal.valueOf(26_354_167)), investment.getProfitForYear(3));
		}
	}

	@Nested
	class CompoundFixedSavingTest {
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
			investment = FixedSaving.builder()
				.investmentAmount(investmentAmount)
				.investPeriod(investPeriod)
				.interestRate(annualInterestRateRate)
				.interestType(InterestType.COMPOUND)
				.taxable(taxable)
				.build();

		}

		@ParameterizedTest
		@CsvFileSource(files = "src/test/resources/compound_fixed_installment_saving_1y_5percent_standard_tax.csv", numLinesToSkip = 1)
		void shouldReturnInvestmentAmount(int month, int expectedPrincipal, int expectedInterest,
			int expectedTotalProfit) {
			Money principal = investment.getPrincipal(month);
			Money interest = investment.getInterest(month);
			Money totalProfit = investment.getProfit(month);

			assertEquals(Money.won(BigDecimal.valueOf(expectedPrincipal)), principal);
			assertEquals(Money.won(BigDecimal.valueOf(expectedInterest)), interest);
			assertEquals(Money.won(BigDecimal.valueOf(expectedTotalProfit)), totalProfit);
		}

		@Test
		void getPrincipal() {
			// when
			Money principal = investment.getPrincipal();
			// then
			assertEquals(Money.won(BigDecimal.valueOf(12_278_855)), principal);
		}

		@Test
		void getPrincipal_whenMonthIsZero_thenReturnPrincipal() {
			int months = 0;

			Money principal = investment.getPrincipal(months);

			assertEquals(Money.won(BigDecimal.ZERO), principal);
		}

		@Test
		void getInterest() {
			assertEquals(Money.won(BigDecimal.valueOf(51_162)), investment.getInterest());
		}

		@Test
		void getInterest_whenMonthIsZero_thenReturnZeroInterest() {
			int months = 0;

			Money interest = investment.getInterest(months);

			assertEquals(Money.won(BigDecimal.ZERO), interest);
		}

		@Test
		void getProfit() {
			assertEquals(Money.won(BigDecimal.valueOf(12_330_017)), investment.getProfit());
		}

		@Test
		void getTotalProfit_whenMonthIsZero_thenReturnZeroTotalProfit() {
			int months = 0;

			Money totalProfit = investment.getProfit(months);

			assertEquals(Money.won(BigDecimal.ZERO), totalProfit);
		}

		@Test
		void getTotalInvestment() {
			assertEquals(Money.won(BigDecimal.valueOf(12_000_000)), investment.getTotalInvestment());
		}

		@Test
		void getTotalInterest() {
			assertEquals(Money.won(BigDecimal.valueOf(330_017)), investment.getTotalInterest());
		}

		@Test
		void getTotalTax() {
			assertEquals(Money.won(BigDecimal.valueOf(50_823)), investment.getTotalTax());
		}

		@Test
		void getTotalProfit() {
			assertEquals(Money.won(BigDecimal.valueOf(12_330_017)), investment.getProfit());
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
			investment = ((FixedSaving)investment).toBuilder()
				.investPeriod(new YearlyInvestPeriod(3))
				.build();

			assertEquals(Money.won(BigDecimal.valueOf(0)), investment.getPrincipalForYear(-1));
			assertEquals(Money.won(BigDecimal.valueOf(0)), investment.getPrincipalForYear(0));
			assertEquals(Money.won(BigDecimal.valueOf(12_000_000)), investment.getPrincipalForYear(1));
			assertEquals(Money.won(BigDecimal.valueOf(24_330_017)), investment.getPrincipalForYear(2));
			assertEquals(Money.won(BigDecimal.valueOf(37_290_862)), investment.getPrincipalForYear(3));
			assertEquals(Money.won(BigDecimal.valueOf(37_290_862)), investment.getPrincipalForYear(4));
		}

		@Test
		void getPrincipalForYear_whenPeriodIs25Months() {
			investment = ((FixedSaving)investment).toBuilder()
				.investPeriod(new MonthlyInvestPeriod(25))
				.build();

			assertEquals(Money.won(BigDecimal.valueOf(0)), investment.getPrincipalForYear(-1));
			assertEquals(Money.won(BigDecimal.valueOf(0)), investment.getPrincipalForYear(0));
			assertEquals(Money.won(BigDecimal.valueOf(12_000_000)), investment.getPrincipalForYear(1));
			assertEquals(Money.won(BigDecimal.valueOf(24_330_017)), investment.getPrincipalForYear(2));
			assertEquals(Money.won(BigDecimal.valueOf(26_290_862)), investment.getPrincipalForYear(3));
			assertEquals(Money.won(BigDecimal.valueOf(26_290_862)), investment.getPrincipalForYear(4));
		}

		@Test
		void getInterestForYear() {
			investment = ((FixedSaving)investment).toBuilder()
				.investPeriod(new YearlyInvestPeriod(3))
				.build();

			assertEquals(Money.won(BigDecimal.valueOf(0)), investment.getInterestForYear(-1));
			assertEquals(Money.won(BigDecimal.valueOf(0)), investment.getInterestForYear(0));
			assertEquals(Money.won(BigDecimal.valueOf(330_017)), investment.getInterestForYear(1));
			assertEquals(Money.won(BigDecimal.valueOf(960_844)), investment.getInterestForYear(2));
			assertEquals(Money.won(BigDecimal.valueOf(1_623_946)), investment.getInterestForYear(3));
			assertEquals(Money.won(BigDecimal.valueOf(1_623_946)), investment.getInterestForYear(4));
		}

		@Test
		void getInterestForYear_whenPeriodIs25Months() {
			Taxable nonTax = taxableFactory.createNonTax();
			investment = ((FixedSaving)investment).toBuilder()
				.investPeriod(new MonthlyInvestPeriod(25))
				.taxable(nonTax)
				.build();

			assertEquals(Money.won(BigDecimal.valueOf(0)), investment.getInterestForYear(-1));
			assertEquals(Money.won(BigDecimal.valueOf(0)), investment.getInterestForYear(0));
			assertEquals(Money.won(BigDecimal.valueOf(330_017)), investment.getInterestForYear(1));
			assertEquals(Money.won(BigDecimal.valueOf(960_844)), investment.getInterestForYear(2));
			assertEquals(Money.won(BigDecimal.valueOf(109_545)), investment.getInterestForYear(3));
			assertEquals(Money.won(BigDecimal.valueOf(109_545)), investment.getInterestForYear(4));
		}

		@Test
		void getProfitForYear() {
			investment = ((FixedSaving)investment).toBuilder()
				.investPeriod(new YearlyInvestPeriod(3))
				.build();

			assertEquals(Money.won(BigDecimal.valueOf(0)), investment.getProfitForYear(-1));
			assertEquals(Money.won(BigDecimal.valueOf(0)), investment.getProfitForYear(0));
			assertEquals(Money.won(BigDecimal.valueOf(12_330_017)), investment.getProfitForYear(1));
			assertEquals(Money.won(BigDecimal.valueOf(25_290_862)), investment.getProfitForYear(2));
			assertEquals(Money.won(BigDecimal.valueOf(38_914_808)), investment.getProfitForYear(3));
			assertEquals(Money.won(BigDecimal.valueOf(38_914_808)), investment.getProfitForYear(4));
		}

		@Test
		void getProfitForYear_whenPeriodIs25Months() {
			Taxable nonTax = taxableFactory.createNonTax();
			investment = ((FixedSaving)investment).toBuilder()
				.investPeriod(new MonthlyInvestPeriod(25))
				.taxable(nonTax)
				.build();

			assertEquals(Money.won(BigDecimal.valueOf(0)), investment.getProfitForYear(-1));
			assertEquals(Money.won(BigDecimal.valueOf(0)), investment.getProfitForYear(0));
			assertEquals(Money.won(BigDecimal.valueOf(12_330_017)), investment.getProfitForYear(1));
			assertEquals(Money.won(BigDecimal.valueOf(25_290_862)), investment.getProfitForYear(2));
			assertEquals(Money.won(BigDecimal.valueOf(26_400_407)), investment.getProfitForYear(3));
			assertEquals(Money.won(BigDecimal.valueOf(26_400_407)), investment.getProfitForYear(4));
		}
	}
}
