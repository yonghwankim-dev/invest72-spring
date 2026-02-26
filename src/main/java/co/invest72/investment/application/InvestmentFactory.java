package co.invest72.investment.application;

import static co.invest72.investment.domain.interest.InterestType.*;
import static co.invest72.investment.domain.investment.InvestmentType.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import co.invest72.financial_product.domain.FinancialProduct;
import co.invest72.investment.application.dto.CalculateInvestmentDto;
import co.invest72.investment.console.input.parser.InstallmentInvestmentAmountParser;
import co.invest72.investment.console.input.parser.InvestmentAmountParser;
import co.invest72.investment.domain.InstallmentInvestmentAmount;
import co.invest72.investment.domain.InterestRate;
import co.invest72.investment.domain.InvestPeriod;
import co.invest72.investment.domain.Investment;
import co.invest72.investment.domain.LumpSumInvestmentAmount;
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

	private final Map<InvestmentKey, Function<CalculateInvestmentRequest, Investment>> registry = new HashMap<>();
	private final Map<InvestmentKey, Function<FinancialProduct, Investment>> productRegistry = new HashMap<>();
	private final Map<InvestmentKey, Function<CalculateInvestmentDto, Investment>> dtoRegistry = new HashMap<>();

	public InvestmentFactory() {
		registry.put(new InvestmentKey(DEPOSIT, SIMPLE), this::simpleFixedDeposit);
		registry.put(new InvestmentKey(DEPOSIT, COMPOUND), this::compoundFixedDeposit);
		registry.put(new InvestmentKey(SAVINGS, SIMPLE), this::simpleFixedInstallmentSaving);
		registry.put(new InvestmentKey(SAVINGS, COMPOUND), this::compoundFixedInstallmentSaving);

		productRegistry.put(new InvestmentKey(DEPOSIT, SIMPLE), this::simpleFixedDeposit);
		productRegistry.put(new InvestmentKey(DEPOSIT, COMPOUND), this::compoundFixedDeposit);
		productRegistry.put(new InvestmentKey(SAVINGS, SIMPLE), this::simpleFixedInstallmentSaving);
		productRegistry.put(new InvestmentKey(SAVINGS, COMPOUND), this::compoundFixedInstallmentSaving);

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
		InvestmentKey key = createInvestmentKey(request.getType(), request.getInterestType());
		Function<CalculateInvestmentRequest, Investment> creator = registry.get(key);
		if (creator == null) {
			throw new IllegalArgumentException("Unsupported investment type or interest type: " + key);
		}
		return creator.apply(request);
	}

	private InvestmentKey createInvestmentKey(String investmentTypeValue, String interestTypeValue) {
		InvestmentType type = InvestmentType.from(investmentTypeValue);
		InterestType interestType = InterestType.from(interestTypeValue);
		return createInvestmentKey(type, interestType);
	}

	private InvestmentKey createInvestmentKey(InvestmentType investmentType, InterestType interestType) {
		return new InvestmentKey(investmentType, interestType);
	}

	private Investment simpleFixedDeposit(CalculateInvestmentDto dto) {
		return new SimpleFixedDeposit(
			new FixedDepositAmount(dto.getAmount().getValue().intValue()),
			new MonthlyInvestPeriod(dto.getMonths().getValue()),
			dto.getInterestRate(),
			resolveTaxable(dto.getTaxType(), dto.getTaxRate())
		);
	}

	private Investment compoundFixedDeposit(CalculateInvestmentDto dto) {
		return new CompoundFixedDeposit(
			new FixedDepositAmount(dto.getAmount().getValue().intValue()),
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

	private Investment simpleFixedDeposit(FinancialProduct product) {
		return new SimpleFixedDeposit(
			new FixedDepositAmount(product.getAmount().getValue().intValue()),
			new MonthlyInvestPeriod(product.getMonths().getValue()),
			new AnnualInterestRate(product.getInterestRate().getValue().doubleValue()),
			resolveTaxable(product.getTaxType(), new FixedTaxRate(product.getTaxRate().getValue().doubleValue()))
		);
	}

	private CompoundFixedDeposit compoundFixedDeposit(FinancialProduct product) {
		return new CompoundFixedDeposit(
			new FixedDepositAmount(product.getAmount().getValue().intValue()),
			new MonthlyInvestPeriod(product.getMonths().getValue()),
			new AnnualInterestRate(product.getInterestRate().getValue().doubleValue()),
			resolveTaxable(product.getTaxType(), new FixedTaxRate(product.getTaxRate().getValue().doubleValue()))
		);
	}

	private SimpleFixedInstallmentSaving simpleFixedInstallmentSaving(FinancialProduct product) {
		InstallmentInvestmentAmount investmentAmount = new MonthlyInstallmentInvestmentAmount(
			product.getAmount().getValue().intValue());
		return new SimpleFixedInstallmentSaving(
			investmentAmount,
			new MonthlyInvestPeriod(product.getMonths().getValue()),
			new AnnualInterestRate(product.getInterestRate().getValue().doubleValue()),
			resolveTaxable(product.getTaxType(), new FixedTaxRate(product.getTaxRate().getValue().doubleValue()))
		);
	}

	private CompoundFixedInstallmentSaving compoundFixedInstallmentSaving(FinancialProduct product) {
		InstallmentInvestmentAmount investmentAmount = new MonthlyInstallmentInvestmentAmount(
			product.getAmount().getValue().intValue());
		return new CompoundFixedInstallmentSaving(
			investmentAmount,
			new MonthlyInvestPeriod(product.getMonths().getValue()),
			new AnnualInterestRate(product.getInterestRate().getValue().doubleValue()),
			resolveTaxable(product.getTaxType(), new FixedTaxRate(product.getTaxRate().getValue().doubleValue()))
		);
	}

	private Investment simpleFixedDeposit(CalculateInvestmentRequest request) {
		LumpSumInvestmentAmount investmentAmount = new FixedDepositAmount(request.getAmount());
		PeriodType periodType = PeriodType.from(request.getPeriodType());
		PeriodRange periodRange = createPeriodRange(periodType, request.getPeriodValue());
		InvestPeriod investPeriod = periodType.create(periodRange);
		InterestRate interestRate = new AnnualInterestRate(request.getAnnualInterestRate());
		Taxable taxable = resolveTaxable(request);
		return new SimpleFixedDeposit(
			investmentAmount,
			investPeriod,
			interestRate,
			taxable
		);
	}

	private CompoundFixedDeposit compoundFixedDeposit(CalculateInvestmentRequest request) {
		LumpSumInvestmentAmount investmentAmount = new FixedDepositAmount(request.getAmount());
		PeriodType periodType = PeriodType.from(request.getPeriodType());
		PeriodRange periodRange = createPeriodRange(periodType, request.getPeriodValue());
		InvestPeriod investPeriod = periodType.create(periodRange);
		InterestRate interestRate = new AnnualInterestRate(request.getAnnualInterestRate());
		Taxable taxable = resolveTaxable(request);
		return new CompoundFixedDeposit(
			investmentAmount,
			investPeriod,
			interestRate,
			taxable
		);
	}

	private SimpleFixedInstallmentSaving simpleFixedInstallmentSaving(CalculateInvestmentRequest request) {
		InvestmentAmountParser investmentAmountParser = new InstallmentInvestmentAmountParser();
		InstallmentInvestmentAmount investmentAmount = (InstallmentInvestmentAmount)investmentAmountParser.parse(
			request.getAmountType() + " " + request.getAmount());
		PeriodType periodType = PeriodType.from(request.getPeriodType());
		PeriodRange periodRange = createPeriodRange(periodType, request.getPeriodValue());
		InvestPeriod investPeriod = periodType.create(periodRange);
		InterestRate interestRate = new AnnualInterestRate(request.getAnnualInterestRate());
		Taxable taxable = resolveTaxable(request);
		return new SimpleFixedInstallmentSaving(
			investmentAmount,
			investPeriod,
			interestRate,
			taxable
		);
	}

	private CompoundFixedInstallmentSaving compoundFixedInstallmentSaving(CalculateInvestmentRequest request) {
		InvestmentAmountParser investmentAmountParser = new InstallmentInvestmentAmountParser();
		InstallmentInvestmentAmount investmentAmount = (InstallmentInvestmentAmount)investmentAmountParser.parse(
			request.getAmountType() + " " + request.getAmount());
		PeriodType periodType = PeriodType.from(request.getPeriodType());
		PeriodRange periodRange = createPeriodRange(periodType, request.getPeriodValue());
		InvestPeriod investPeriod = periodType.create(periodRange);
		InterestRate interestRate = new AnnualInterestRate(request.getAnnualInterestRate());
		Taxable taxable = resolveTaxable(request);
		return new CompoundFixedInstallmentSaving(
			investmentAmount,
			investPeriod,
			interestRate,
			taxable
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

	private Taxable resolveTaxable(CalculateInvestmentRequest request) {
		TaxType taxType = TaxType.from(request.getTaxType());
		Double taxRate = request.getTaxRate();
		return resolveTaxable(taxType, new FixedTaxRate(taxRate));
	}

	private Taxable resolveTaxable(TaxType taxType, TaxRate taxRate) {
		TaxableFactory taxableFactory = new KoreanTaxableFactory();
		TaxableResolver taxableResolver = new KoreanStringBasedTaxableResolver(taxableFactory);
		return taxableResolver.resolve(taxType, taxRate);
	}

	public record InvestmentKey(InvestmentType investmentType, InterestType interestType) {
	}
}
