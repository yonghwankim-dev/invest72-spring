package co.invest72.investment.domain.investment;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import co.invest72.investment.domain.InterestRate;
import co.invest72.investment.domain.InvestPeriod;
import co.invest72.investment.domain.Investment;
import co.invest72.investment.domain.LumpSumInvestmentAmount;
import co.invest72.investment.domain.PeriodRange;
import co.invest72.investment.domain.Taxable;
import co.invest72.investment.domain.TaxableFactory;
import co.invest72.investment.domain.amount.FixedDepositAmount;
import co.invest72.investment.domain.interest.AnnualInterestRate;
import co.invest72.investment.domain.period.MonthlyInvestPeriod;
import co.invest72.investment.domain.period.PeriodYearRange;
import co.invest72.investment.domain.period.YearlyInvestPeriod;
import co.invest72.investment.domain.tax.FixedTaxRate;
import co.invest72.investment.domain.tax.KoreanTaxableFactory;
import co.invest72.investment.domain.tax.TaxType;

class SimpleFixedDepositTest {

	private Investment investment;

	@BeforeEach
	void setUp() {
		LumpSumInvestmentAmount investmentAmount = new FixedDepositAmount(BigDecimal.valueOf(1_000_000), "KRW");
		PeriodRange periodRange = new PeriodYearRange(1);
		InvestPeriod investPeriod = new MonthlyInvestPeriod(periodRange.toMonths());
		InterestRate interestRate = new AnnualInterestRate(0.05);
		TaxableFactory taxableFactory = new KoreanTaxableFactory();
		Taxable taxable = taxableFactory.createStandardTax(new FixedTaxRate(0.154));
		investment = SimpleFixedDeposit.builder()
			.investmentAmount(investmentAmount)
			.investPeriod(investPeriod)
			.interestRate(interestRate)
			.taxable(taxable)
			.build();
	}

	@DisplayName("월별 투자 금액 계산 - 고정 예금, 단리, 일반 과세")
	@ParameterizedTest
	@CsvFileSource(files = "src/test/resources/simple_fixed_deposit_1y_5percent_standard_tax.csv", numLinesToSkip = 1)
	void shouldReturnInvestmentAmount(int month, int expectedPrincipal, int expectedInterest, int expectedProfit) {
		BigDecimal principal = investment.getPrincipal(month).getValue();
		BigDecimal interest = investment.getInterest(month).getValue();
		BigDecimal profit = investment.getProfit(month).getValue();

		assertEquals(BigDecimal.valueOf(expectedPrincipal), principal);
		assertEquals(BigDecimal.valueOf(expectedInterest), interest);
		assertEquals(BigDecimal.valueOf(expectedProfit), profit);
	}

	@Test
	void getPrincipal() {
		BigDecimal principal = investment.getPrincipal().getValue();

		assertEquals(BigDecimal.valueOf(1_045_833), principal);
	}

	@Test
	void getPrincipal_whenMonthsIsNegative_thenReturnPrincipal() {
		int month = -1;

		BigDecimal principal = investment.getPrincipal(month).getValue();

		assertEquals(BigDecimal.valueOf(1_000_000), principal);
	}

	@Test
	void getPrincipal_whenMonthsIsZero_thenReturnPrincipal() {
		int month = 0;

		BigDecimal principal = investment.getPrincipal(month).getValue();

		assertEquals(BigDecimal.valueOf(1_000_000), principal);
	}

	@Test
	void getPrincipal_whenMonthsGreaterThanFinalMonth_thenReturnFinalMonthPrincipal() {
		int month = 13;

		BigDecimal principal = investment.getPrincipal(month).getValue();

		assertEquals(BigDecimal.valueOf(1_045_833), principal);
	}

	@Test
	void getInterest() {
		BigDecimal interest = investment.getInterest().getValue();

		assertEquals(BigDecimal.valueOf(4_167), interest);
	}

	@Test
	void getInterest_whenMonthsIsZero_thenReturnZeroInterest() {
		int months = 0;

		BigDecimal interest = investment.getInterest(months).getValue();

		assertEquals(BigDecimal.valueOf(0), interest);
	}

	@Test
	void getInterest_whenMonthsIsNegative_thenReturnZeroInterest() {
		int months = -1;

		BigDecimal interest = investment.getInterest(months).getValue();

		assertEquals(BigDecimal.valueOf(0), interest);
	}

	@Test
	void getInterest_whenMonthGreaterThanFinalMonth_thenReturnFinalMonthInterest() {
		int month = 13;

		BigDecimal interest = investment.getInterest(month).getValue();

		assertEquals(BigDecimal.valueOf(4_167), interest);
	}

	@Test
	void getProfit() {
		BigDecimal profit = investment.getProfit().getValue();

		assertEquals(BigDecimal.valueOf(1_050_000), profit);
	}

	@Test
	void getFinalMonth() {
		int finalMonth = investment.getFinalMonth();

		assertEquals(12, finalMonth);
	}

	@Test
	void getProfit_whenMonthsIsNegative_thenReturnProfit() {
		int months = -1;

		BigDecimal profit = investment.getProfit(months).getValue();

		assertEquals(BigDecimal.valueOf(1_000_000), profit);
	}

	@Test
	void getProfit_whenMonthsIsZero_thenReturnProfit() {
		int months = 0;

		BigDecimal profit = investment.getProfit(months).getValue();

		assertEquals(BigDecimal.valueOf(1_000_000), profit);
	}

	@Test
	void getProfit_whenMonthsGreaterThanFinalMonth_thenReturnFinalMonthProfit() {
		int month = 13;

		BigDecimal profit = investment.getProfit(month).getValue();

		assertEquals(BigDecimal.valueOf(1_050_000), profit);
	}

	@Test
	void getTotalInvestment() {
		BigDecimal totalInvestment = investment.getTotalInvestment().getValue();

		assertEquals(BigDecimal.valueOf(1_000_000), totalInvestment);
	}

	@Test
	void getTotalInterest() {
		BigDecimal totalInterest = investment.getTotalInterest().getValue();

		assertEquals(BigDecimal.valueOf(50_000), totalInterest);
	}

	@Test
	void getTotalTax() {
		BigDecimal totalTax = investment.getTotalTax().getValue();

		assertEquals(BigDecimal.valueOf(7_700), totalTax);
	}

	@Test
	void getTotalProfit() {
		BigDecimal totalProfit = investment.getTotalProfitMoney().getValue();

		assertEquals(BigDecimal.valueOf(1_042_300), totalProfit);
	}

	@Test
	void getTaxType() {
		String taxType = investment.getTaxType();

		assertEquals(TaxType.STANDARD.getDescription(), taxType);
	}

	@Test
	void getPrincipalForYear() {
		assertEquals(BigDecimal.valueOf(1_000_000), investment.getPrincipalForYear(1));
	}

	@Test
	void getPrincipalForYear_whenPeriodIsFiveYears() {
		investment = ((SimpleFixedDeposit)investment).toBuilder()
			.investPeriod(new YearlyInvestPeriod(5))
			.taxable(new KoreanTaxableFactory().createNonTax())
			.build();

		assertEquals(BigDecimal.valueOf(1_000_000), investment.getPrincipalForYear(-1));
		assertEquals(BigDecimal.valueOf(1_000_000), investment.getPrincipalForYear(0));
		assertEquals(BigDecimal.valueOf(1_000_000), investment.getPrincipalForYear(1));
		assertEquals(BigDecimal.valueOf(1_050_000), investment.getPrincipalForYear(2));
		assertEquals(BigDecimal.valueOf(1_100_000), investment.getPrincipalForYear(3));
		assertEquals(BigDecimal.valueOf(1_150_000), investment.getPrincipalForYear(4));
		assertEquals(BigDecimal.valueOf(1_200_000), investment.getPrincipalForYear(5));
	}

	@Test
	void getPrincipalForYear_whenPeriodIsOneMonth() {
		investment = ((SimpleFixedDeposit)investment).toBuilder()
			.investPeriod(new MonthlyInvestPeriod(1))
			.taxable(new KoreanTaxableFactory().createNonTax())
			.build();

		assertEquals(BigDecimal.valueOf(1_000_000), investment.getPrincipalForYear(0));
		assertEquals(BigDecimal.valueOf(1_000_000), investment.getPrincipalForYear(1));
		assertEquals(BigDecimal.valueOf(1_000_000), investment.getPrincipalForYear(2));
		assertEquals(BigDecimal.valueOf(1_000_000), investment.getPrincipalForYear(3));
	}

	@Test
	void getInterestForYear() {
		investment = ((SimpleFixedDeposit)investment).toBuilder()
			.investPeriod(new YearlyInvestPeriod(5))
			.taxable(new KoreanTaxableFactory().createNonTax())
			.build();

		assertEquals(BigDecimal.valueOf(0), investment.getInterestForYear(-1));
		assertEquals(BigDecimal.valueOf(0), investment.getInterestForYear(0));
		assertEquals(BigDecimal.valueOf(50_000), investment.getInterestForYear(1));
		assertEquals(BigDecimal.valueOf(50_000), investment.getInterestForYear(2));
		assertEquals(BigDecimal.valueOf(50_000), investment.getInterestForYear(3));
		assertEquals(BigDecimal.valueOf(50_000), investment.getInterestForYear(4));
		assertEquals(BigDecimal.valueOf(50_000), investment.getInterestForYear(5));
	}

	@Test
	void getInterestForYear_whenPeriodIsMonth13() {
		investment = ((SimpleFixedDeposit)investment).toBuilder()
			.investPeriod(new MonthlyInvestPeriod(13))
			.taxable(new KoreanTaxableFactory().createNonTax())
			.build();

		assertEquals(BigDecimal.valueOf(50_000), investment.getInterestForYear(1));
		assertEquals(BigDecimal.valueOf(4_167), investment.getInterestForYear(2));
	}

	@Test
	void getInterestForYear_whenPeriodIsMonth25() {
		investment = ((SimpleFixedDeposit)investment).toBuilder()
			.investPeriod(new MonthlyInvestPeriod(25))
			.taxable(new KoreanTaxableFactory().createNonTax())
			.build();

		assertEquals(BigDecimal.valueOf(50_000), investment.getInterestForYear(1));
		assertEquals(BigDecimal.valueOf(50_000), investment.getInterestForYear(2));
		assertEquals(BigDecimal.valueOf(4_167), investment.getInterestForYear(3));
	}

	@Test
	void getInterestForYear_whenPeriodIsMonth24() {
		investment = ((SimpleFixedDeposit)investment).toBuilder()
			.investPeriod(new MonthlyInvestPeriod(24))
			.taxable(new KoreanTaxableFactory().createNonTax())
			.build();

		assertEquals(BigDecimal.valueOf(50_000), investment.getInterestForYear(1));
		assertEquals(BigDecimal.valueOf(50_000), investment.getInterestForYear(2));
	}

	@Test
	void getInterestForYear_whenPeriodIsMonth1() {
		investment = ((SimpleFixedDeposit)investment).toBuilder()
			.investPeriod(new MonthlyInvestPeriod(1))
			.taxable(new KoreanTaxableFactory().createNonTax())
			.build();

		assertEquals(BigDecimal.valueOf(4_167), investment.getInterestForYear(1));
	}

	@Test
	void getProfitForYear() {
		investment = ((SimpleFixedDeposit)investment).toBuilder()
			.investPeriod(new YearlyInvestPeriod(5))
			.taxable(new KoreanTaxableFactory().createNonTax())
			.build();

		assertEquals(BigDecimal.valueOf(1_000_000), investment.getProfitForYear(-1));
		assertEquals(BigDecimal.valueOf(1_000_000), investment.getProfitForYear(0));
		assertEquals(BigDecimal.valueOf(1_050_000), investment.getProfitForYear(1));
		assertEquals(BigDecimal.valueOf(1_100_000), investment.getProfitForYear(2));
		assertEquals(BigDecimal.valueOf(1_150_000), investment.getProfitForYear(3));
		assertEquals(BigDecimal.valueOf(1_200_000), investment.getProfitForYear(4));
		assertEquals(BigDecimal.valueOf(1_250_000), investment.getProfitForYear(5));
	}

	@Test
	void getProfitForYear_whenPeriodIsMonth25() {
		investment = ((SimpleFixedDeposit)investment).toBuilder()
			.investPeriod(new MonthlyInvestPeriod(25))
			.taxable(new KoreanTaxableFactory().createNonTax())
			.build();

		assertEquals(BigDecimal.valueOf(1_050_000), investment.getProfitForYear(1));
		assertEquals(BigDecimal.valueOf(1_100_000), investment.getProfitForYear(2));
		assertEquals(BigDecimal.valueOf(1_104_167), investment.getProfitForYear(3));
	}
}
