package co.invest72.investment.domain.investment.factory;

import java.math.BigDecimal;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import co.invest72.investment.domain.InterestRate;
import co.invest72.investment.domain.InvestPeriod;
import co.invest72.investment.domain.InvestmentAmount;
import co.invest72.investment.domain.amount.YearlyInstallmentInvestmentAmount;
import co.invest72.investment.domain.interest.AnnualInterestRate;
import co.invest72.investment.domain.investment.YearlyInvestmentDetail;
import co.invest72.investment.domain.period.YearlyInvestPeriod;
import co.invest72.money.domain.Money;

class SimpleFixedDepositYearlyDetailFactoryTest {

	private SimpleFixedDepositYearlyDetailFactory factory;

	@DisplayName("년도 데이터 생성 - 원화 기준 데이터 생성")
	@Test
	void givenFactory_whenAmountIsKRW_thenReturnYearlyDetailsData() {
		// given
		InvestmentAmount investmentAmount = new YearlyInstallmentInvestmentAmount(Money.won(12_000_000));
		InterestRate interestRate = new AnnualInterestRate(BigDecimal.valueOf(0.05));
		InvestPeriod investPeriod = new YearlyInvestPeriod(1);
		factory = new SimpleFixedDepositYearlyDetailFactory(investmentAmount, interestRate, investPeriod);
		// when
		List<YearlyInvestmentDetail> details = factory.createDetails();
		// then
		Assertions.assertThat(details).hasSize(2);
		Assertions.assertThat(details.get(0).getPrincipal()).isEqualTo(Money.won(12_000_000));
		Assertions.assertThat(details.get(0).getInterest()).isEqualTo(Money.won(BigDecimal.ZERO));
		Assertions.assertThat(details.get(0).getProfit()).isEqualTo(Money.won(12_000_000));
		Assertions.assertThat(details.get(1).getPrincipal()).isEqualTo(Money.won(12_000_000));
		Assertions.assertThat(details.get(1).getInterest()).isEqualTo(Money.won(600000));
		Assertions.assertThat(details.get(1).getProfit()).isEqualTo(Money.won(12_600_000));
	}

	@DisplayName("년도 데이터 생성 - 달러 기준 데이터 생성")
	@Test
	void givenFactory_whenAmountIsUSD_thenReturnYearlyDetailsData() {
		// given
		InvestmentAmount investmentAmount = new YearlyInstallmentInvestmentAmount(Money.dollar(12_000_000));
		InterestRate interestRate = new AnnualInterestRate(BigDecimal.valueOf(0.05));
		InvestPeriod investPeriod = new YearlyInvestPeriod(1);
		factory = new SimpleFixedDepositYearlyDetailFactory(investmentAmount, interestRate, investPeriod);
		// when
		List<YearlyInvestmentDetail> details = factory.createDetails();
		// then
		Assertions.assertThat(details).hasSize(2);
		Assertions.assertThat(details.get(0).getPrincipal()).isEqualTo(Money.dollar(12_000_000));
		Assertions.assertThat(details.get(0).getInterest()).isEqualTo(Money.dollar(BigDecimal.ZERO));
		Assertions.assertThat(details.get(0).getProfit()).isEqualTo(Money.dollar(12_000_000));
		Assertions.assertThat(details.get(1).getPrincipal()).isEqualTo(Money.dollar(12_000_000));
		Assertions.assertThat(details.get(1).getInterest()).isEqualTo(Money.dollar(600000));
		Assertions.assertThat(details.get(1).getProfit()).isEqualTo(Money.dollar(12_600_000));
	}
}
