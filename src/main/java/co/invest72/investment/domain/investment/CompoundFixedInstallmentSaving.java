package co.invest72.investment.domain.investment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import co.invest72.investment.application.dto.MonthlyInvestmentDetail;
import co.invest72.investment.domain.InstallmentInvestmentAmount;
import co.invest72.investment.domain.InterestRate;
import co.invest72.investment.domain.InvestPeriod;
import co.invest72.investment.domain.Investment;
import co.invest72.investment.domain.Taxable;

public class CompoundFixedInstallmentSaving implements Investment {

	private final InstallmentInvestmentAmount investmentAmount;
	private final InvestPeriod investPeriod;
	private final InterestRate interestRate;
	private final Taxable taxable;
	private final List<MonthlyInvestmentDetail> details;

	public CompoundFixedInstallmentSaving(InstallmentInvestmentAmount investmentAmount, InvestPeriod investPeriod,
		InterestRate interestRate, Taxable taxable) {
		this.investmentAmount = investmentAmount;
		this.investPeriod = investPeriod;
		this.interestRate = interestRate;
		this.taxable = taxable;
		this.details = calculateDetails();
	}

	private List<MonthlyInvestmentDetail> calculateDetails() {
		List<MonthlyInvestmentDetail> result = new ArrayList<>();
		BigDecimal principal = BigDecimal.ZERO;
		BigDecimal interest = BigDecimal.ZERO;
		BigDecimal tax = BigDecimal.ZERO;
		BigDecimal profit = BigDecimal.ZERO;
		// 0 월
		result.add(new MonthlyInvestmentDetail(0, principal, interest, profit));

		for (int i = 1; i <= getFinalMonth(); i++) {
			// 월 적립금액 누적
			principal = principal.add(investmentAmount.getAmount());

			// 월 이자 계산
			interest = interestRate.getMonthlyRate().multiply(principal);
			
			profit = principal.add(interest);

			result.add(new MonthlyInvestmentDetail(i, principal, interest, profit));

			principal = profit; // 이자를 포함한 원금이 다음 달의 원금이 됨
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
			.skip(1)
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
		BigDecimal totalTax = BigDecimal.valueOf(getTotalTax());
		BigDecimal totalProfit = details.get(getFinalMonth()).getProfit().subtract(totalTax);
		return roundToInt.applyAsInt(totalProfit);
	}

	@Override
	public int getFinalMonth() {
		return investPeriod.getMonths();
	}

	@Override
	public String getTaxType() {
		return taxable.getTaxType();
	}
}
