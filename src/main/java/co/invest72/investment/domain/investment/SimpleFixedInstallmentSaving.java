package co.invest72.investment.domain.investment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import co.invest72.investment.domain.InstallmentInvestmentAmount;
import co.invest72.investment.domain.InterestRate;
import co.invest72.investment.domain.InvestPeriod;
import co.invest72.investment.domain.Investment;
import co.invest72.investment.domain.Taxable;
import co.invest72.investment.domain.interest.InterestType;

/**
 * 정기적금
 * 이자 계산 방식은 단리 방식으로, 매월 납입하는 금액에 대해 이자를 계산합니다.
 */
public class SimpleFixedInstallmentSaving implements Investment {

	private final InstallmentInvestmentAmount investmentAmount;
	private final InvestPeriod investPeriod;
	private final InterestRate interestRate;
	private final Taxable taxable;
	private final List<MonthlyInvestmentDetail> details;
	private final List<YearlyInvestmentDetail> yearlyDetails;

	public SimpleFixedInstallmentSaving(InstallmentInvestmentAmount investmentAmount, InvestPeriod investPeriod,
		InterestRate interestRate, Taxable taxable) {
		this.investmentAmount = investmentAmount;
		this.investPeriod = investPeriod;
		this.interestRate = interestRate;
		this.taxable = taxable;
		InstallmentSavingDetailFactory factory = new InstallmentSavingDetailFactory(investmentAmount, interestRate,
			investPeriod);
		this.details = factory.createMonthlyDetails(InterestType.SIMPLE);
		this.yearlyDetails = calculateYearlyDetails();
	}

	private List<MonthlyInvestmentDetail> calculateDetails() {
		List<MonthlyInvestmentDetail> result = new ArrayList<>();
		BigDecimal principal = BigDecimal.ZERO;
		BigDecimal interest = BigDecimal.ZERO;
		BigDecimal profit = BigDecimal.ZERO;
		// 0 월
		result.add(new MonthlyInvestmentDetail(0, principal, interest, profit));

		for (int i = 1; i <= getFinalMonth(); i++) {
			// 월 적립금액 누적
			principal = profit.add(investmentAmount.getAmount());

			// 월 이자 계산
			interest = interestRate.getMonthlyRate().multiply(principal);

			profit = principal.add(interest);

			result.add(new MonthlyInvestmentDetail(i, principal, interest, profit));
		}
		return result;
	}

	private List<YearlyInvestmentDetail> calculateYearlyDetails() {
		List<YearlyInvestmentDetail> result = new ArrayList<>();
		BigDecimal principal = BigDecimal.ZERO;
		BigDecimal interest = BigDecimal.ZERO;
		BigDecimal profit = BigDecimal.ZERO;
		// 0 월
		result.add(new YearlyInvestmentDetail(0, principal, interest, profit));
		int finalMonth = getFinalMonth();
		BigDecimal accInterest = BigDecimal.ZERO;
		for (int i = 1; i <= finalMonth; i++) {
			principal = principal.add(investmentAmount.getAmount());
			interest = interestRate.getMonthlyRate().multiply(principal);
			profit = principal.add(interest);

			accInterest = accInterest.add(interest);
			if (i % 12 == 0) {
				BigDecimal yearlyProfit = principal.add(accInterest);
				result.add(new YearlyInvestmentDetail(i / 12, principal, accInterest, yearlyProfit));
			}
		}
		return result;
	}

	@Override
	public int getPrincipal() {
		return getPrincipal(getFinalMonth());
	}

	@Override
	public int getPrincipal(int month) {
		if (month > getFinalMonth()) {
			return getPrincipal();
		}
		if (month < 0) {
			return getPrincipal(0);
		}
		return roundToInt.applyAsInt(details.get(month).getPrincipal());
	}

	@Override
	public int getInterest() {
		return getInterest(getFinalMonth());
	}

	@Override
	public int getInterest(int month) {
		if (month > getFinalMonth()) {
			return getInterest();
		}
		if (month < 0) {
			return getInterest(0);
		}
		return roundToInt.applyAsInt(details.get(month).getInterest());
	}

	@Override
	public int getProfit() {
		return getProfit(getFinalMonth());
	}

	@Override
	public int getProfit(int month) {
		if (month > getFinalMonth()) {
			return getProfit();
		}
		if (month < 0) {
			return getProfit(0);
		}
		return roundToInt.applyAsInt(details.get(month).getProfit());
	}

	@Override
	public int getTotalInvestment() {
		BigDecimal totalInvestment = investmentAmount.getAmount()
			.multiply(BigDecimal.valueOf(investPeriod.getMonths()));
		return roundToInt.applyAsInt(totalInvestment);
	}

	@Override
	public int getTotalPrincipal() {
		return roundToInt.applyAsInt(details.get(getFinalMonth()).getPrincipal());
	}

	@Override
	public int getTotalInterest() {
		BigDecimal totalInterest = details.stream()
			.skip(1) // 0월은 이자가 없음
			.map(MonthlyInvestmentDetail::getInterest)
			.reduce(BigDecimal.ZERO, BigDecimal::add);
		return roundToInt.applyAsInt(totalInterest);
	}

	@Override
	public int getTotalTax() {
		return taxable.applyTax(getTotalInterest());
	}

	@Override
	public int getTotalProfit() {
		return getTotalPrincipal() + getTotalInterest() - getTotalTax();
	}

	@Override
	public int getFinalMonth() {
		return investPeriod.getMonths();
	}

	@Override
	public String getTaxType() {
		return taxable.getTaxType();
	}

	@Override
	public int getPrincipalForYear(int year) {
		int finalYear = getFinalYear();
		if (year > finalYear) {
			return getPrincipalForYear(finalYear);
		}
		if (year < 0) {
			return getPrincipalForYear(0);
		}
		return roundToInt.applyAsInt(yearlyDetails.get(year).getPrincipal());
	}

	private int getFinalYear() {
		return (getFinalMonth() - 1) / 12 + 1;
	}

	@Override
	public int getInterestForYear(int year) {
		int finalYear = getFinalYear();
		if (year > finalYear) {
			return getInterestForYear(finalYear);
		}
		if (year < 0) {
			return getInterestForYear(0);
		}
		return roundToInt.applyAsInt(yearlyDetails.get(year).getInterest());
	}

	@Override
	public int getProfitForYear(int year) {
		int finalYear = getFinalYear();
		if (year > finalYear) {
			return getProfitForYear(finalYear);
		}
		if (year < 0) {
			return getProfitForYear(0);
		}
		return roundToInt.applyAsInt(yearlyDetails.get(year).getProfit());
	}
}
