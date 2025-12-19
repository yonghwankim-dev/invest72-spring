package co.invest72.investment.domain.investment;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import co.invest72.investment.domain.InterestRate;
import co.invest72.investment.domain.InvestPeriod;
import co.invest72.investment.domain.InvestmentAmount;
import co.invest72.investment.domain.amount.FixedDepositAmount;
import co.invest72.investment.domain.interest.AnnualInterestRate;
import co.invest72.investment.domain.period.MonthlyInvestPeriod;

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
		List<MonthlyInvestmentDetail> details = factory.createMonthlyDetails();

		Assertions.assertThat(details).hasSize(13);
	}
}
