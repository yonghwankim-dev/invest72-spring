package co.invest72.investment.domain.investment.factory;

import java.math.BigDecimal;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import co.invest72.investment.domain.InterestRate;
import co.invest72.investment.domain.InvestPeriod;
import co.invest72.investment.domain.InvestmentAmount;
import co.invest72.investment.domain.amount.FixedDepositAmount;
import co.invest72.investment.domain.interest.AnnualInterestRate;
import co.invest72.investment.domain.interest.InterestType;
import co.invest72.investment.domain.investment.InvestmentDetail;
import co.invest72.investment.domain.period.MonthlyInvestPeriod;
import co.invest72.investment.domain.period.YearlyInvestPeriod;
import co.invest72.money.domain.Money;

class FixedDepositMonthlyDetailFactoryTest {

	private FixedDepositMonthlyDetailFactory factory;

	@BeforeEach
	void setUp() {
		InvestmentAmount investmentAmount = new FixedDepositAmount(Money.won(1_000_000));
		InterestRate interestRate = new AnnualInterestRate(BigDecimal.valueOf(0.05));
		InvestPeriod investPeriod = new MonthlyInvestPeriod(2);
		factory = new FixedDepositMonthlyDetailFactory(investmentAmount, interestRate, investPeriod,
			InterestType.SIMPLE);
	}

	@DisplayName("단리-예금-월별 데이터 생성")
	@Test
	void givenFactory_whenInterestTypeSimpleAndDepositAndMonthly_thenReturnDetails() {
		// when
		List<InvestmentDetail> details = factory.createDetails();
		// then
		Assertions.assertThat(details).hasSize(3);
		Assertions.assertThat(details.get(0).getPrincipal()).isEqualTo(Money.won(1_000_000));
		Assertions.assertThat(details.get(0).getInterest()).isEqualTo(Money.won(0));
		Assertions.assertThat(details.get(0).getProfit()).isEqualTo(Money.won(1_000_000));
		Assertions.assertThat(details.get(1).getPrincipal()).isEqualTo(Money.won(1_000_000));
		Assertions.assertThat(details.get(1).getInterest()).isEqualTo(Money.won(BigDecimal.valueOf(4_166.67)));
		Assertions.assertThat(details.get(1).getProfit()).isEqualTo(Money.won(BigDecimal.valueOf(1_004_166.67)));
		Assertions.assertThat(details.get(2).getPrincipal()).isEqualTo(Money.won(BigDecimal.valueOf(1_004_166.67)));
		Assertions.assertThat(details.get(2).getInterest()).isEqualTo(Money.won(BigDecimal.valueOf(4_166.67)));
		Assertions.assertThat(details.get(2).getProfit()).isEqualTo(Money.won(BigDecimal.valueOf(1_008_333.33)));
	}

	@DisplayName("복리-예금-월별 데이터 생성")
	@Test
	void givenFactory_whenInterestTypeCompoundAndDepositAndMonthly_thenReturnDetails() {
		// given
		factory = factory.toBuilder()
			.interestType(InterestType.COMPOUND)
			.build();
		// when
		List<InvestmentDetail> details = factory.createDetails();
		// then
		Assertions.assertThat(details).hasSize(3);
		Assertions.assertThat(details.get(0).getPrincipal()).isEqualTo(Money.won(1_000_000));
		Assertions.assertThat(details.get(0).getInterest()).isEqualTo(Money.won(0));
		Assertions.assertThat(details.get(0).getProfit()).isEqualTo(Money.won(1_000_000));
		Assertions.assertThat(details.get(1).getPrincipal()).isEqualTo(Money.won(1_000_000));
		Assertions.assertThat(details.get(1).getInterest()).isEqualTo(Money.won(BigDecimal.valueOf(4_166.67)));
		Assertions.assertThat(details.get(1).getProfit()).isEqualTo(Money.won(BigDecimal.valueOf(1_004_166.67)));
		Assertions.assertThat(details.get(2).getPrincipal()).isEqualTo(Money.won(BigDecimal.valueOf(1_004_166.67)));
		Assertions.assertThat(details.get(2).getInterest()).isEqualTo(Money.won(BigDecimal.valueOf(4_184.03)));
		Assertions.assertThat(details.get(2).getProfit()).isEqualTo(Money.won(BigDecimal.valueOf(1_008_350.69)));
	}

	@DisplayName("이자없음-예금-월별 데이터 생성")
	@Test
	void givenFactory_whenInterestTypeNoneAndDepositAndMonthly_thenReturnDetails() {
		// given
		factory = factory.toBuilder()
			.interestType(InterestType.NONE)
			.build();
		// when
		List<InvestmentDetail> details = factory.createDetails();
		// then
		Assertions.assertThat(details).hasSize(3);
		Assertions.assertThat(details.get(0).getPrincipal()).isEqualTo(Money.won(1_000_000));
		Assertions.assertThat(details.get(0).getInterest()).isEqualTo(Money.won(0));
		Assertions.assertThat(details.get(0).getProfit()).isEqualTo(Money.won(1_000_000));
		Assertions.assertThat(details.get(1).getPrincipal()).isEqualTo(Money.won(1_000_000));
		Assertions.assertThat(details.get(1).getInterest()).isEqualTo(Money.won(BigDecimal.valueOf(0)));
		Assertions.assertThat(details.get(1).getProfit()).isEqualTo(Money.won(BigDecimal.valueOf(1_000_000)));
		Assertions.assertThat(details.get(2).getPrincipal()).isEqualTo(Money.won(BigDecimal.valueOf(1_000_000)));
		Assertions.assertThat(details.get(2).getInterest()).isEqualTo(Money.won(BigDecimal.valueOf(0)));
		Assertions.assertThat(details.get(2).getProfit()).isEqualTo(Money.won(BigDecimal.valueOf(1_000_000)));
	}

	@DisplayName("단리-예금-년도별 데이터 생성")
	@Test
	void givenFactory_whenInterestTypeSimpleAndDepositAndYearly_thenReturnDetails() {
		// given
		factory = factory.toBuilder()
			.investPeriod(new YearlyInvestPeriod(1))
			.build();

		// when
		List<InvestmentDetail> details = factory.createYearlyDetails();
		// then
		Assertions.assertThat(details).hasSize(2);
		Assertions.assertThat(details.get(0).getPrincipal()).isEqualTo(Money.won(1_000_000));
		Assertions.assertThat(details.get(0).getInterest()).isEqualTo(Money.won(0));
		Assertions.assertThat(details.get(0).getProfit()).isEqualTo(Money.won(1_000_000));
		Assertions.assertThat(details.get(1).getPrincipal()).isEqualTo(Money.won(1_000_000));
		Assertions.assertThat(details.get(1).getInterest()).isEqualTo(Money.won(BigDecimal.valueOf(50_000)));
		Assertions.assertThat(details.get(1).getProfit()).isEqualTo(Money.won(BigDecimal.valueOf(1_050_000)));
	}

	@DisplayName("단리-예금-년도별 데이터 생성 - 투자기간이 13개월인 경우 2년차 데이터가 존재해야 한다")
	@Test
	void givenFactory_whenInterestTypeSimpleAndDepositAndYearlyAndPeriodIs13_thenReturnDetails() {
		// given
		factory = factory.toBuilder()
			.investPeriod(new MonthlyInvestPeriod(13))
			.build();

		// when
		List<InvestmentDetail> details = factory.createYearlyDetails();
		// then
		Assertions.assertThat(details).hasSize(3);
		Assertions.assertThat(details.get(0).getPrincipal()).isEqualTo(Money.won(1_000_000));
		Assertions.assertThat(details.get(0).getInterest()).isEqualTo(Money.won(0));
		Assertions.assertThat(details.get(0).getProfit()).isEqualTo(Money.won(1_000_000));
		Assertions.assertThat(details.get(1).getPrincipal()).isEqualTo(Money.won(1_000_000));
		Assertions.assertThat(details.get(1).getInterest()).isEqualTo(Money.won(BigDecimal.valueOf(50_000)));
		Assertions.assertThat(details.get(1).getProfit()).isEqualTo(Money.won(BigDecimal.valueOf(1_050_000)));
		Assertions.assertThat(details.get(2).getPrincipal()).isEqualTo(Money.won(1_050_000));
		Assertions.assertThat(details.get(2).getInterest()).isEqualTo(Money.won(BigDecimal.valueOf(4_166.67)));
		Assertions.assertThat(details.get(2).getProfit()).isEqualTo(Money.won(BigDecimal.valueOf(1_054_166.67)));
	}

	@DisplayName("단리-예금-월별 데이터 생성 - 투자기간이 0개월인 경우 1개의 데이터만 반환해야 한다")
	@Test
	void givenFactory_whenInterestTypeSimpleAndDepositAndMonthlyAndPeriodIsZero_thenReturnDetails() {
		// given
		factory = factory.toBuilder()
			.investPeriod(new MonthlyInvestPeriod(0))
			.build();

		// when
		List<InvestmentDetail> details = factory.createDetails();
		// then
		Assertions.assertThat(details).hasSize(1);
		Assertions.assertThat(details.get(0).getPrincipal()).isEqualTo(Money.won(1_000_000));
		Assertions.assertThat(details.get(0).getInterest()).isEqualTo(Money.won(0));
		Assertions.assertThat(details.get(0).getProfit()).isEqualTo(Money.won(1_000_000));
	}
}
