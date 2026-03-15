package co.invest72.investment.domain.investment.factory;

import java.math.BigDecimal;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import co.invest72.investment.domain.InterestRate;
import co.invest72.investment.domain.InvestPeriod;
import co.invest72.investment.domain.InvestmentAmount;
import co.invest72.investment.domain.amount.MonthlyInstallmentInvestmentAmount;
import co.invest72.investment.domain.interest.AnnualInterestRate;
import co.invest72.investment.domain.investment.MonthlyInvestmentDetail;
import co.invest72.investment.domain.period.MonthlyInvestPeriod;
import co.invest72.money.domain.Money;
import testutil.BigDecimalAssertion;

class SimpleFixedInstallmentSavingMonthlyDetailFactoryTest {

	@DisplayName("월별 투자 상세 정보 생성 - 통화가 KRW인 경우")
	@Test
	void createDetails_whenCurrentIsKRW_thenReturnKRWDetails() {
		// Given
		InvestmentAmount investmentAmount = new MonthlyInstallmentInvestmentAmount(
			Money.won(1_000_000));
		InterestRate interestRate = new AnnualInterestRate(0.05);
		InvestPeriod investPeriod = new MonthlyInvestPeriod(2);
		SimpleFixedInstallmentSavingMonthlyDetailFactory factory = new SimpleFixedInstallmentSavingMonthlyDetailFactory(
		);

		// When
		List<MonthlyInvestmentDetail> details = factory.createDetails(investmentAmount, interestRate, investPeriod);

		// Then
		Assertions.assertThat(details).hasSize(3);
		BigDecimalAssertion.assertBigDecimalEquals(BigDecimal.ZERO, details.get(0).getPrincipal());
		BigDecimalAssertion.assertBigDecimalEquals(BigDecimal.ZERO, details.get(0).getInterest());
		BigDecimalAssertion.assertBigDecimalEquals(BigDecimal.ZERO, details.get(0).getProfit());
		BigDecimalAssertion.assertBigDecimalEquals(BigDecimal.valueOf(1_000_000), details.get(1).getPrincipal());
		BigDecimalAssertion.assertBigDecimalEquals(BigDecimal.valueOf(4_166.67), details.get(1).getInterest());
		BigDecimalAssertion.assertBigDecimalEquals(BigDecimal.valueOf(1_004_166.67), details.get(1).getProfit());
		BigDecimalAssertion.assertBigDecimalEquals(BigDecimal.valueOf(2_004_166.67), details.get(2).getPrincipal());
		BigDecimalAssertion.assertBigDecimalEquals(BigDecimal.valueOf(8_333.33), details.get(2).getInterest());
		BigDecimalAssertion.assertBigDecimalEquals(BigDecimal.valueOf(2_012_500.00), details.get(2).getProfit());
	}

	@DisplayName("월별 투자 상세 정보 생성 - 통화가 USD인 경우")
	@Test
	void createDetails_whenCurrentIsUSD_thenReturnUSDDetails() {
		// Given
		InvestmentAmount investmentAmount = new MonthlyInstallmentInvestmentAmount(
			Money.dollar(1_000_000));
		InterestRate interestRate = new AnnualInterestRate(0.05);
		InvestPeriod investPeriod = new MonthlyInvestPeriod(2);
		SimpleFixedInstallmentSavingMonthlyDetailFactory factory = new SimpleFixedInstallmentSavingMonthlyDetailFactory(
		);

		// When
		List<MonthlyInvestmentDetail> details = factory.createDetails(investmentAmount, interestRate, investPeriod);

		// Then
		Assertions.assertThat(details).hasSize(3);
		BigDecimalAssertion.assertBigDecimalEquals(BigDecimal.ZERO, details.get(0).getPrincipal());
		BigDecimalAssertion.assertBigDecimalEquals(BigDecimal.ZERO, details.get(0).getInterest());
		BigDecimalAssertion.assertBigDecimalEquals(BigDecimal.ZERO, details.get(0).getProfit());
		BigDecimalAssertion.assertBigDecimalEquals(BigDecimal.valueOf(1_000_000), details.get(1).getPrincipal());
		BigDecimalAssertion.assertBigDecimalEquals(BigDecimal.valueOf(4_166.67), details.get(1).getInterest());
		BigDecimalAssertion.assertBigDecimalEquals(BigDecimal.valueOf(1_004_166.67), details.get(1).getProfit());
		BigDecimalAssertion.assertBigDecimalEquals(BigDecimal.valueOf(2_004_166.67), details.get(2).getPrincipal());
		BigDecimalAssertion.assertBigDecimalEquals(BigDecimal.valueOf(8_333.33), details.get(2).getInterest());
		BigDecimalAssertion.assertBigDecimalEquals(BigDecimal.valueOf(2_012_500.00), details.get(2).getProfit());
	}
}
