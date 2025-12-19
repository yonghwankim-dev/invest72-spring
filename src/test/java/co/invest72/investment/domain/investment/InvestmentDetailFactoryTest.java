package co.invest72.investment.domain.investment;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InvestmentDetailFactoryTest {

	private InvestmentDetailFactory<MonthlyInvestmentDetail> factory;

	@BeforeEach
	void setUp() {
		factory = new InvestmentDetailFactory<>();
	}

	@Test
	void canCreated() {
		Assertions.assertThat(factory).isNotNull();
	}

	@Test
	void createMonthlyDetails() {

		List<MonthlyInvestmentDetail> details = factory.createMonthlyDetails();

		Assertions.assertThat(details).isNotNull();
	}
}
