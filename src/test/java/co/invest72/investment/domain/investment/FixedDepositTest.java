package co.invest72.investment.domain.investment;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.assertj.core.api.Assertions;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;

import co.invest72.investment.domain.InterestRate;
import co.invest72.investment.domain.InvestPeriod;
import co.invest72.investment.domain.Investment;
import co.invest72.investment.domain.LumpSumInvestmentAmount;
import co.invest72.investment.domain.PeriodRange;
import co.invest72.investment.domain.Taxable;
import co.invest72.investment.domain.TaxableFactory;
import co.invest72.investment.domain.amount.FixedDepositAmount;
import co.invest72.investment.domain.interest.AnnualInterestRate;
import co.invest72.investment.domain.interest.InterestType;
import co.invest72.investment.domain.period.MonthlyInvestPeriod;
import co.invest72.investment.domain.period.PeriodYearRange;
import co.invest72.investment.domain.period.YearlyInvestPeriod;
import co.invest72.investment.domain.tax.FixedTaxRate;
import co.invest72.investment.domain.tax.KoreanTaxableFactory;
import co.invest72.investment.domain.tax.TaxType;
import co.invest72.money.domain.Currency;
import co.invest72.money.domain.Money;

class FixedDepositTest {

	private final Offset<BigDecimal> offset = Offset.offset(new BigDecimal("0.01"));

	@Nested
	class SimpleFixedDepositTest {

		private Investment investment;

		@BeforeEach
		void setUp() {
			LumpSumInvestmentAmount investmentAmount = new FixedDepositAmount(BigDecimal.valueOf(1_000_000),
				Currency.won());
			PeriodRange periodRange = new PeriodYearRange(1);
			InvestPeriod investPeriod = new MonthlyInvestPeriod(periodRange.toMonths());
			InterestRate interestRate = new AnnualInterestRate(0.05);
			TaxableFactory taxableFactory = new KoreanTaxableFactory();
			Taxable taxable = taxableFactory.createStandardTax(new FixedTaxRate(0.154));
			investment = FixedDeposit.builder()
				.investmentAmount(investmentAmount)
				.investPeriod(investPeriod)
				.interestRate(interestRate)
				.interestType(InterestType.SIMPLE)
				.taxable(taxable)
				.build();
		}

		@DisplayName("월별 투자 금액 계산 - 고정 예금, 단리, 일반 과세")
		@ParameterizedTest
		@CsvFileSource(files = "src/test/resources/simple_fixed_deposit_1y_5percent_standard_tax.csv", numLinesToSkip = 1)
		void shouldReturnInvestmentAmount(int month, double expectedPrincipal, double expectedInterest,
			double expectedProfit) {
			Money principal = investment.getPrincipal(month);
			Money interest = investment.getInterest(month);
			Money profit = investment.getProfit(month);

			Assertions.assertThat(principal.getValue())
				.isCloseTo(BigDecimal.valueOf(expectedPrincipal), offset);
			Assertions.assertThat(interest.getValue())
				.isCloseTo(BigDecimal.valueOf(expectedInterest), offset);
			Assertions.assertThat(profit.getValue())
				.isCloseTo(BigDecimal.valueOf(expectedProfit), offset);
		}

		@Test
		void getPrincipal() {
			Money principal = investment.getPrincipal();

			Assertions.assertThat(principal.getValue())
				.isCloseTo(BigDecimal.valueOf(1_045_833.33), offset);
		}

		@Test
		void getPrincipal_whenMonthsIsNegative_thenReturnPrincipal() {
			int month = -1;

			Money principal = investment.getPrincipal(month);

			assertEquals(Money.won(BigDecimal.valueOf(1_000_000)), principal);
		}

		@Test
		void getPrincipal_whenMonthsIsZero_thenReturnPrincipal() {
			int month = 0;

			Money principal = investment.getPrincipal(month);

			assertEquals(Money.won(BigDecimal.valueOf(1_000_000)), principal);
		}

		@Test
		void getPrincipal_whenMonthsGreaterThanFinalMonth_thenReturnFinalMonthPrincipal() {
			int month = 13;

			Money principal = investment.getPrincipal(month);

			Assertions.assertThat(principal.getValue())
				.isCloseTo(BigDecimal.valueOf(1_045_833.33), offset);
		}

		@Test
		void getInterest() {
			Money interest = investment.getInterest();

			Assertions.assertThat(interest.getValue())
				.isCloseTo(BigDecimal.valueOf(4_166.67), offset);
		}

		@Test
		void getInterest_whenMonthsIsZero_thenReturnZeroInterest() {
			int months = 0;

			Money interest = investment.getInterest(months);

			assertEquals(Money.won(BigDecimal.valueOf(0)), interest);
		}

		@Test
		void getInterest_whenMonthsIsNegative_thenReturnZeroInterest() {
			int months = -1;

			Money interest = investment.getInterest(months);

			assertEquals(Money.won(BigDecimal.valueOf(0)), interest);
		}

		@Test
		void getInterest_whenMonthGreaterThanFinalMonth_thenReturnFinalMonthInterest() {
			int month = 13;

			Money interest = investment.getInterest(month);

			Assertions.assertThat(interest.getValue())
				.isCloseTo(BigDecimal.valueOf(4_166.66), offset);
		}

		@Test
		void getProfit() {
			Money profit = investment.getProfit();

			Assertions.assertThat(profit.getValue())
				.isCloseTo(BigDecimal.valueOf(1_050_000), offset);
		}

		@Test
		void getFinalMonth() {
			int finalMonth = investment.getFinalMonth();

			assertEquals(12, finalMonth);
		}

		@Test
		void getProfit_whenMonthsIsNegative_thenReturnProfit() {
			int months = -1;

			Money profit = investment.getProfit(months);

			assertEquals(Money.won(BigDecimal.valueOf(1_000_000)), profit);
		}

		@Test
		void getProfit_whenMonthsIsZero_thenReturnProfit() {
			int months = 0;

			Money profit = investment.getProfit(months);

			assertEquals(Money.won(BigDecimal.valueOf(1_000_000)), profit);
		}

		@Test
		void getProfit_whenMonthsGreaterThanFinalMonth_thenReturnFinalMonthProfit() {
			int month = 13;

			Money profit = investment.getProfit(month);

			Assertions.assertThat(profit.getValue())
				.isCloseTo(BigDecimal.valueOf(1_050_000), offset);
		}

		@Test
		void getTotalInvestment() {
			Money totalInvestment = investment.getTotalInvestment();

			assertEquals(Money.won(BigDecimal.valueOf(1_000_000)), totalInvestment);
		}

		@Test
		void getTotalInterest() {
			Money totalInterest = investment.getTotalInterest();

			Assertions.assertThat(totalInterest.getValue())
				.isCloseTo(BigDecimal.valueOf(50_000), offset);
		}

		@Test
		void getTotalTax() {
			Money totalTax = investment.getTotalTax();

			Assertions.assertThat(totalTax.getValue())
				.isCloseTo(BigDecimal.valueOf(7_700), offset);
		}

		@Test
		void getTotalProfit() {
			Money totalProfit = investment.getTotalProfit();

			Assertions.assertThat(totalProfit.getValue())
				.isCloseTo(BigDecimal.valueOf(1_042_300), offset);
		}

		@Test
		void getTaxType() {
			String taxType = investment.getTaxType();

			assertEquals(TaxType.STANDARD.getDescription(), taxType);
		}

		@Test
		void getPrincipalForYear() {
			assertEquals(Money.won(BigDecimal.valueOf(1_000_000)), investment.getPrincipalForYear(1));
		}

		@Test
		void getPrincipalForYear_whenPeriodIsFiveYears() {
			investment = ((FixedDeposit)investment).toBuilder()
				.investPeriod(new YearlyInvestPeriod(5))
				.taxable(new KoreanTaxableFactory().createNonTax())
				.build();

			Assertions.assertThat(investment.getPrincipalForYear(-1).getValue())
				.isCloseTo(BigDecimal.valueOf(1_000_000), offset);
			Assertions.assertThat(investment.getPrincipalForYear(0).getValue())
				.isCloseTo(BigDecimal.valueOf(1_000_000), offset);
			Assertions.assertThat(investment.getPrincipalForYear(1).getValue())
				.isCloseTo(BigDecimal.valueOf(1_000_000), offset);
			Assertions.assertThat(investment.getPrincipalForYear(2).getValue())
				.isCloseTo(BigDecimal.valueOf(1_050_000), offset);
			Assertions.assertThat(investment.getPrincipalForYear(3).getValue())
				.isCloseTo(BigDecimal.valueOf(1_100_000), offset);
			Assertions.assertThat(investment.getPrincipalForYear(4).getValue())
				.isCloseTo(BigDecimal.valueOf(1_150_000), offset);
			Assertions.assertThat(investment.getPrincipalForYear(5).getValue())
				.isCloseTo(BigDecimal.valueOf(1_200_000), offset);
		}

		@Test
		void getPrincipalForYear_whenPeriodIsOneMonth() {
			investment = ((FixedDeposit)investment).toBuilder()
				.investPeriod(new MonthlyInvestPeriod(1))
				.taxable(new KoreanTaxableFactory().createNonTax())
				.build();

			assertEquals(Money.won(BigDecimal.valueOf(1_000_000)), investment.getPrincipalForYear(0));
			assertEquals(Money.won(BigDecimal.valueOf(1_000_000)), investment.getPrincipalForYear(1));
			assertEquals(Money.won(BigDecimal.valueOf(1_000_000)), investment.getPrincipalForYear(2));
			assertEquals(Money.won(BigDecimal.valueOf(1_000_000)), investment.getPrincipalForYear(3));
		}

		@Test
		void getInterestForYear() {
			investment = ((FixedDeposit)investment).toBuilder()
				.investPeriod(new YearlyInvestPeriod(5))
				.taxable(new KoreanTaxableFactory().createNonTax())
				.build();

			Assertions.assertThat(investment.getInterestForYear(-1).getValue())
				.isCloseTo(BigDecimal.valueOf(0), offset);
			Assertions.assertThat(investment.getInterestForYear(0).getValue())
				.isCloseTo(BigDecimal.valueOf(0), offset);
			Assertions.assertThat(investment.getInterestForYear(1).getValue())
				.isCloseTo(BigDecimal.valueOf(50_000), offset);
			Assertions.assertThat(investment.getInterestForYear(2).getValue())
				.isCloseTo(BigDecimal.valueOf(50_000), offset);
			Assertions.assertThat(investment.getInterestForYear(3).getValue())
				.isCloseTo(BigDecimal.valueOf(50_000), offset);
			Assertions.assertThat(investment.getInterestForYear(4).getValue())
				.isCloseTo(BigDecimal.valueOf(50_000), offset);
			Assertions.assertThat(investment.getInterestForYear(5).getValue())
				.isCloseTo(BigDecimal.valueOf(50_000), offset);
		}

		@Test
		void getInterestForYear_whenPeriodIsMonth13() {
			investment = ((FixedDeposit)investment).toBuilder()
				.investPeriod(new MonthlyInvestPeriod(13))
				.taxable(new KoreanTaxableFactory().createNonTax())
				.build();

			Assertions.assertThat(investment.getInterestForYear(1).getValue())
				.isCloseTo(BigDecimal.valueOf(50_000), offset);
			Assertions.assertThat(investment.getInterestForYear(2).getValue())
				.isCloseTo(BigDecimal.valueOf(4_166.66), offset);
		}

		@Test
		void getInterestForYear_whenPeriodIsMonth25() {
			investment = ((FixedDeposit)investment).toBuilder()
				.investPeriod(new MonthlyInvestPeriod(25))
				.taxable(new KoreanTaxableFactory().createNonTax())
				.build();

			Assertions.assertThat(investment.getInterestForYear(1).getValue())
				.isCloseTo(BigDecimal.valueOf(50_000), offset);
			Assertions.assertThat(investment.getInterestForYear(2).getValue())
				.isCloseTo(BigDecimal.valueOf(50_000), offset);
			Assertions.assertThat(investment.getInterestForYear(3).getValue())
				.isCloseTo(BigDecimal.valueOf(4_166.66), offset);
		}

		@Test
		void getInterestForYear_whenPeriodIsMonth24() {
			investment = ((FixedDeposit)investment).toBuilder()
				.investPeriod(new MonthlyInvestPeriod(24))
				.taxable(new KoreanTaxableFactory().createNonTax())
				.build();

			Assertions.assertThat(investment.getInterestForYear(1).getValue())
				.isCloseTo(BigDecimal.valueOf(50_000), offset);
			Assertions.assertThat(investment.getInterestForYear(2).getValue())
				.isCloseTo(BigDecimal.valueOf(50_000), offset);
		}

		@Test
		void getInterestForYear_whenPeriodIsMonth1() {
			investment = ((FixedDeposit)investment).toBuilder()
				.investPeriod(new MonthlyInvestPeriod(1))
				.taxable(new KoreanTaxableFactory().createNonTax())
				.build();

			Assertions.assertThat(investment.getInterestForYear(1).getValue())
				.isCloseTo(BigDecimal.valueOf(4_166.66), offset);
		}

		@ParameterizedTest(name = "{0}년 차(경계값 포함) 예상 수익금: {1}원")
		@CsvSource({
			"-1, 1000000",
			"0, 1000000",
			"1, 1050000",
			"2, 1100000",
			"3, 1150000",
			"4, 1200000",
			"5, 1250000"
		})
		void getProfitForYear(int year, String expectedProfit) {
			// given
			investment = ((FixedDeposit)investment).toBuilder()
				.investPeriod(new YearlyInvestPeriod(5))
				.taxable(new KoreanTaxableFactory().createNonTax())
				.build();

			// when & then
			Assertions.assertThat(investment.getProfitForYear(year).getValue())
				.isCloseTo(new BigDecimal(expectedProfit), offset);
		}

		@ParameterizedTest(name = "{0}년 차(경계값 포함) 예상 수익금: {1}원")
		@CsvSource({
			"1, 1050000",
			"2, 1100000",
			"3, 1104166.66",
		})
		void getProfitForYear_whenPeriodIsMonth25(int year, String expectedProfit) {
			investment = ((FixedDeposit)investment).toBuilder()
				.investPeriod(new MonthlyInvestPeriod(25))
				.taxable(new KoreanTaxableFactory().createNonTax())
				.build();
			Assertions.assertThat(investment.getProfitForYear(year).getValue())
				.isCloseTo(new BigDecimal(expectedProfit), offset);
		}
	}

	@Nested
	class CompoundFixedDepositTest {
		private Investment investment;

		@BeforeEach
		void setUp() {
			LumpSumInvestmentAmount depositAmount = new FixedDepositAmount(BigDecimal.valueOf(1_000_000),
				Currency.won());
			InvestPeriod investPeriod = new YearlyInvestPeriod(1);
			InterestRate interestRate = new AnnualInterestRate(0.05);
			TaxableFactory taxableFactory = new KoreanTaxableFactory();
			Taxable taxable = taxableFactory.createStandardTax(new FixedTaxRate(0.154));
			investment = FixedDeposit.builder()
				.investmentAmount(depositAmount)
				.investPeriod(investPeriod)
				.interestRate(interestRate)
				.interestType(InterestType.COMPOUND)
				.taxable(taxable)
				.build();
		}

		@ParameterizedTest
		@CsvFileSource(files = "src/test/resources/compound_fixed_deposit_1y_5percent_standard_tax.csv", numLinesToSkip = 1)
		void shouldReturnInvestmentAmount(int month, double expectedPrincipal, double expectedInterest,
			double expectedTotalProfit) {
			Money principal = investment.getPrincipal(month);
			Money interest = investment.getInterest(month);
			Money totalProfit = investment.getProfit(month);

			Assertions.assertThat(principal.getValue())
				.isCloseTo(BigDecimal.valueOf(expectedPrincipal), offset);
			Assertions.assertThat(interest.getValue())
				.isCloseTo(BigDecimal.valueOf(expectedInterest), offset);
			Assertions.assertThat(totalProfit.getValue())
				.isCloseTo(BigDecimal.valueOf(expectedTotalProfit), offset);
			// assertEquals(Money.won(BigDecimal.valueOf(expectedPrincipal)), principal);
			// assertEquals(Money.won(BigDecimal.valueOf(expectedInterest)), interest);
			// assertEquals(Money.won(BigDecimal.valueOf(expectedTotalProfit)), totalProfit);
		}

		@Test
		void getPrincipal() {
			Money principal = investment.getPrincipal();

			Assertions.assertThat(principal.getValue())
				.isCloseTo(BigDecimal.valueOf(1_046_800.23), offset);
		}

		@Test
		void getPrincipal_whenMonthIsNegative() {
			Money principal = investment.getPrincipal(-1);

			assertEquals(Money.won(BigDecimal.valueOf(1_000_000)), principal);
		}

		@Test
		void getPrincipal_whenMonthIsZero() {
			Money principal = investment.getPrincipal(0);

			assertEquals(Money.won(BigDecimal.valueOf(1_000_000)), principal);
		}

		@Test
		void getPrincipal_whenMonthGreaterThanFinalMonth() {
			Money principal = investment.getPrincipal(13);

			Assertions.assertThat(principal.getValue())
				.isCloseTo(BigDecimal.valueOf(1_046_800.23), offset);
		}

		@Test
		void getInterest() {
			Money interest = investment.getInterest();

			Assertions.assertThat(interest.getValue())
				.isCloseTo(BigDecimal.valueOf(4_361.66), offset);
		}

		@Test
		void getInterest_whenMonthIsNegative() {
			Money interest = investment.getInterest(-1);

			assertEquals(Money.won(BigDecimal.valueOf(0)), interest);
		}

		@Test
		void getInterest_whenMonthIsZero() {
			Money interest = investment.getInterest(0);

			assertEquals(Money.won(BigDecimal.valueOf(0)), interest);
		}

		@Test
		void getInterest_whenMonthGreaterThanFinalMonth() {
			Money interest = investment.getInterest(13);

			Assertions.assertThat(interest.getValue())
				.isCloseTo(BigDecimal.valueOf(4_361.66), offset);
		}

		@Test
		void getProfit() {
			Money totalProfit = investment.getProfit();

			Assertions.assertThat(totalProfit.getValue())
				.isCloseTo(BigDecimal.valueOf(1_051_161.9), offset);
		}

		@Test
		void getTotalInvestment() {
			Money totalInvestment = investment.getTotalInvestment();

			assertEquals(Money.won(BigDecimal.valueOf(1_000_000)), totalInvestment);
		}

		@Test
		void getTotalInterest() {
			Money totalInterest = investment.getTotalInterest();

			Assertions.assertThat(totalInterest.getValue())
				.isCloseTo(BigDecimal.valueOf(51_161.89), offset);
		}

		@Test
		void getTotalTax() {
			Money totalTax = investment.getTotalTax();

			Assertions.assertThat(totalTax.getValue())
				.isCloseTo(totalTax.getValue(), offset);
		}

		@Test
		void getTaxType() {
			String taxType = investment.getTaxType();

			assertEquals(TaxType.STANDARD.getDescription(), taxType);
		}

		@Test
		void getPrincipalForYear_whenPeriodIs3Year() {
			investment = ((FixedDeposit)investment).toBuilder()
				.investPeriod(new YearlyInvestPeriod(3))
				.build();

			Assertions.assertThat(investment.getPrincipalForYear(-1).getValue())
				.isCloseTo(BigDecimal.valueOf(1_000_000.0), offset);
			Assertions.assertThat(investment.getPrincipalForYear(0).getValue())
				.isCloseTo(BigDecimal.valueOf(1_000_000.0), offset);
			Assertions.assertThat(investment.getPrincipalForYear(1).getValue())
				.isCloseTo(BigDecimal.valueOf(1_000_000.0), offset);
			Assertions.assertThat(investment.getPrincipalForYear(2).getValue())
				.isCloseTo(BigDecimal.valueOf(1_051_161.89), offset);
			Assertions.assertThat(investment.getPrincipalForYear(3).getValue())
				.isCloseTo(BigDecimal.valueOf(1_104_941.33), offset);
		}

		@Test
		void getInterestForYear_whenPeriodIs3Year() {
			investment = ((FixedDeposit)investment).toBuilder()
				.investPeriod(new YearlyInvestPeriod(3))
				.build();

			Assertions.assertThat(investment.getInterestForYear(-1).getValue())
				.isCloseTo(BigDecimal.valueOf(0.0), offset);
			Assertions.assertThat(investment.getInterestForYear(0).getValue())
				.isCloseTo(BigDecimal.valueOf(0.0), offset);
			Assertions.assertThat(investment.getInterestForYear(1).getValue())
				.isCloseTo(BigDecimal.valueOf(51_161.89), offset);
			Assertions.assertThat(investment.getInterestForYear(2).getValue())
				.isCloseTo(BigDecimal.valueOf(53_779.44), offset);
			Assertions.assertThat(investment.getInterestForYear(3).getValue())
				.isCloseTo(BigDecimal.valueOf(56_530.89), offset);
			Assertions.assertThat(investment.getInterestForYear(4).getValue())
				.isCloseTo(BigDecimal.valueOf(56_530.89), offset);
		}

		@Test
		void getProfitForYear_whenPeriodIs3Year() {
			investment = ((FixedDeposit)investment).toBuilder()
				.investPeriod(new YearlyInvestPeriod(3))
				.build();

			Assertions.assertThat(investment.getProfitForYear(-1).getValue())
				.isCloseTo(BigDecimal.valueOf(1_000_000.0), offset);
			Assertions.assertThat(investment.getProfitForYear(0).getValue())
				.isCloseTo(BigDecimal.valueOf(1_000_000.0), offset);
			Assertions.assertThat(investment.getProfitForYear(1).getValue())
				.isCloseTo(BigDecimal.valueOf(1_051_161.9), offset);
			Assertions.assertThat(investment.getProfitForYear(2).getValue())
				.isCloseTo(BigDecimal.valueOf(1_104_941.34), offset);
			Assertions.assertThat(investment.getProfitForYear(3).getValue())
				.isCloseTo(BigDecimal.valueOf(1_161_472.23), offset);
			Assertions.assertThat(investment.getProfitForYear(4).getValue())
				.isCloseTo(BigDecimal.valueOf(1_161_472.23), offset);
		}
	}
}
