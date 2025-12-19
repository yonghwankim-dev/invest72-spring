package co.invest72.investment.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import co.invest72.investment.application.dto.MonthlyInvestmentDetail;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MonthlyCompoundInterest implements Investment {

	private final LumpSumInvestmentAmount initialAmount;
	private final InvestmentAmount monthlyAmount;
	private final InvestPeriod investPeriod;
	private final InterestRate interestRate;
	private final Taxable taxable;
	private final List<MonthlyInvestmentDetail> details;

	@Builder(toBuilder = true)
	public MonthlyCompoundInterest(LumpSumInvestmentAmount initialAmount, InvestmentAmount monthlyAmount,
		InvestPeriod investPeriod, InterestRate interestRate, Taxable taxable) {
		this.initialAmount = initialAmount;
		this.monthlyAmount = monthlyAmount;
		this.investPeriod = investPeriod;
		this.interestRate = interestRate;
		this.taxable = taxable;
		this.details = calculateDetails();
	}

	private List<MonthlyInvestmentDetail> calculateDetails() {
		List<MonthlyInvestmentDetail> result = new ArrayList<>();
		result.add(
			new MonthlyInvestmentDetail(1, initialAmount.getAmount(), BigDecimal.ZERO, initialAmount.getAmount()));

		BigDecimal principal = initialAmount.getAmount();
		for (int i = 2; i <= investPeriod.getMonths(); i++) {
			// 지난달 최종금액 + 매월 적립금
			principal = principal.add(monthlyAmount.getAmount());

			// 이자 계산
			BigDecimal interest = principal.multiply(interestRate.getMonthlyRate());

			// 세금 계산
			BigDecimal tax = BigDecimal.valueOf(
				taxable.applyTax(interest.setScale(0, RoundingMode.HALF_EVEN).intValue()));

			// 수익 계산
			BigDecimal profit = principal.add(interest).subtract(tax);

			log.info("month : {}, principal : {}, totalInterest : {}, totalTax : {}, profit : {}", i,
				principal.setScale(0, RoundingMode.HALF_EVEN).intValue(),
				interest.setScale(0, RoundingMode.HALF_EVEN).intValue(),
				tax.setScale(0, RoundingMode.HALF_EVEN).intValue(),
				profit.setScale(0, RoundingMode.HALF_EVEN).intValue()
			);
			result.add(
				new MonthlyInvestmentDetail(i,
					principal,
					interest,
					profit
				)
			);

			// 다음 달 계산을 위해서 principal 업데이트
			if (i < investPeriod.getMonths()) {
				principal = profit;
			}
		}
		return result;
	}

	@Override
	public int getPrincipal() {
		return getPrincipal(investPeriod.getMonths());
	}

	/**
	 * 시작 금액 + (월 납입액 * (회차 - 1)) 을 계산합니다.
	 * @param month 회차 (두번째 달부터 원금에 가산됨)
	 * @return 해당 회차의 원금
	 */
	@Override
	public int getPrincipal(int month) {
		if (isOutOfRange(month)) {
			throw new IllegalArgumentException("Invalid month: " + month);
		}
		if (month <= 1) {
			return roundToInt.applyAsInt(details.get(0).getPrincipal());
		}
		return roundToInt.applyAsInt(details.get(month - 1).getPrincipal());
	}

	private boolean isOutOfRange(int month) {
		return month > investPeriod.getMonths();
	}

	@Override
	public int getInterest() {
		return getInterest(investPeriod.getMonths());
	}

	@Override
	public int getInterest(int month) {
		if (isOutOfRange(month)) {
			throw new IllegalArgumentException("Invalid month: " + month);
		}
		if (month <= 1) {
			return roundToInt.applyAsInt(details.get(0).getInterest());
		}
		return roundToInt.applyAsInt(details.get(month - 1).getInterest());
	}

	@Override
	public int getTotalInterest() {
		BigDecimal totalInterest = details.stream()
			.map(MonthlyInvestmentDetail::getInterest)
			.reduce(BigDecimal.ZERO, BigDecimal::add);
		return roundToInt.applyAsInt(totalInterest);

	}

	@Override
	public int getProfit() {
		return getProfit(investPeriod.getMonths());
	}

	@Override
	public int getProfit(int month) {
		if (isOutOfRange(month)) {
			throw new IllegalArgumentException("Invalid month: " + month);
		}
		if (month <= 1) {
			return roundToInt.applyAsInt(details.get(0).getProfit());
		}
		return roundToInt.applyAsInt(details.get(month - 1).getProfit());
	}

	@Override
	public int getTotalInvestment() {
		BigDecimal totalInvestment = initialAmount.getAmount()
			.add(monthlyAmount.getAmount().multiply(BigDecimal.valueOf(investPeriod.getMonths() - 1)));
		return roundToInt.applyAsInt(totalInvestment);
	}

	@Override
	public int getTotalPrincipal() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public int getTotalTax() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public int getTotalProfit() {
		throw new UnsupportedOperationException("Not implemented yet");
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
