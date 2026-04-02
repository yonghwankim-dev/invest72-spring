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
import co.invest72.money.domain.Money;

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
		Money principal = investment.getPrincipal(month);
		Money interest = investment.getInterest(month);
		Money profit = investment.getProfit(month);

		assertEquals(Money.won(BigDecimal.valueOf(expectedPrincipal)), principal);
		assertEquals(Money.won(BigDecimal.valueOf(expectedInterest)), interest);
		assertEquals(Money.won(BigDecimal.valueOf(expectedProfit)), profit);
	}

	@Test
	void getPrincipal() {
		Money principal = investment.getPrincipal();

		assertEquals(Money.won(BigDecimal.valueOf(1_045_833)), principal);
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

		assertEquals(Money.won(BigDecimal.valueOf(1_045_833)), principal);
	}

	@Test
	void getInterest() {
		Money interest = investment.getInterest();

		assertEquals(Money.won(BigDecimal.valueOf(4_167)), interest);
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

		assertEquals(Money.won(BigDecimal.valueOf(4_167)), interest);
	}

	@Test
	void getProfit() {
		Money profit = investment.getProfit();

		assertEquals(Money.won(BigDecimal.valueOf(1_050_000)), profit);
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

		assertEquals(Money.won(BigDecimal.valueOf(1_050_000)), profit);
	}

	@Test
	void getTotalInvestment() {
		Money totalInvestment = investment.getTotalInvestment();

		assertEquals(Money.won(BigDecimal.valueOf(1_000_000)), totalInvestment);
	}

	@Test
	void getTotalInterest() {
		Money totalInterest = investment.getTotalInterest();

		assertEquals(Money.won(BigDecimal.valueOf(50_000)), totalInterest);
	}

	@Test
	void getTotalTax() {
		Money totalTax = investment.getTotalTax();

		assertEquals(Money.won(BigDecimal.valueOf(7_700)), totalTax);
	}

	@Test
	void getTotalProfit() {
		Money totalProfit = investment.getTotalProfit();

		assertEquals(Money.won(BigDecimal.valueOf(1_042_300)), totalProfit);
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
		investment = ((SimpleFixedDeposit)investment).toBuilder()
			.investPeriod(new YearlyInvestPeriod(5))
			.taxable(new KoreanTaxableFactory().createNonTax())
			.build();

		assertEquals(Money.won(BigDecimal.valueOf(1_000_000)), investment.getPrincipalForYear(-1));
		assertEquals(Money.won(BigDecimal.valueOf(1_000_000)), investment.getPrincipalForYear(0));
		assertEquals(Money.won(BigDecimal.valueOf(1_000_000)), investment.getPrincipalForYear(1));
		assertEquals(Money.won(BigDecimal.valueOf(1_050_000)), investment.getPrincipalForYear(2));
		assertEquals(Money.won(BigDecimal.valueOf(1_100_000)), investment.getPrincipalForYear(3));
		assertEquals(Money.won(BigDecimal.valueOf(1_150_000)), investment.getPrincipalForYear(4));
		assertEquals(Money.won(BigDecimal.valueOf(1_200_000)), investment.getPrincipalForYear(5));
	}

	@Test
	void getPrincipalForYear_whenPeriodIsOneMonth() {
		investment = ((SimpleFixedDeposit)investment).toBuilder()
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
		investment = ((SimpleFixedDeposit)investment).toBuilder()
			.investPeriod(new YearlyInvestPeriod(5))
			.taxable(new KoreanTaxableFactory().createNonTax())
			.build();

		assertEquals(Money.won(BigDecimal.valueOf(0)), investment.getInterestForYear(-1));
		assertEquals(Money.won(BigDecimal.valueOf(0)), investment.getInterestForYear(0));
		assertEquals(Money.won(BigDecimal.valueOf(50_000)), investment.getInterestForYear(1));
		assertEquals(Money.won(BigDecimal.valueOf(50_000)), investment.getInterestForYear(2));
		assertEquals(Money.won(BigDecimal.valueOf(50_000)), investment.getInterestForYear(3));
		assertEquals(Money.won(BigDecimal.valueOf(50_000)), investment.getInterestForYear(4));
		assertEquals(Money.won(BigDecimal.valueOf(50_000)), investment.getInterestForYear(5));
	}

	@Test
	void getInterestForYear_whenPeriodIsMonth13() {
		investment = ((SimpleFixedDeposit)investment).toBuilder()
			.investPeriod(new MonthlyInvestPeriod(13))
			.taxable(new KoreanTaxableFactory().createNonTax())
			.build();

		assertEquals(Money.won(BigDecimal.valueOf(50_000)), investment.getInterestForYear(1));
		assertEquals(Money.won(BigDecimal.valueOf(4_167)), investment.getInterestForYear(2));
	}

	@Test
	void getInterestForYear_whenPeriodIsMonth25() {
		investment = ((SimpleFixedDeposit)investment).toBuilder()
			.investPeriod(new MonthlyInvestPeriod(25))
			.taxable(new KoreanTaxableFactory().createNonTax())
			.build();

		assertEquals(Money.won(BigDecimal.valueOf(50_000)), investment.getInterestForYear(1));
		assertEquals(Money.won(BigDecimal.valueOf(50_000)), investment.getInterestForYear(2));
		assertEquals(Money.won(BigDecimal.valueOf(4_167)), investment.getInterestForYear(3));
	}

	@Test
	void getInterestForYear_whenPeriodIsMonth24() {
		investment = ((SimpleFixedDeposit)investment).toBuilder()
			.investPeriod(new MonthlyInvestPeriod(24))
			.taxable(new KoreanTaxableFactory().createNonTax())
			.build();

		assertEquals(Money.won(BigDecimal.valueOf(50_000)), investment.getInterestForYear(1));
		assertEquals(Money.won(BigDecimal.valueOf(50_000)), investment.getInterestForYear(2));
	}

	@Test
	void getInterestForYear_whenPeriodIsMonth1() {
		investment = ((SimpleFixedDeposit)investment).toBuilder()
			.investPeriod(new MonthlyInvestPeriod(1))
			.taxable(new KoreanTaxableFactory().createNonTax())
			.build();

		assertEquals(Money.won(BigDecimal.valueOf(4_167)), investment.getInterestForYear(1));
	}

	@Test
	void getProfitForYear() {
		investment = ((SimpleFixedDeposit)investment).toBuilder()
			.investPeriod(new YearlyInvestPeriod(5))
			.taxable(new KoreanTaxableFactory().createNonTax())
			.build();

		assertEquals(Money.won(BigDecimal.valueOf(1_000_000)), investment.getProfitForYear(-1));
		assertEquals(Money.won(BigDecimal.valueOf(1_000_000)), investment.getProfitForYear(0));
		assertEquals(Money.won(BigDecimal.valueOf(1_050_000)), investment.getProfitForYear(1));
		assertEquals(Money.won(BigDecimal.valueOf(1_100_000)), investment.getProfitForYear(2));
		assertEquals(Money.won(BigDecimal.valueOf(1_150_000)), investment.getProfitForYear(3));
		assertEquals(Money.won(BigDecimal.valueOf(1_200_000)), investment.getProfitForYear(4));
		assertEquals(Money.won(BigDecimal.valueOf(1_250_000)), investment.getProfitForYear(5));
	}

	@Test
	void getProfitForYear_whenPeriodIsMonth25() {
		investment = ((SimpleFixedDeposit)investment).toBuilder()
			.investPeriod(new MonthlyInvestPeriod(25))
			.taxable(new KoreanTaxableFactory().createNonTax())
			.build();

		assertEquals(Money.won(BigDecimal.valueOf(1_050_000)), investment.getProfitForYear(1));
		assertEquals(Money.won(BigDecimal.valueOf(1_100_000)), investment.getProfitForYear(2));
		assertEquals(Money.won(BigDecimal.valueOf(1_104_167)), investment.getProfitForYear(3));
	}
}
