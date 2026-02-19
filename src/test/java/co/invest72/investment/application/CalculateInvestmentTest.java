package co.invest72.investment.application;

import static co.invest72.investment.domain.interest.InterestType.*;
import static co.invest72.investment.domain.investment.InvestmentType.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import co.invest72.investment.domain.amount.AmountType;
import co.invest72.investment.domain.tax.TaxType;
import co.invest72.investment.presentation.request.CalculateInvestmentRequest;

class CalculateInvestmentTest {

	private CalculateExpirationInvestment investmentUseCase;
	private String investmentType;
	private int amount;
	private String periodType;
	private int periodValue;
	private String interestType;
	private double annualInterestRate;
	private String taxable;
	private double taxRate;
	private CalculateInvestmentRequest request;

	@BeforeEach
	void setUp() {
		InvestmentFactory investmentFactory = new InvestmentFactory();
		investmentUseCase = new CalculateExpirationInvestment(investmentFactory, new TaxPercentFormatter());
		investmentType = FIXED_DEPOSIT.getTypeName();
		String amountType = AmountType.ONE_TIME.getDescription();
		amount = 1_000_000;
		periodType = "년";
		periodValue = 1;
		interestType = SIMPLE.getTypeName();
		annualInterestRate = 0.05;
		taxable = TaxType.NON_TAX.getDescription();
		taxRate = 0.0; // 비과세이므로 세율은 0.0

		request = CalculateInvestmentRequest.builder()
			.type(investmentType)
			.amountType(amountType)
			.amount(amount)
			.periodType(periodType)
			.periodValue(periodValue)
			.interestType(interestType)
			.annualInterestRate(annualInterestRate)
			.taxType(taxable)
			.taxRate(taxRate)
			.build();
	}

	@DisplayName("복리 적금 계산 테스트")
	@Test
	void calAmount_shouldReturnCalAmountResponse() {
		investmentType = INSTALLMENT_SAVING.getTypeName();
		amount = 1_000_000;
		interestType = COMPOUND.getTypeName();

		request = CalculateInvestmentRequest.builder()
			.type(investmentType)
			.amountType(AmountType.MONTHLY.getDescription())
			.amount(amount)
			.periodType(periodType)
			.periodValue(periodValue)
			.interestType(interestType)
			.annualInterestRate(annualInterestRate)
			.taxType(taxable)
			.taxRate(taxRate)
			.build();

		CalculateExpirationInvestment.CalculateExpirationInvestmentResponse response = investmentUseCase.calInvestment(
			request);

		int expectedTotalProfitAmount = 12_330_017;
		assertEquals(expectedTotalProfitAmount, response.totalProfit());

		int expectedTotalPrincipal = 12_278_855;
		assertEquals(expectedTotalPrincipal, response.totalPrincipal());

		int expectedInterest = 330_017;
		assertEquals(expectedInterest, response.totalInterest());

		int expectedTax = 0;
		assertEquals(expectedTax, response.totalTax());
	}
}
