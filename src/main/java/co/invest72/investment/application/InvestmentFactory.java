package co.invest72.investment.application;

import static co.invest72.investment.domain.interest.InterestType.*;
import static co.invest72.investment.domain.investment.InvestmentType.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import co.invest72.exchange_rate.domain.entity.ExchangeRate;
import co.invest72.exchange_rate.domain.service.ExchangeRateService;
import co.invest72.financial_product.domain.FinancialProduct;
import co.invest72.financial_product.domain.ProductAmount;
import co.invest72.financial_product.domain.ProductMonths;
import co.invest72.financial_product.infrastructure.mapper.ProductAmountMapper;
import co.invest72.investment.application.dto.CalculateInvestmentDto;
import co.invest72.investment.console.input.parser.InstallmentInvestmentAmountParser;
import co.invest72.investment.console.input.parser.InvestmentAmountParser;
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
import co.invest72.investment.domain.investment.CashInvestment;
import co.invest72.investment.domain.investment.FixedDeposit;
import co.invest72.investment.domain.investment.FixedSaving;
import co.invest72.investment.domain.investment.InvestmentType;
import co.invest72.investment.domain.period.MonthlyInvestPeriod;
import co.invest72.investment.domain.period.PeriodMonthsRange;
import co.invest72.investment.domain.period.PeriodType;
import co.invest72.investment.domain.period.PeriodYearRange;
import co.invest72.investment.domain.tax.FixedTaxRate;
import co.invest72.investment.domain.tax.KoreanTaxableFactory;
import co.invest72.investment.domain.tax.TaxType;
import co.invest72.investment.domain.tax.resolver.KoreanStringBasedTaxableResolver;
import co.invest72.investment.presentation.request.CalculateInvestmentRequest;
import co.invest72.money.domain.Currency;
import co.invest72.money.domain.Money;

public class InvestmentFactory {

	private final Map<InvestmentKey, Function<CalculateInvestmentDto, Investment>> dtoRegistry = new HashMap<>();
	private final ProductAmountMapper productAmountMapper;
	private final ExchangeRateService exchangeRateService;

	public InvestmentFactory(ProductAmountMapper productAmountMapper, ExchangeRateService exchangeRateService) {
		dtoRegistry.put(new InvestmentKey(CASH, NONE), this::cash);
		dtoRegistry.put(new InvestmentKey(DEPOSIT, SIMPLE), this::deposit);
		dtoRegistry.put(new InvestmentKey(DEPOSIT, COMPOUND), this::deposit);
		dtoRegistry.put(new InvestmentKey(SAVINGS, SIMPLE), this::savings);
		dtoRegistry.put(new InvestmentKey(SAVINGS, COMPOUND), this::savings);
		this.productAmountMapper = productAmountMapper;
		this.exchangeRateService = exchangeRateService;
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
			.type(InvestmentType.valueOf(product.getProductInvestmentType().getName()))
			.amount(product.getAmount())
			.months(product.getMonths())
			.interestRate(new AnnualInterestRate(product.getProductAnnualInterestRate().getValue()))
			.interestType(InterestType.valueOf(product.getProductInterestType().getName()))
			.taxType(TaxType.valueOf(product.getProductTaxType().getName()))
			.taxRate(new FixedTaxRate(product.getProductTaxRate().getValue()))
			.currency(product.getAmount().getCurrency())
			.build();
		return createBy(dto);
	}

	public Investment createBy(CalculateInvestmentRequest request) {
		InvestmentType investmentType = InvestmentType.valueOf(request.getType());
		PeriodType periodType = PeriodType.from(request.getPeriodType());
		PeriodRange periodRange = createPeriodRange(periodType, request.getPeriodValue());
		InvestPeriod investPeriod = periodType.create(periodRange);
		Currency currency = Currency.of(request.getCurrencyCode(), request.getCurrencyName());

		// ProductAmount는 일시금이거나 월투자금액으로 고정됨
		Money amount = Money.of(BigDecimal.valueOf(request.getAmount()), currency);
		ProductAmount productAmount = ProductAmount.from(amount);
		// InvestmentType 적금인 경우에 월 투자금액으로 저장되도록 처리
		if (investmentType == SAVINGS) {
			InvestmentAmountParser investmentAmountParser = new InstallmentInvestmentAmountParser();
			InstallmentInvestmentAmount investmentAmount = (InstallmentInvestmentAmount)investmentAmountParser.parse(
				request.getAmountType() + " " + request.getAmount());
			productAmount = productAmountMapper.toProductAmount(investmentAmount.getMonthlyAmount());
		}

		CalculateInvestmentDto dto = CalculateInvestmentDto.builder()
			.type(investmentType)
			.amount(productAmount)
			.months(new ProductMonths(investPeriod.getMonths()))
			.interestRate(new AnnualInterestRate(request.getAnnualInterestRate()))
			.interestType(InterestType.valueOf(request.getInterestType()))
			.taxType(TaxType.valueOf(request.getTaxType()))
			.taxRate(new FixedTaxRate(request.getTaxRate()))
			.currency(request.getCurrencyCode())
			.build();
		return createBy(dto);
	}

	private InvestmentKey createInvestmentKey(InvestmentType investmentType, InterestType interestType) {
		return new InvestmentKey(investmentType, interestType);
	}

	private Investment cash(CalculateInvestmentDto dto) {
		InvestmentAmount investmentAmount = new FixedDepositAmount(dto.getAmount().getValue(), dto.getCurrency());
		return new CashInvestment(investmentAmount);
	}

	private Investment deposit(CalculateInvestmentDto dto) {
		return FixedDeposit.builder()
			.investmentAmount(new FixedDepositAmount(dto.getAmount().getValue(), dto.getCurrency()))
			.investPeriod(new MonthlyInvestPeriod(dto.getMonths().getValue()))
			.interestRate(dto.getInterestRate())
			.interestType(dto.getInterestType())
			.taxable(resolveTaxable(dto.getTaxType(), dto.getTaxRate()))
			.build();
	}

	private Investment savings(CalculateInvestmentDto dto) {
		ExchangeRate exchangeRate = exchangeRateService.findExchangeRate(dto.getCurrency());
		Currency currency = Currency.of(exchangeRate.getCurrencyCode(), exchangeRate.getCurrencyName());
		return FixedSaving.builder()
			.investmentAmount(new MonthlyInstallmentInvestmentAmount(Money.of(
				dto.getAmount().getValue(), currency)))
			.investPeriod(new MonthlyInvestPeriod(dto.getMonths().getValue()))
			.interestRate(dto.getInterestRate())
			.interestType(dto.getInterestType())
			.taxable(resolveTaxable(dto.getTaxType(), dto.getTaxRate()))
			.build();
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
