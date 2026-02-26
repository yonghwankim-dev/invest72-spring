package co.invest72.investment.application;

import static co.invest72.investment.domain.amount.AmountType.*;
import static co.invest72.investment.domain.interest.InterestType.*;
import static co.invest72.investment.domain.investment.InvestmentType.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import co.invest72.financial_product.domain.FinancialProduct;
import co.invest72.financial_product.domain.ProductAmount;
import co.invest72.financial_product.domain.ProductMonths;
import co.invest72.financial_product.domain.ProductRate;
import co.invest72.investment.domain.Investment;
import co.invest72.investment.domain.investment.CompoundFixedDeposit;
import co.invest72.investment.domain.investment.CompoundFixedInstallmentSaving;
import co.invest72.investment.domain.investment.InvestmentType;
import co.invest72.investment.domain.investment.SimpleFixedDeposit;
import co.invest72.investment.domain.investment.SimpleFixedInstallmentSaving;
import co.invest72.investment.domain.tax.TaxType;
import co.invest72.investment.presentation.request.CalculateInvestmentRequest;

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

	@DisplayName("투자 객체 생성 - 단리-예금 객체 생성")
	@Test
	void createBy_whenSimpleDepositFinancialProduct_thenReturnInvestment() {
		// given
		FinancialProduct product = FinancialProduct.builder()
			.userId("user-" + UUID.randomUUID())
			.name("단리-예금")
			.investmentType(InvestmentType.DEPOSIT)
			.amount(new ProductAmount(BigDecimal.valueOf(1_000_000)))
			.months(new ProductMonths(12))
			.interestRate(new ProductRate(BigDecimal.valueOf(0.05)))
			.interestType(SIMPLE)
			.taxType(TaxType.NON_TAX)
			.taxRate(new ProductRate(BigDecimal.ZERO))
			.startDate(LocalDate.now())
			.createdAt(LocalDateTime.now())
			.build();
		// when
		investment = investmentFactory.createBy(product);
		// then
		assertInstanceOfInvestment(SimpleFixedDeposit.class, investment);
	}

	@DisplayName("투자 객체 생성 - 복리-예금 객체 생성")
	@Test
	void createBy_whenCompoundDepositFinancialProduct_thenReturnInvestment() {
		// given
		FinancialProduct product = FinancialProduct.builder()
			.userId("user-" + UUID.randomUUID())
			.name("복리-예금")
			.investmentType(InvestmentType.DEPOSIT)
			.amount(new ProductAmount(BigDecimal.valueOf(1_000_000)))
			.months(new ProductMonths(12))
			.interestRate(new ProductRate(BigDecimal.valueOf(0.05)))
			.interestType(COMPOUND)
			.taxType(TaxType.NON_TAX)
			.taxRate(new ProductRate(BigDecimal.ZERO))
			.startDate(LocalDate.now())
			.createdAt(LocalDateTime.now())
			.build();
		// when
		investment = investmentFactory.createBy(product);
		// then
		assertInstanceOfInvestment(CompoundFixedDeposit.class, investment);
	}

	@DisplayName("투자 객체 생성 - 단리-적금 객체 생성")
	@Test
	void createBy_whenSimpleInstallmentSavingFinancialProduct_thenReturnInvestment() {
		// given
		FinancialProduct product = FinancialProduct.builder()
			.userId("user-" + UUID.randomUUID())
			.name("단리-적금")
			.investmentType(InvestmentType.SAVINGS)
			.amount(new ProductAmount(BigDecimal.valueOf(1_000_000)))
			.months(new ProductMonths(12))
			.interestRate(new ProductRate(BigDecimal.valueOf(0.05)))
			.interestType(SIMPLE)
			.taxType(TaxType.NON_TAX)
			.taxRate(new ProductRate(BigDecimal.ZERO))
			.startDate(LocalDate.now())
			.createdAt(LocalDateTime.now())
			.build();
		// when
		investment = investmentFactory.createBy(product);
		// then
		assertInstanceOfInvestment(SimpleFixedInstallmentSaving.class, investment);
	}

	@DisplayName("투자 객체 생성 - 복리-적금 객체 생성")
	@Test
	void createBy_whenCompoundInstallmentSavingFinancialProduct_thenReturnInvestment() {
		// given
		FinancialProduct product = FinancialProduct.builder()
			.userId("user-" + UUID.randomUUID())
			.name("복리-적금")
			.investmentType(InvestmentType.SAVINGS)
			.amount(new ProductAmount(BigDecimal.valueOf(1_000_000)))
			.months(new ProductMonths(12))
			.interestRate(new ProductRate(BigDecimal.valueOf(0.05)))
			.interestType(COMPOUND)
			.taxType(TaxType.NON_TAX)
			.taxRate(new ProductRate(BigDecimal.ZERO))
			.startDate(LocalDate.now())
			.createdAt(LocalDateTime.now())
			.build();
		// when
		investment = investmentFactory.createBy(product);
		// then
		assertInstanceOfInvestment(CompoundFixedInstallmentSaving.class, investment);
	}
}
