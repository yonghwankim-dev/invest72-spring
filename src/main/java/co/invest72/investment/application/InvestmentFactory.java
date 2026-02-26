package co.invest72.investment.application;

import static co.invest72.investment.domain.interest.InterestType.*;
import static co.invest72.investment.domain.investment.InvestmentType.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import co.invest72.financial_product.domain.FinancialProduct;
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

	public InvestmentFactory() {
		registry.put(new InvestmentKey(DEPOSIT, SIMPLE), this::simpleFixedDeposit);
		registry.put(new InvestmentKey(DEPOSIT, COMPOUND), this::compoundFixedDeposit);
		registry.put(new InvestmentKey(SAVINGS, SIMPLE), this::simpleFixedInstallmentSaving);
		registry.put(new InvestmentKey(SAVINGS, COMPOUND), this::compoundFixedInstallmentSaving);
	}

	// TODO: FinancialProduct에서 Investment 생성 지원
	public Investment createBy(FinancialProduct product) {
		throw new UnsupportedOperationException("Creating Investment from FinancialProduct is not supported yet.");
	}

	public Investment createBy(CalculateInvestmentRequest request) {
		InvestmentKey key = createInvestmentKey(request.getType(), request.getInterestType());
		Function<CalculateInvestmentRequest, Investment> creator = registry.get(key);
		if (creator == null) {
			throw new IllegalArgumentException("Unsupported investment type or totalInterest type: " + key);
		}
		return creator.apply(request);
	}

	private InvestmentKey createInvestmentKey(String investmentTypeValue, String interestTypeValue) {
		InvestmentType type = InvestmentType.from(investmentTypeValue);
		InterestType interestType = InterestType.from(interestTypeValue);
		return new InvestmentKey(type, interestType);
	}

	private Investment simpleFixedDeposit(CalculateInvestmentRequest request) {
		PeriodType periodType = PeriodType.from(request.getPeriodType());
		PeriodRange periodRange = createPeriodRange(periodType, request.getPeriodValue());
		InvestPeriod investPeriod = new MonthlyInvestPeriod(periodRange.toMonths());
		LumpSumInvestmentAmount investmentAmount = new FixedDepositAmount(request.getAmount());
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
		TaxableFactory taxableFactory = new KoreanTaxableFactory();
		TaxableResolver taxableResolver = new KoreanStringBasedTaxableResolver(taxableFactory);
		TaxType taxType = TaxType.from(request.getTaxType());
		TaxRate taxRate = new FixedTaxRate(request.getTaxRate());
		return taxableResolver.resolve(taxType, taxRate);
	}

	public record InvestmentKey(InvestmentType investmentType, InterestType interestType) {
	}
}
