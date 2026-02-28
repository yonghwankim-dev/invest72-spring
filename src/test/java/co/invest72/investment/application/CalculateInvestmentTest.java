package co.invest72.investment.application;

import static co.invest72.investment.domain.interest.InterestType.*;
import static co.invest72.investment.domain.investment.InvestmentType.*;

import java.math.BigDecimal;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import co.invest72.investment.domain.Investment;
import co.invest72.investment.domain.amount.AmountType;
import co.invest72.investment.domain.period.PeriodType;
import co.invest72.investment.domain.tax.TaxType;
import co.invest72.investment.presentation.request.CalculateInvestmentRequest;
import co.invest72.investment.presentation.response.CalculateMonthlyInvestmentResponse;
import co.invest72.investment.presentation.response.CalculateYearlyInvestmentResponse;
import co.invest72.investment.presentation.response.MonthlyInvestmentResult;
import co.invest72.investment.presentation.response.YearlyInvestmentResult;

class CalculateInvestmentTest {

	private CalculateInvestment calculateMonthlyInvestment;
	private CalculateInvestmentRequest request;
	private InvestmentFactory investmentFactory;

	@BeforeEach
	void setUp() {
		investmentFactory = new InvestmentFactory();
		calculateMonthlyInvestment = new CalculateInvestment(new TaxPercentFormatter());

		request = CalculateInvestmentRequest.builder()
			.type(DEPOSIT.getTypeName())
			.amountType(AmountType.ONE_TIME.getDescription())
			.amount(1_000_000)
			.periodType(PeriodType.MONTH.getDisplayName())
			.periodValue(36)
			.interestType(SIMPLE.getTypeName())
			.annualInterestRate(0.05)
			.taxType(TaxType.STANDARD.getDescription())
			.taxRate(0.154)
			.build();
	}

	@DisplayName("월별 투자 금액 계산 - 고정 예금, 단리, 과세")
	@Test
	void calMonthlyInvestmentAmount_shouldReturnResponse() {
		request = request.toBuilder()
			.periodValue(4)
			.build();
		Investment investment = investmentFactory.createBy(request);

		CalculateMonthlyInvestmentResponse response = calculateMonthlyInvestment.calMonthlyInvestment(investment);

		List<MonthlyInvestmentResult> details = List.of(
			new MonthlyInvestmentResult(1, 1_000_000, 4_167, 1_004_167),
			new MonthlyInvestmentResult(2, 1_004_167, 4_167, 1_008_333),
			new MonthlyInvestmentResult(3, 1_008_333, 4_167, 1_012_500),
			new MonthlyInvestmentResult(4, 1_012_500, 4_167, 1_016_667)
		);
		CalculateMonthlyInvestmentResponse expected = CalculateMonthlyInvestmentResponse.builder()
			.totalInvestment(BigDecimal.valueOf(1_000_000))
			.totalInterest(16_667)
			.totalTax(2_567)
			.totalProfit(1_014_100)
			.taxType(TaxType.STANDARD.getDescription())
			.taxPercent("15.4%")
			.details(details)
			.build();
		Assertions.assertThat(response).isEqualTo(expected);
	}

	@DisplayName("월별 투자 금액 계산 - 고정 예금, 단리, 비과세")
	@Test
	void calMonthlyInvestmentAmount_shouldSimpleFixedDeposit() {
		request = request.toBuilder()
			.periodValue(12)
			.taxType(TaxType.NON_TAX.getDescription())
			.taxRate(0.0)
			.build();
		Investment investment = investmentFactory.createBy(request);

		CalculateMonthlyInvestmentResponse response = calculateMonthlyInvestment.calMonthlyInvestment(investment);

		List<MonthlyInvestmentResult> details = List.of(
			new MonthlyInvestmentResult(1, 1_000_000, 4_167, 1_004_167),
			new MonthlyInvestmentResult(2, 1_004_167, 4_167, 1_008_333),
			new MonthlyInvestmentResult(3, 1_008_333, 4_167, 1_012_500),
			new MonthlyInvestmentResult(4, 1_012_500, 4_167, 1_016_667),
			new MonthlyInvestmentResult(5, 1_016_667, 4_167, 1_020_833),
			new MonthlyInvestmentResult(6, 1_020_833, 4_167, 1_025_000),
			new MonthlyInvestmentResult(7, 1_025_000, 4_167, 1_029_167),
			new MonthlyInvestmentResult(8, 1_029_167, 4_167, 1_033_333),
			new MonthlyInvestmentResult(9, 1_033_333, 4_167, 1_037_500),
			new MonthlyInvestmentResult(10, 1_037_500, 4_167, 1_041_667),
			new MonthlyInvestmentResult(11, 1_041_667, 4_167, 1_045_833),
			new MonthlyInvestmentResult(12, 1_045_833, 4_167, 1_050_000)
		);

		CalculateMonthlyInvestmentResponse expected = CalculateMonthlyInvestmentResponse.builder()
			.totalInvestment(BigDecimal.valueOf(1_000_000))
			.totalInterest(50_000)
			.totalTax(0)
			.totalProfit(1_050_000)
			.taxType(TaxType.NON_TAX.getDescription())
			.taxPercent("0%")
			.details(details)
			.build();
		Assertions.assertThat(response).isEqualTo(expected);
	}

	@DisplayName("월별 투자 금액 계산 - 고정 예금, 복리, 비과세")
	@Test
	void calMonthlyInvestmentAmount_whenCompoundFixedDeposit() {
		request = request.toBuilder()
			.periodValue(12)
			.interestType(COMPOUND.getTypeName())
			.taxType(TaxType.NON_TAX.getDescription())
			.taxRate(0.0)
			.build();
		Investment investment = investmentFactory.createBy(request);

		CalculateMonthlyInvestmentResponse response = calculateMonthlyInvestment.calMonthlyInvestment(investment);

		List<MonthlyInvestmentResult> details = List.of(
			new MonthlyInvestmentResult(1, 1_000_000, 4_167, 1_004_167),
			new MonthlyInvestmentResult(2, 1_004_167, 4184, 1008351),
			new MonthlyInvestmentResult(3, 1008351, 4201, 1012552),
			new MonthlyInvestmentResult(4, 1012552, 4219, 1016771),
			new MonthlyInvestmentResult(5, 1016771, 4237, 1021008),
			new MonthlyInvestmentResult(6, 1021008, 4254, 1025262),
			new MonthlyInvestmentResult(7, 1025262, 4272, 1029534),
			new MonthlyInvestmentResult(8, 1029534, 4290, 1033824),
			new MonthlyInvestmentResult(9, 1033824, 4308, 1038131),
			new MonthlyInvestmentResult(10, 1038131, 4326, 1042457),
			new MonthlyInvestmentResult(11, 1042457, 4344, 1046800),
			new MonthlyInvestmentResult(12, 1046800, 4362, 1051162)
		);
		CalculateMonthlyInvestmentResponse expected = CalculateMonthlyInvestmentResponse.builder()
			.totalInvestment(BigDecimal.valueOf(1_000_000))
			.totalInterest(51_162)
			.totalTax(0)
			.totalProfit(1_051_162)
			.taxType(TaxType.NON_TAX.getDescription())
			.taxPercent("0%")
			.details(details)
			.build();

		Assertions.assertThat(response).isEqualTo(expected);
	}

	@DisplayName("년도별 투자 금액 계산 - 고정 예금, 단리, 과세, 4개월")
	@Test
	void calYearlyInvestmentAmount_whenPeriodIs4Months() {
		request = request.toBuilder()
			.periodValue(4)
			.build();
		Investment investment = investmentFactory.createBy(request);

		CalculateYearlyInvestmentResponse response = calculateMonthlyInvestment.calYearlyInvestment(investment);

		List<YearlyInvestmentResult> details = List.of(
			new YearlyInvestmentResult(1, 1_000_000, 16_667, 1_016_667)
		);
		CalculateYearlyInvestmentResponse expected = CalculateYearlyInvestmentResponse.builder()
			.totalInvestment(BigDecimal.valueOf(1_000_000))
			.totalInterest(16_667)
			.totalTax(2_567)
			.totalProfit(1_014_100)
			.taxType(TaxType.STANDARD.getDescription())
			.taxPercent("15.4%")
			.details(details)
			.build();
		Assertions.assertThat(response).isEqualTo(expected);
	}

	@DisplayName("년도별 투자 금액 계산 - 고정 예금, 단리, 과세, 36개월")
	@Test
	void calYearlyInvestmentAmount_whenPeriodIs36Months() {
		request = request.toBuilder()
			.periodValue(36)
			.build();
		Investment investment = investmentFactory.createBy(request);

		CalculateYearlyInvestmentResponse response = calculateMonthlyInvestment.calYearlyInvestment(investment);

		List<YearlyInvestmentResult> details = List.of(
			new YearlyInvestmentResult(1, 1_000_000, 50_000, 1_050_000),
			new YearlyInvestmentResult(2, 1_050_000, 50_000, 1_100_000),
			new YearlyInvestmentResult(3, 1_100_000, 50_000, 1_150_000)
		);
		CalculateYearlyInvestmentResponse expected = CalculateYearlyInvestmentResponse.builder()
			.totalInvestment(BigDecimal.valueOf(1_000_000))
			.totalInterest(150_000)
			.totalTax(23_100)
			.totalProfit(1_126_900)
			.taxType(TaxType.STANDARD.getDescription())
			.taxPercent("15.4%")
			.details(details)
			.build();
		Assertions.assertThat(response).isEqualTo(expected);
	}

	@DisplayName("년도별 투자 금액 계산 - 고정 예금, 단리, 비과세, 24개월")
	@Test
	void calYearlyInvestmentAmount_whenPeriodIs24Months() {
		request = request.toBuilder()
			.periodValue(24)
			.taxType(TaxType.NON_TAX.getDescription())
			.taxRate(0.0)
			.build();
		Investment investment = investmentFactory.createBy(request);

		CalculateYearlyInvestmentResponse response = calculateMonthlyInvestment.calYearlyInvestment(investment);

		List<YearlyInvestmentResult> details = List.of(
			new YearlyInvestmentResult(1, 1_000_000, 50_000, 1_050_000),
			new YearlyInvestmentResult(2, 1_050_000, 50_000, 1_100_000)
		);
		CalculateYearlyInvestmentResponse expected = CalculateYearlyInvestmentResponse.builder()
			.totalInvestment(BigDecimal.valueOf(1_000_000))
			.totalInterest(100_000)
			.totalTax(0)
			.totalProfit(1_100_000)
			.taxType(TaxType.NON_TAX.getDescription())
			.taxPercent("0%")
			.details(details)
			.build();
		Assertions.assertThat(response).isEqualTo(expected);
	}

	@DisplayName("년도별 투자 금액 계산 - 고정 예금, 단리, 비과세, 13개월")
	@Test
	void calYearlyInvestmentAmount_whenPeriodIs13MonthsAndNonTax() {
		request = request.toBuilder()
			.periodValue(13)
			.taxType(TaxType.NON_TAX.getDescription())
			.taxRate(0.0)
			.build();
		Investment investment = investmentFactory.createBy(request);

		CalculateYearlyInvestmentResponse response = calculateMonthlyInvestment.calYearlyInvestment(investment);

		List<YearlyInvestmentResult> details = List.of(
			new YearlyInvestmentResult(1, 1_000_000, 50_000, 1_050_000),
			new YearlyInvestmentResult(2, 1_050_000, 4_167, 1_054_167)
		);
		CalculateYearlyInvestmentResponse expected = CalculateYearlyInvestmentResponse.builder()
			.totalInvestment(BigDecimal.valueOf(1_000_000))
			.totalInterest(54_167)
			.totalTax(0)
			.totalProfit(1_054_167)
			.taxType(TaxType.NON_TAX.getDescription())
			.taxPercent("0%")
			.details(details)
			.build();
		Assertions.assertThat(response).isEqualTo(expected);
	}
}
