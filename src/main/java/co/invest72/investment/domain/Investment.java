package co.invest72.investment.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.UnaryOperator;

import co.invest72.money.domain.Money;

public interface Investment {

	UnaryOperator<BigDecimal> roundToWholeAmount = amount -> amount
		.setScale(0, RoundingMode.HALF_EVEN);

	UnaryOperator<Money> roundToWholeMoney = money ->
		Money.of(roundToWholeAmount.apply(money.getValue()), money.getCurrency());

	Money getPrincipal();

	/**
	 * 지정된 월 회차(month)의 원금 금액을 Money 객체로 반환합니다.
	 * @param month 회차 (기본 1부터 시작)
	 * @return 원금 금액을 Money 객체로 반환
	 */
	Money getPrincipal(int month);

	/**
	 * 만기 시점의 이자 금액을 반환합니다.
	 * @return 이자 금액
	 */
	Money getInterest();

	/**
	 * 지정된 월 회차(month)의 이자 금액을 반환합니다.
	 * @param month 회차 (1부터 시작)
	 * @return 이자 금액
	 */
	Money getInterest(int month);

	/**
	 * 만기 시점의 수익 금액을 반환합니다.
	 * <p>
	 * 해당 금액은 원금 + 이자 - 세금 입니다.
	 * </p>
	 * @return 총 투자 금액
	 */
	Money getProfit();

	/**
	 * 지정된 월 회차(month)의 수익 금액을 반환합니다.
	 * <p>
	 * 해당 금액은 원금 + 이자 - 세금 입니다.
	 * </p>
	 *
	 * @param month 회차 (1부터 시작)
	 * @return 총 투자 금액
	 */
	Money getProfit(int month);

	/**
	 * 만기까지의 총 투자 금액을 반환합니다.
	 * @return 총 투자 금액
	 */
	Money getTotalInvestment();

	/**
	 * 만기까지의 총 이자 금액을 반환합니다.
	 * @return 총 이자 금액
	 */
	Money getTotalInterest();

	/**
	 * 만기까지의 총 세금 금액을 반환합니다.
	 * @return 총 세금 금액
	 */
	Money getTotalTax();

	/**
	 * 만기까지의 총 수익 금액을 반환합니다.
	 * @return 총 수익 금액
	 */
	Money getTotalProfit();

	/**
	 * 투자 기간의 마지막 월을 반환합니다
	 * <p>
	 * 예를 들어 투자 기간이 1년이면 12를 반환한다.
	 * </p>
	 * @return 마지막 월 (기본 1부터 시작)
	 */
	int getFinalMonth();

	String getTaxType();

	/**
	 * 지정된 연도(year)의 원금 금액을 Money 객체로 반환합니다.
	 * @param year 연도 (1부터 시작)
	 * @return 원금 금액을 Money 객체로 반환
	 */
	Money getPrincipalForYear(int year);

	/**
	 * 지정된 연도(year)의 이자 금액을 Money 객체로 반환합니다.
	 * @param year 연도 (1부터 시작)
	 * @return 이자 금액을 Money 객체로 반환
	 */
	Money getInterestForYear(int year);

	/**
	 * 지정된 연도(year)의 수익 금액을 Money 객체로 반환합니다.
	 * @param year 연도 (1부터 시작)
	 * @return 수익 금액을 Money 객체로 반환
	 */
	Money getProfitForYearMoney(int year);

	BigDecimal getTaxRate();
}
