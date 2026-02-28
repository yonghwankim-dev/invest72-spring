package co.invest72.investment.application;

import static co.invest72.investment.domain.interest.InterestType.*;
import static co.invest72.investment.domain.investment.InvestmentType.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import co.invest72.financial_product.domain.FinancialProduct;
import co.invest72.financial_product.domain.ProductAmount;
import co.invest72.financial_product.domain.ProductMonths;
import co.invest72.investment.application.dto.CalculateInvestmentDto;
import co.invest72.investment.console.input.parser.InstallmentInvestmentAmountParser;
import co.invest72.investment.console.input.parser.InvestmentAmountParser;
import co.invest72.investment.domain.CashInvestment;
import co.invest72.investment.domain.InstallmentInvestmentAmount;
import co.invest72.investment.domain.InvestPeriod;
import co.invest72.investment.domain.Investment;
import co.invest72.investment.domain.InvestmentAmount;
import co.invest72.investment.domain.PeriodRange;
import co.invest72.investment.domain.TaxRate;
import co.invest72.investment.domain.Taxable;
import co.invest72.investment.domain.TaxableFactory;
import co.invest72.investment.domain.TaxableResolver;
import co.invest72.investment.domain.amount.FixedDepositAmount;
import co.invest72.investment.domain.amount.MonthlyInstallmentInvestmentAmount;
import co.invest72.investment.domain.interest.AnnualInterestRate;
import co.invest72.investment.domain.interest.InterestType;
import co.invest72.investment.domain.investment.CompoundFixedDeposit;
import co.invest72.investment.domain.investment.CompoundFixedInstallmentSaving;
import co.invest72.investment.domain.investment.InvestmentType;
import co.invest72.investment.domain.investment.SimpleFixedDeposit;
import co.invest72.investment.domain.investment.SimpleFixedInstallmentSaving;
import co.invest72.investment.domain.period.MonthlyInvestPeriod;
import co.invest72.investment.domain.period.PeriodMonthsRange;
import co.invest72.investment.domain.period.PeriodType;
import co.invest72.investment.domain.period.PeriodYearRange;
import co.invest72.investment.domain.tax.FixedTaxRate;
import co.invest72.investment.domain.tax.KoreanTaxableFactory;
import co.invest72.investment.domain.tax.TaxType;
import co.invest72.investment.domain.tax.resolver.KoreanStringBasedTaxableResolver;
import co.invest72.investment.presentation.request.CalculateInvestmentRequest;

public class InvestmentFactory {

	private final Map<InvestmentKey, Function<CalculateInvestmentDto, Investment>> dtoRegistry = new HashMap<>();

	public InvestmentFactory() {
		dtoRegistry.put(new InvestmentKey(CASH, NONE), this::cashInvestment);
		dtoRegistry.put(new InvestmentKey(DEPOSIT, SIMPLE), this::simpleFixedDeposit);
		dtoRegistry.put(new InvestmentKey(DEPOSIT, COMPOUND), this::compoundFixedDeposit);
		dtoRegistry.put(new InvestmentKey(SAVINGS, SIMPLE), this::simpleFixedInstallmentSaving);
		dtoRegistry.put(new InvestmentKey(SAVINGS, COMPOUND), this::compoundFixedInstallmentSaving);
	}

	public Investment createBy(CalculateInvestmentDto dto) {
		InvestmentKey key = createInvestmentKey(dto.getType(), dto.getInterestType());
		Function<CalculateInvestmentDto, Investment> creator = dtoRegistry.get(key);
		if (creator == null) {
			throw new IllegalArgumentException("Unsupported investment type or interest type: " + key);
		}
		return creator.apply(dto);
	}

	public Investment createBy(FinancialProduct product) {
		CalculateInvestmentDto dto = CalculateInvestmentDto.builder()
			.type(product.getInvestmentType())
			.amount(product.getAmount())
			.months(product.getMonths())
			.interestRate(new AnnualInterestRate(product.getInterestRate().getValue().doubleValue()))
			.interestType(product.getInterestType())
			.taxType(product.getTaxType())
			.taxRate(new FixedTaxRate(product.getTaxRate().getValue().doubleValue()))
			.build();
		return createBy(dto);
	}

	public Investment createBy(CalculateInvestmentRequest request) {

		InvestmentType investmentType = InvestmentType.from(request.getType());
		PeriodType periodType = PeriodType.from(request.getPeriodType());
		PeriodRange periodRange = createPeriodRange(periodType, request.getPeriodValue());
		InvestPeriod investPeriod = periodType.create(periodRange);

		// ProductAmount는 일시금이거나 월투자금액으로 고정됨
		ProductAmount productAmount = new ProductAmount(BigDecimal.valueOf(request.getAmount()));
		// InvestmentType 적금인 경우에 월 투자금액으로 저장되도록 처리
		if (investmentType == SAVINGS) {
			InvestmentAmountParser investmentAmountParser = new InstallmentInvestmentAmountParser();
			InstallmentInvestmentAmount investmentAmount = (InstallmentInvestmentAmount)investmentAmountParser.parse(
				request.getAmountType() + " " + request.getAmount());
			productAmount = new ProductAmount(investmentAmount.getMonthlyAmount());
		}

		CalculateInvestmentDto dto = CalculateInvestmentDto.builder()
			.type(investmentType)
			.amount(productAmount)
			.months(new ProductMonths(investPeriod.getMonths()))
			.interestRate(new AnnualInterestRate(request.getAnnualInterestRate()))
			.interestType(InterestType.from(request.getInterestType()))
			.taxType(TaxType.from(request.getTaxType()))
			.taxRate(new FixedTaxRate(request.getTaxRate()))
			.build();
		return createBy(dto);
	}

	private InvestmentKey createInvestmentKey(InvestmentType investmentType, InterestType interestType) {
		return new InvestmentKey(investmentType, interestType);
	}

	private Investment cashInvestment(CalculateInvestmentDto dto) {
		InvestmentAmount investmentAmount = new FixedDepositAmount(dto.getAmount().getValue());
		return new CashInvestment(investmentAmount);
	}

	private Investment simpleFixedDeposit(CalculateInvestmentDto dto) {
		return new SimpleFixedDeposit(
			new FixedDepositAmount(dto.getAmount().getValue()),
			new MonthlyInvestPeriod(dto.getMonths().getValue()),
			dto.getInterestRate(),
			resolveTaxable(dto.getTaxType(), dto.getTaxRate())
		);
	}

	private Investment compoundFixedDeposit(CalculateInvestmentDto dto) {
		return new CompoundFixedDeposit(
			new FixedDepositAmount(dto.getAmount().getValue()),
			new MonthlyInvestPeriod(dto.getMonths().getValue()),
			dto.getInterestRate(),
			resolveTaxable(dto.getTaxType(), dto.getTaxRate())
		);
	}

	private Investment simpleFixedInstallmentSaving(CalculateInvestmentDto dto) {
		InstallmentInvestmentAmount investmentAmount = new MonthlyInstallmentInvestmentAmount(
			dto.getAmount().getValue().intValue());
		return new SimpleFixedInstallmentSaving(
			investmentAmount,
			new MonthlyInvestPeriod(dto.getMonths().getValue()),
			dto.getInterestRate(),
			resolveTaxable(dto.getTaxType(), dto.getTaxRate())
		);
	}

	private Investment compoundFixedInstallmentSaving(CalculateInvestmentDto dto) {
		InstallmentInvestmentAmount investmentAmount = new MonthlyInstallmentInvestmentAmount(
			dto.getAmount().getValue().intValue());
		return new CompoundFixedInstallmentSaving(
			investmentAmount,
			new MonthlyInvestPeriod(dto.getMonths().getValue()),
			dto.getInterestRate(),
			resolveTaxable(dto.getTaxType(), dto.getTaxRate())
		);
	}

	private PeriodRange createPeriodRange(PeriodType periodType, int periodValue) {
		PeriodRange periodRange;
		if (periodType == PeriodType.MONTH) {
			periodRange = new PeriodMonthsRange(periodValue);
		} else {
			periodRange = new PeriodYearRange(periodValue);
		}
		return periodRange;
	}

	private Taxable resolveTaxable(TaxType taxType, TaxRate taxRate) {
		TaxableFactory taxableFactory = new KoreanTaxableFactory();
		TaxableResolver taxableResolver = new KoreanStringBasedTaxableResolver(taxableFactory);
		return taxableResolver.resolve(taxType, taxRate);
	}

	public record InvestmentKey(InvestmentType investmentType, InterestType interestType) {
	}
}
