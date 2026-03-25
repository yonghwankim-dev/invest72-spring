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
import testutil.BigDecimalAssertion;

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
		BigDecimalAssertion.assertBigDecimalEquals(BigDecimal.ZERO, details.get(0).getPrincipal());
		BigDecimalAssertion.assertBigDecimalEquals(BigDecimal.ZERO, details.get(0).getInterest());
		BigDecimalAssertion.assertBigDecimalEquals(BigDecimal.ZERO, details.get(0).getProfit());
		BigDecimalAssertion.assertBigDecimalEquals(BigDecimal.valueOf(2_000_000), details.get(1).getPrincipal());
		BigDecimalAssertion.assertBigDecimalEquals(BigDecimal.valueOf(12_500), details.get(1).getInterest());
		BigDecimalAssertion.assertBigDecimalEquals(BigDecimal.valueOf(2_012_500), details.get(1).getProfit());
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
		BigDecimalAssertion.assertBigDecimalEquals(BigDecimal.ZERO, details.get(0).getPrincipal());
		BigDecimalAssertion.assertBigDecimalEquals(BigDecimal.ZERO, details.get(0).getInterest());
		BigDecimalAssertion.assertBigDecimalEquals(BigDecimal.ZERO, details.get(0).getProfit());
		BigDecimalAssertion.assertBigDecimalEquals(BigDecimal.valueOf(2_000_000), details.get(1).getPrincipal());
		BigDecimalAssertion.assertBigDecimalEquals(BigDecimal.valueOf(12_500), details.get(1).getInterest());
		BigDecimalAssertion.assertBigDecimalEquals(BigDecimal.valueOf(2_012_500), details.get(1).getProfit());
	}
}
