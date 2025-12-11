package co.invest72.investment.application;

import static co.invest72.investment.domain.interest.InterestType.*;
import static co.invest72.investment.domain.investment.InvestmentType.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import co.invest72.investment.domain.amount.AmountType;
import co.invest72.investment.domain.period.PeriodType;
import co.invest72.investment.domain.tax.TaxType;
import co.invest72.investment.presentation.request.CalculateInvestmentRequest;
import co.invest72.investment.presentation.response.CalculateMonthlyInvestmentResponse;
import co.invest72.investment.presentation.response.MonthlyInvestmentResult;

class CalculateMonthlyInvestmentTest {

	private CalculateMonthlyInvestment calculateMonthlyInvestment;

	@BeforeEach
	void setUp() {
		InvestmentFactory investmentFactory = new InvestmentFactory();
		calculateMonthlyInvestment = new CalculateMonthlyInvestment(investmentFactory, new TaxPercentFormatter());
	}

	@DisplayName("월별 투자 금액 계산 - 고정 예금, 단리, 과세")
	@Test
	void calMonthlyInvestmentAmount_shouldReturnResponse() {
		CalculateInvestmentRequest request = CalculateInvestmentRequest.builder()
			.type(FIXED_DEPOSIT.getTypeName())
			.amountType(AmountType.ONE_TIME.getDescription())
			.amount(1_000_000)
			.periodType(PeriodType.MONTH.getDisplayName())
			.periodValue(4)
			.interestType(SIMPLE.getTypeName())
			.annualInterestRate(0.05)
			.taxType(TaxType.STANDARD.getDescription())
			.taxRate(0.154)
			.build();
		CalculateMonthlyInvestmentResponse response = calculateMonthlyInvestment.calMonthlyInvestmentAmount(
			request);

		List<MonthlyInvestmentResult> details = List.of(
			new MonthlyInvestmentResult(1, 1_000_000, 4_167, 1_004_167),
			new MonthlyInvestmentResult(2, 1_000_000, 4_167, 1_004_167),
			new MonthlyInvestmentResult(3, 1_000_000, 4_167, 1_004_167),
			new MonthlyInvestmentResult(4, 1_000_000, 4_167, 1_004_167)
		);
		CalculateMonthlyInvestmentResponse expected = CalculateMonthlyInvestmentResponse.builder()
			.totalInvestment(1_000_000)
			.totalPrincipal(1_000_000)
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
		CalculateInvestmentRequest request = CalculateInvestmentRequest.builder()
			.type(FIXED_DEPOSIT.getTypeName())
			.amountType(AmountType.ONE_TIME.getDescription())
			.amount(1_000_000)
			.periodType(PeriodType.MONTH.getDisplayName())
			.periodValue(12)
			.interestType(SIMPLE.getTypeName())
			.annualInterestRate(0.05)
			.taxType(TaxType.NON_TAX.getDescription())
			.taxRate(0.0)
			.build();
		CalculateMonthlyInvestmentResponse response = calculateMonthlyInvestment.calMonthlyInvestmentAmount(
			request);

		List<MonthlyInvestmentResult> details = new ArrayList<>(IntStream.rangeClosed(1, 12)
			.mapToObj(month -> new MonthlyInvestmentResult(month, 1_000_000, 4_167, 1_004_167))
			.toList());

		CalculateMonthlyInvestmentResponse expected = CalculateMonthlyInvestmentResponse.builder()
			.totalInvestment(1_000_000)
			.totalPrincipal(1_000_000)
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
		CalculateInvestmentRequest request = CalculateInvestmentRequest.builder()
			.type(FIXED_DEPOSIT.getTypeName())
			.amountType(AmountType.ONE_TIME.getDescription())
			.amount(1_000_000)
			.periodType(PeriodType.MONTH.getDisplayName())
			.periodValue(12)
			.interestType(COMPOUND.getTypeName())
			.annualInterestRate(0.05)
			.taxType(TaxType.NON_TAX.getDescription())
			.taxRate(0.0)
			.build();
		CalculateMonthlyInvestmentResponse response = calculateMonthlyInvestment.calMonthlyInvestmentAmount(
			request);

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
			.totalInvestment(1_000_000)
			.totalPrincipal(1_046_800)
			.totalInterest(51_162)
			.totalTax(0)
			.totalProfit(1_051_162)
			.taxType(TaxType.NON_TAX.getDescription())
			.taxPercent("0%")
			.details(details)
			.build();

		Assertions.assertThat(response).isEqualTo(expected);
	}

	@DisplayName("년도별 투자 금액 계산")
	@Test
	void calYearlyInvestmentAmount() {
		CalculateInvestmentRequest request = CalculateInvestmentRequest.builder()
			.type(FIXED_DEPOSIT.getTypeName())
			.amountType(AmountType.ONE_TIME.getDescription())
			.amount(1_000_000)
			.periodType(PeriodType.MONTH.getDisplayName())
			.periodValue(12)
			.interestType(COMPOUND.getTypeName())
			.annualInterestRate(0.05)
			.taxType(TaxType.NON_TAX.getDescription())
			.taxRate(0.0)
			.build();
		CalculateMonthlyInvestmentResponse response = calculateMonthlyInvestment.calYearlyInvestmentAmount(request);

		Assertions.assertThat(response).isNotNull();
	}
}
