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
import co.invest72.investment.domain.amount.MonthlyInstallmentInvestmentAmount;
import co.invest72.investment.domain.interest.AnnualInterestRate;
import co.invest72.investment.domain.interest.InterestType;
import co.invest72.investment.domain.investment.InvestmentDetail;
import co.invest72.investment.domain.period.MonthlyInvestPeriod;
import co.invest72.investment.domain.period.YearlyInvestPeriod;
import co.invest72.money.domain.Money;

class SavingDetailFactoryTest {

	private InvestmentDetailFactory factory;

	@BeforeEach
	void setUp() {
		InvestmentAmount investmentAmount = new MonthlyInstallmentInvestmentAmount(
			Money.won(1_000_000));
		InterestRate interestRate = new AnnualInterestRate(0.05);
		InvestPeriod investPeriod = new MonthlyInvestPeriod(2);
		factory = new SavingDetailFactory(investmentAmount, interestRate, investPeriod, InterestType.SIMPLE);
	}

	@DisplayName("단리-적금-월별 데이터 생성 - 통화가 KRW인 경우")
	@Test
	void createDetails_whenCurrentIsKRW_thenReturnKRWDetails() {
		// When
		List<InvestmentDetail> details = factory.createMonthlyDetails();

		// Then
		Assertions.assertThat(details).hasSize(3);
		Assertions.assertThat(details.get(0).getPrincipal()).isEqualTo(Money.won(0));
		Assertions.assertThat(details.get(0).getInterest()).isEqualTo(Money.won(0));
		Assertions.assertThat(details.get(0).getProfit()).isEqualTo(Money.won(0));
		Assertions.assertThat(details.get(1).getPrincipal()).isEqualTo(Money.won(BigDecimal.valueOf(1_000_000)));
		Assertions.assertThat(details.get(1).getInterest()).isEqualTo(Money.won(BigDecimal.valueOf(4_166.67)));
		Assertions.assertThat(details.get(1).getProfit()).isEqualTo(Money.won(BigDecimal.valueOf(1_004_166.67)));
		Assertions.assertThat(details.get(2).getPrincipal()).isEqualTo(Money.won(BigDecimal.valueOf(2_004_166.67)));
		Assertions.assertThat(details.get(2).getInterest()).isEqualTo(Money.won(BigDecimal.valueOf(8_333.33)));
		Assertions.assertThat(details.get(2).getProfit()).isEqualTo(Money.won(BigDecimal.valueOf(2_012_500.00)));
	}

	@DisplayName("단리-적금-월별 데이터 생성 - 통화가 USD인 경우")
	@Test
	void createDetails_whenCurrentIsUSD_thenReturnUSDDetails() {
		// Given
		factory = ((SavingDetailFactory)factory).toBuilder()
			.investmentAmount(new MonthlyInstallmentInvestmentAmount(Money.dollar(1_000_000)))
			.build();

		// When
		List<InvestmentDetail> details = factory.createMonthlyDetails();

		// Then
		Assertions.assertThat(details).hasSize(3);
		Assertions.assertThat(details.get(0).getPrincipal()).isEqualTo(Money.dollar(0));
		Assertions.assertThat(details.get(0).getInterest()).isEqualTo(Money.dollar(0));
		Assertions.assertThat(details.get(0).getProfit()).isEqualTo(Money.dollar(0));
		Assertions.assertThat(details.get(1).getPrincipal()).isEqualTo(Money.dollar(BigDecimal.valueOf(1_000_000)));
		Assertions.assertThat(details.get(1).getInterest()).isEqualTo(Money.dollar(BigDecimal.valueOf(4_166.67)));
		Assertions.assertThat(details.get(1).getProfit()).isEqualTo(Money.dollar(BigDecimal.valueOf(1_004_166.67)));
		Assertions.assertThat(details.get(2).getPrincipal()).isEqualTo(Money.dollar(BigDecimal.valueOf(2_004_166.67)));
		Assertions.assertThat(details.get(2).getInterest()).isEqualTo(Money.dollar(BigDecimal.valueOf(8_333.33)));
		Assertions.assertThat(details.get(2).getProfit()).isEqualTo(Money.dollar(BigDecimal.valueOf(2_012_500.00)));
	}

	@DisplayName("복리-적금-월별 데이터 생성 - 통화가 KRW인 경우")
	@Test
	void givenFactory_whenCompoundAndMonthlyAndKRW_thenReturnDetails() {
		// given
		factory = ((SavingDetailFactory)factory).toBuilder()
			.interestType(InterestType.COMPOUND)
			.build();
		// When
		List<InvestmentDetail> details = factory.createMonthlyDetails();

		// Then
		Assertions.assertThat(details).hasSize(3);
		Assertions.assertThat(details.get(0).getPrincipal()).isEqualTo(Money.won(0));
		Assertions.assertThat(details.get(0).getInterest()).isEqualTo(Money.won(0));
		Assertions.assertThat(details.get(0).getProfit()).isEqualTo(Money.won(0));
		Assertions.assertThat(details.get(1).getPrincipal()).isEqualTo(Money.won(BigDecimal.valueOf(1_000_000)));
		Assertions.assertThat(details.get(1).getInterest()).isEqualTo(Money.won(BigDecimal.valueOf(4_166.67)));
		Assertions.assertThat(details.get(1).getProfit()).isEqualTo(Money.won(BigDecimal.valueOf(1_004_166.67)));
		Assertions.assertThat(details.get(2).getPrincipal()).isEqualTo(Money.won(BigDecimal.valueOf(2_004_166.67)));
		Assertions.assertThat(details.get(2).getInterest()).isEqualTo(Money.won(BigDecimal.valueOf(8_350.69)));
		Assertions.assertThat(details.get(2).getProfit()).isEqualTo(Money.won(BigDecimal.valueOf(2_012_517.36)));
	}

	@DisplayName("단리-적금-년도별 데이터 생성 - 통화가 KRW인 경우")
	@Test
	void givenFactory_whenSimpleYearlyKRW_thenReturnDetails() {
		// given
		factory = ((SavingDetailFactory)factory).toBuilder()
			.investPeriod(new MonthlyInvestPeriod(13))
			.build();
		// When
		List<InvestmentDetail> details = factory.createYearlyDetails();

		// Then
		Assertions.assertThat(details).hasSize(3);
		Assertions.assertThat(details.get(0).getPrincipal()).isEqualTo(Money.won(0));
		Assertions.assertThat(details.get(0).getInterest()).isEqualTo(Money.won(0));
		Assertions.assertThat(details.get(0).getProfit()).isEqualTo(Money.won(0));
		Assertions.assertThat(details.get(1).getPrincipal()).isEqualTo(Money.won(BigDecimal.valueOf(12_000_000)));
		Assertions.assertThat(details.get(1).getInterest()).isEqualTo(Money.won(BigDecimal.valueOf(325_000)));
		Assertions.assertThat(details.get(1).getProfit()).isEqualTo(Money.won(BigDecimal.valueOf(12_325_000)));
		Assertions.assertThat(details.get(2).getPrincipal()).isEqualTo(Money.won(BigDecimal.valueOf(13_325_000)));
		Assertions.assertThat(details.get(2).getInterest()).isEqualTo(Money.won(BigDecimal.valueOf(54_166.67)));
		Assertions.assertThat(details.get(2).getProfit()).isEqualTo(Money.won(BigDecimal.valueOf(13_379_166.67)));
	}

	@DisplayName("복리-적금-년도별 데이터 생성 - 통화가 KRW인 경우")
	@Test
	void givenFactory_whenCompoundYearlyKRW_thenReturnDetails() {
		// given
		factory = ((SavingDetailFactory)factory).toBuilder()
			.investPeriod(new YearlyInvestPeriod(1))
			.interestType(InterestType.COMPOUND)
			.build();
		// When
		List<InvestmentDetail> details = factory.createYearlyDetails();

		// Then
		Assertions.assertThat(details).hasSize(2);
		Assertions.assertThat(details.get(0).getPrincipal()).isEqualTo(Money.won(0));
		Assertions.assertThat(details.get(0).getInterest()).isEqualTo(Money.won(0));
		Assertions.assertThat(details.get(0).getProfit()).isEqualTo(Money.won(0));
		Assertions.assertThat(details.get(1).getPrincipal()).isEqualTo(Money.won(BigDecimal.valueOf(12_000_000)));
		Assertions.assertThat(details.get(1).getInterest()).isEqualTo(Money.won(BigDecimal.valueOf(330_017.39)));
		Assertions.assertThat(details.get(1).getProfit()).isEqualTo(Money.won(BigDecimal.valueOf(12_330_017.39)));
	}

	@DisplayName("복리-적금-년도별 데이터 생성 - 13개월인 경우 2년차의 이자수익은 1개월분만 반영되어야 한다")
	@Test
	void givenFactory_whenInvestPeriodIs13_thenReflectedForOneMonthOnly() {
		// given
		factory = ((SavingDetailFactory)factory).toBuilder()
			.investPeriod(new MonthlyInvestPeriod(13))
			.interestType(InterestType.COMPOUND)
			.build();
		// When
		List<InvestmentDetail> details = factory.createYearlyDetails();

		// Then
		Assertions.assertThat(details).hasSize(3);
		Assertions.assertThat(details.get(0).getPrincipal()).isEqualTo(Money.won(0));
		Assertions.assertThat(details.get(0).getInterest()).isEqualTo(Money.won(0));
		Assertions.assertThat(details.get(0).getProfit()).isEqualTo(Money.won(0));
		Assertions.assertThat(details.get(1).getPrincipal()).isEqualTo(Money.won(BigDecimal.valueOf(12_000_000)));
		Assertions.assertThat(details.get(1).getInterest()).isEqualTo(Money.won(BigDecimal.valueOf(330_017.39)));
		Assertions.assertThat(details.get(1).getProfit()).isEqualTo(Money.won(BigDecimal.valueOf(12_330_017.39)));
		Assertions.assertThat(details.get(2).getPrincipal()).isEqualTo(Money.won(BigDecimal.valueOf(13_330_017.39)));
		Assertions.assertThat(details.get(2).getInterest()).isEqualTo(Money.won(BigDecimal.valueOf(55541.74)));
		Assertions.assertThat(details.get(2).getProfit()).isEqualTo(Money.won(BigDecimal.valueOf(13_385_559.13)));
	}
}
