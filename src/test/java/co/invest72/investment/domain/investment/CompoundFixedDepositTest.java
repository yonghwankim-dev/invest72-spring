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
import co.invest72.money.domain.Money;

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
		Money principal = investment.getPrincipal(month);
		Money interest = investment.getInterest(month);
		Money totalProfit = investment.getProfit(month);

		assertEquals(Money.won(BigDecimal.valueOf(expectedPrincipal)), principal);
		assertEquals(Money.won(BigDecimal.valueOf(expectedInterest)), interest);
		assertEquals(Money.won(BigDecimal.valueOf(expectedTotalProfit)), totalProfit);
	}

	@Test
	void getPrincipal() {
		Money principal = investment.getPrincipal();

		assertEquals(Money.won(BigDecimal.valueOf(1_046_800)), principal);
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

		assertEquals(Money.won(BigDecimal.valueOf(1_046_800)), principal);
	}

	@Test
	void getInterest() {
		Money interest = investment.getInterest();

		assertEquals(Money.won(BigDecimal.valueOf(4_362)), interest);
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

		assertEquals(Money.won(BigDecimal.valueOf(4_362)), interest);
	}

	@Test
	void getProfit() {
		Money totalProfit = investment.getProfit();

		assertEquals(Money.won(BigDecimal.valueOf(1_051_162)), totalProfit);
	}

	@Test
	void getTotalInvestment() {
		Money totalInvestment = investment.getTotalInvestment();

		assertEquals(Money.won(BigDecimal.valueOf(1_000_000)), totalInvestment);
	}

	@Test
	void getTotalInterest() {
		Money totalInterest = investment.getTotalInterest();

		assertEquals(Money.won(BigDecimal.valueOf(51_162)), totalInterest);
	}

	@Test
	void getTotalTax() {
		Money totalTax = investment.getTotalTax();

		assertEquals(Money.won(BigDecimal.valueOf(7_879)), totalTax);
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

		assertEquals(Money.won(BigDecimal.valueOf(1_000_000)), investment.getPrincipalForYear(-1));
		assertEquals(Money.won(BigDecimal.valueOf(1_000_000)), investment.getPrincipalForYear(0));
		assertEquals(Money.won(BigDecimal.valueOf(1_000_000)), investment.getPrincipalForYear(1));
		assertEquals(Money.won(BigDecimal.valueOf(1_051_162)), investment.getPrincipalForYear(2));
		assertEquals(Money.won(BigDecimal.valueOf(1_104_941)), investment.getPrincipalForYear(3));
		assertEquals(Money.won(BigDecimal.valueOf(1_104_941)), investment.getPrincipalForYear(4));
	}

	@Test
	void getInterestForYear_whenPeriodIs3Year() {
		investment = ((CompoundFixedDeposit)investment).toBuilder()
			.investPeriod(new YearlyInvestPeriod(3))
			.build();

		assertEquals(Money.won(BigDecimal.valueOf(0)), investment.getInterestForYear(-1));
		assertEquals(Money.won(BigDecimal.valueOf(0)), investment.getInterestForYear(0));
		assertEquals(Money.won(BigDecimal.valueOf(51_162)), investment.getInterestForYear(1));
		assertEquals(Money.won(BigDecimal.valueOf(53_779)), investment.getInterestForYear(2));
		assertEquals(Money.won(BigDecimal.valueOf(56_531)), investment.getInterestForYear(3));
		assertEquals(Money.won(BigDecimal.valueOf(56_531)), investment.getInterestForYear(4));
	}

	@Test
	void getProfitForYear_whenPeriodIs3Year() {
		investment = ((CompoundFixedDeposit)investment).toBuilder()
			.investPeriod(new YearlyInvestPeriod(3))
			.build();

		assertEquals(Money.won(BigDecimal.valueOf(1_000_000)), investment.getProfitForYear(-1));
		assertEquals(Money.won(BigDecimal.valueOf(1_000_000)), investment.getProfitForYear(0));
		assertEquals(Money.won(BigDecimal.valueOf(1_051_162)), investment.getProfitForYear(1));
		assertEquals(Money.won(BigDecimal.valueOf(1_104_941)), investment.getProfitForYear(2));
		assertEquals(Money.won(BigDecimal.valueOf(1_161_472)), investment.getProfitForYear(3));
		assertEquals(Money.won(BigDecimal.valueOf(1_161_472)), investment.getProfitForYear(4));
	}
}
