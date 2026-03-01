package co.invest72.investment.application;

import static co.invest72.investment.domain.amount.AmountType.*;
import static co.invest72.investment.domain.interest.InterestType.*;
import static co.invest72.investment.domain.investment.InvestmentType.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import co.invest72.financial_product.domain.FinancialProduct;
import co.invest72.financial_product.domain.ProductAmount;
import co.invest72.financial_product.domain.ProductMonths;
import co.invest72.investment.application.dto.CalculateInvestmentDto;
import co.invest72.investment.domain.CashInvestment;
import co.invest72.investment.domain.Investment;
import co.invest72.investment.domain.interest.AnnualInterestRate;
import co.invest72.investment.domain.investment.CompoundFixedDeposit;
import co.invest72.investment.domain.investment.CompoundFixedInstallmentSaving;
import co.invest72.investment.domain.investment.SimpleFixedDeposit;
import co.invest72.investment.domain.investment.SimpleFixedInstallmentSaving;
import co.invest72.investment.domain.tax.FixedTaxRate;
import co.invest72.investment.domain.tax.TaxType;
import co.invest72.investment.presentation.request.CalculateInvestmentRequest;
import source.FinancialProductDataProvider;

class InvestmentFactoryTest {

	private InvestmentFactory investmentFactory;
	private CalculateInvestmentRequest request;
	private Investment investment;

	private void assertInstanceOfInvestment(Class<?> expectedType, Investment investment) {
		assertInstanceOf(expectedType, investment);
	}

	@BeforeEach
	void setUp() {
		investmentFactory = new InvestmentFactory();
	}

	@DisplayName("투자 객체 생성 - 단리-예금 객체 생성")
	@Test
	void shouldReturnInvestment_whenRequestIsSimpleFixedDeposit() {
		request = CalculateInvestmentRequest.builder()
			.type(DEPOSIT.getTypeName())
			.amountType(ONE_TIME.getDescription())
			.amount(1_000_000)
			.periodType("년")
			.periodValue(1)
			.interestType(SIMPLE.getTypeName())
			.annualInterestRate(0.05)
			.taxType(TaxType.NON_TAX.getDescription())
			.taxRate(0.0)
			.build();

		investment = investmentFactory.createBy(request);

		assertNotNull(investment);
		assertInstanceOfInvestment(SimpleFixedDeposit.class, investment);
	}

	@DisplayName("투자 객체 생성 - 복리-예금 객체 생성")
	@Test
	void shouldInstanceOfCompoundFixedDeposit_whenRequestIsCompoundFixedDeposit() {
		request = CalculateInvestmentRequest.builder()
			.type(DEPOSIT.getTypeName())
			.amountType(ONE_TIME.getDescription())
			.amount(1_000_000)
			.periodType("년")
			.periodValue(1)
			.interestType(COMPOUND.getTypeName())
			.annualInterestRate(0.05)
			.taxType(TaxType.NON_TAX.getDescription())
			.taxRate(0.0)
			.build();

		investment = investmentFactory.createBy(request);

		assertNotNull(investment);
		assertInstanceOfInvestment(CompoundFixedDeposit.class, investment);
	}

	@DisplayName("투자 객체 생성 - 단리-적금 객체 생성")
	@Test
	void shouldInstanceOfSimpleFixedInstallmentSaving_whenRequestIsSimpleFixedInstallmentSaving() {
		request = CalculateInvestmentRequest.builder()
			.type(SAVINGS.getTypeName())
			.amountType(MONTHLY.getDescription())
			.amount(1_000_000)
			.periodType("년")
			.periodValue(1)
			.interestType(SIMPLE.getTypeName())
			.annualInterestRate(0.05)
			.taxType(TaxType.NON_TAX.getDescription())
			.taxRate(0.0)
			.build();

		investment = investmentFactory.createBy(request);

		assertNotNull(investment);
		assertInstanceOfInvestment(SimpleFixedInstallmentSaving.class, investment);
	}

	@DisplayName("투자 객체 생성 - 단리-적금-년적립")
	@Test
	void createBy_whenProductIsSimpleSavingsAndYearlyAmount_thenReturnInvestment() {
		request = CalculateInvestmentRequest.builder()
			.type(SAVINGS.getTypeName())
			.amountType(YEARLY.getDescription())
			.amount(12_000_000)
			.periodType("년")
			.periodValue(1)
			.interestType(SIMPLE.getTypeName())
			.annualInterestRate(0.05)
			.taxType(TaxType.NON_TAX.getDescription())
			.taxRate(0.0)
			.build();

		investment = investmentFactory.createBy(request);

		assertNotNull(investment);
		assertInstanceOfInvestment(SimpleFixedInstallmentSaving.class, investment);
		assertEquals(BigDecimal.valueOf(12_000_000), investment.getTotalInvestment());
	}

	@DisplayName("투자 객체 생성 - 복리-적금 객체 생성")
	@Test
	void shouldInstanceOfCompoundFixedInstallmentSaving_whenRequestIsCompoundFixedInstallmentSaving() {
		request = CalculateInvestmentRequest.builder()
			.type(SAVINGS.getTypeName())
			.amountType(MONTHLY.getDescription())
			.amount(1_000_000)
			.periodType("년")
			.periodValue(1)
			.interestType(COMPOUND.getTypeName())
			.annualInterestRate(0.05)
			.taxType(TaxType.NON_TAX.getDescription())
			.taxRate(0.0)
			.build();

		investment = investmentFactory.createBy(request);

		assertNotNull(investment);
		assertInstanceOfInvestment(CompoundFixedInstallmentSaving.class, investment);
	}

	@DisplayName("투자 객체 생성 - FinancialProduct 객체를 이용하여 Investment 객체 생성")
	@ParameterizedTest(name = "{2} 객체 생성 테스트")
	@MethodSource(value = "source.FinancialProductTestDataProvider#provideFinancialProducts")
	void createBy_givenFinancialProduct_whenCreateInvestment_thenReturnInvestment(FinancialProduct product,
		Class<?> expectedType, String ignored) {
		// when
		investment = investmentFactory.createBy(product);
		// then
		assertInstanceOfInvestment(expectedType, investment);
	}

	@DisplayName("현금 투자 객체 생성 - FinancialProduct 객체를 이용하여 CashInvestment 객체 생성")
	@Test
	void createBy_givenCashFinancialProduct_whenCreateInvestment_thenReturnCashInvestment() {
		// given
		FinancialProduct cashProduct = FinancialProductDataProvider.createCashProduct("user1");
		// when
		investment = investmentFactory.createBy(cashProduct);
		// then
		assertInstanceOfInvestment(CashInvestment.class, investment);
	}

	@DisplayName("현금 투자 객체 생성 - 현금의 원금이 최대치인 경우 CashInvestment 객체 생성")
	@Test
	void createBy_givenCashFinancialProductWithMaxAmount_whenCreateInvestment_thenReturnCashInvestment() {
		// given
		BigDecimal amount = new BigDecimal("10000000000000"); // 10조원
		CalculateInvestmentDto dto = CalculateInvestmentDto.builder()
			.type(CASH)
			.amount(new ProductAmount(amount)) // 10조원
			.months(new ProductMonths(0))
			.interestRate(new AnnualInterestRate(0.0))
			.interestType(NONE)
			.taxType(TaxType.NONE)
			.taxRate(new FixedTaxRate(0.0))
			.build();
		// when
		investment = investmentFactory.createBy(dto);
		// then
		assertInstanceOfInvestment(CashInvestment.class, investment);
		Assertions.assertThat(investment.getTotalInvestment()).isEqualByComparingTo(amount);
	}
}
