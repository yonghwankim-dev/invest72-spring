package co.invest72.investment.domain.investment;

import java.math.BigDecimal;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import co.invest72.investment.domain.InterestRate;
import co.invest72.investment.domain.InvestPeriod;
import co.invest72.investment.domain.InvestmentAmount;
import co.invest72.investment.domain.amount.FixedDepositAmount;
import co.invest72.investment.domain.interest.AnnualInterestRate;
import co.invest72.investment.domain.interest.InterestType;
import co.invest72.investment.domain.period.MonthlyInvestPeriod;
import testutil.BigDecimalAssertion;

class InvestmentDetailFactoryTest {

	private InvestmentDetailFactory factory;

	@BeforeEach
	void setUp() {
		InvestmentAmount investmentAmount = new FixedDepositAmount(1_000_000);
		InterestRate interestRate = new AnnualInterestRate(0.05);
		InvestPeriod investPeriod = new MonthlyInvestPeriod(12);
		factory = new InvestmentDetailFactory(investmentAmount, interestRate, investPeriod);
	}

	@Test
	void canCreated() {
		Assertions.assertThat(factory).isNotNull();
	}

	@Test
	void createMonthlyDetails() {
		List<MonthlyInvestmentDetail> details = factory.createMonthlyDetails(InterestType.SIMPLE);

		Assertions.assertThat(details)
			.hasSize(13);
	}

	@Test
	void createYearlyDetails() {
		List<YearlyInvestmentDetail> details = factory.calculateYearlyDetails(InterestType.SIMPLE);

		List<YearlyInvestmentDetail> expected = List.of(
			new YearlyInvestmentDetail(0, BigDecimal.valueOf(1_000_000), BigDecimal.ZERO,
				BigDecimal.valueOf(1_000_000)),
			new YearlyInvestmentDetail(1, BigDecimal.valueOf(1_000_000), BigDecimal.valueOf(50_000),
				BigDecimal.valueOf(1_050_000))
		);
		Assertions.assertThat(details)
			.hasSize(2)
			.usingRecursiveComparison()
			.withComparatorForType(BigDecimalAssertion.bigDecimalComparator(), BigDecimal.class)
			.isEqualTo(expected);
	}
}
