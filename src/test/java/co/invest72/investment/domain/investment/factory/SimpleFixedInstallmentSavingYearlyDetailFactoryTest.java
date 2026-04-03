package co.invest72.investment.domain.investment.factory;

import java.math.BigDecimal;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import co.invest72.investment.domain.amount.YearlyInstallmentInvestmentAmount;
import co.invest72.investment.domain.interest.AnnualInterestRate;
import co.invest72.investment.domain.investment.YearlyInvestmentDetail;
import co.invest72.investment.domain.period.MonthlyInvestPeriod;
import co.invest72.money.domain.Money;

class SimpleFixedInstallmentSavingYearlyDetailFactoryTest {

	@DisplayName("연별 투자 상세 정보 생성 - 통화가 KRW인 경우")
	@Test
	void createDetails_whenCurrencyIsKRW_thenReturnKRWYearlyDetails() {
		// given
		SimpleFixedInstallmentSavingYearlyDetailFactory factory = new SimpleFixedInstallmentSavingYearlyDetailFactory(
			new YearlyInstallmentInvestmentAmount(Money.won(BigDecimal.valueOf(1_000_000))),
			new AnnualInterestRate(0.05),
			new MonthlyInvestPeriod(2)
		);

		// When
		List<YearlyInvestmentDetail> details = factory.createDetails();

		// then
		Assertions.assertThat(details).hasSize(2);
		Assertions.assertThat(details.get(0).getPrincipal()).isEqualTo(Money.won(BigDecimal.ZERO));
		Assertions.assertThat(details.get(0).getInterest()).isEqualTo(Money.won(BigDecimal.ZERO));
		Assertions.assertThat(details.get(0).getProfit()).isEqualTo(Money.won(BigDecimal.ZERO));
		Assertions.assertThat(details.get(1).getPrincipal()).isEqualTo(Money.won(BigDecimal.valueOf(2_000_000)));
		Assertions.assertThat(details.get(1).getInterest()).isEqualTo(Money.won(BigDecimal.valueOf(12_500)));
		Assertions.assertThat(details.get(1).getProfit()).isEqualTo(Money.won(BigDecimal.valueOf(2_012_500)));
	}

	@DisplayName("연별 투자 상세 정보 생성 - 통화가 USD인 경우")
	@Test
	void createDetails_whenCurrencyIsUSD_thenReturnUSDYearlyDetails() {
		// given
		SimpleFixedInstallmentSavingYearlyDetailFactory factory = new SimpleFixedInstallmentSavingYearlyDetailFactory(
			new YearlyInstallmentInvestmentAmount(Money.dollar(BigDecimal.valueOf(1_000_000))),
			new AnnualInterestRate(0.05),
			new MonthlyInvestPeriod(2)
		);

		// When
		List<YearlyInvestmentDetail> details = factory.createDetails();

		// then
		Assertions.assertThat(details).hasSize(2);
		Assertions.assertThat(details.get(0).getPrincipal()).isEqualTo(Money.dollar(BigDecimal.ZERO));
		Assertions.assertThat(details.get(0).getInterest()).isEqualTo(Money.dollar(BigDecimal.ZERO));
		Assertions.assertThat(details.get(0).getProfit()).isEqualTo(Money.dollar(BigDecimal.ZERO));
		Assertions.assertThat(details.get(1).getPrincipal()).isEqualTo(Money.dollar(BigDecimal.valueOf(2_000_000)));
		Assertions.assertThat(details.get(1).getInterest()).isEqualTo(Money.dollar(BigDecimal.valueOf(12_500)));
		Assertions.assertThat(details.get(1).getProfit()).isEqualTo(Money.dollar(BigDecimal.valueOf(2_012_500)));
	}
}
