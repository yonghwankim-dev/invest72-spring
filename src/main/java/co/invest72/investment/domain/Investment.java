package co.invest72.investment.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.ToIntFunction;

public interface Investment {

	ToIntFunction<BigDecimal> roundToInt = amount -> amount
		.setScale(0, RoundingMode.HALF_EVEN)
		.intValueExact();

	/**
	 * 만기 시점의 원금 금액을 반환합니다.
	 *
	 * @return 원금 금액
	 */
	int getPrincipal();

	/**
	 * 지정된 월 회차(month)의 원금 금액을 반환합니다.
	 *
	 * @param month 회차 (기본 1부터 시작)
	 * @return 원금 금액
	 */
	int getPrincipal(int month);

	/**
	 * 만기 시점의 이자 금액을 반환합니다.
	 * <p>
	 * 해당 금액은 세전 이자 금액입니다.
	 * </p>
	 * @return 이자 금액
	 */
	int getInterest();

	/**
	 * 지정된 월 회차(month)의 이자 금액을 반환합니다.
	 * <p>
	 * 해당 금액은 세전 이자 금액입니다.
	 * </p>
	 * @param month 회차 (기본 1부터 시작)
	 * @return 이자 금액
	 */
	int getInterest(int month);

	/**
	 * 만기 시점의 누적 이자 금액을 반환합니다.
	 * @return 누적 이자 금액
	 */
	default int getAccInterest() {
		return getAccInterest(getFinalMonth());
	}

	/**
	 * 지정된 월 회차(month)까지의 누적 이자 금액을 반환합니다.
	 * @param month 회차 (기본 1부터 시작)
	 * @return 누적 이자 금액
	 */
	default int getAccInterest(int month) {
		throw new UnsupportedOperationException("implement not yeted");
	}

	/**
	 * 만기 시점의 수익 금액을 반환합니다.
	 * <p>
	 * 해당 금액은 원금 + 이자 - 세금 입니다.
	 * </p>
	 * @return 총 투자 금액
	 */
	int getProfit();

	/**
	 * 지정된 월 회차(month)의 수익 금액을 반환합니다.
	 * <p>
	 * 해당 금액은 원금 + 이자 - 세금 입니다.
	 * </p>
	 *
	 * @param month 회차 (1부터 시작)
	 * @return 총 투자 금액
	 */
	int getProfit(int month);

	/**
	 * 만기까지의 총 투자 금액을 반환합니다.
	 * @return 총 투자 금액
	 */
	int getTotalInvestment();

	/**
	 * 만기까지의 총 원금 금액을 반환합니다.
	 * @return 총 원금 금액
	 */
	int getTotalPrincipal();

	/**
	 * 만기까지의 총 이자 금액을 반환합니다.
	 * @return 총 이자 금액
	 */
	int getTotalInterest();

	/**
	 * 만기까지의 총 세금 금액을 반환합니다.
	 * @return 총 세금 금액
	 */
	int getTotalTax();

	/**
	 * 만기까지의 총 수익 금액을 반환합니다.
	 * @return 총 수익 금액
	 */
	int getTotalProfit();

	/**
	 * 투자 기간의 마지막 월을 반환합니다
	 * <p>
	 * 예를 들어 투자 기간이 1년이면 12를 반환한다.
	 * </p>
	 * @return 마지막 월 (기본 1부터 시작)
	 */
	int getFinalMonth();

	String getTaxType();

	default int getPrincipalForYear(int year) {
		throw new UnsupportedOperationException("implement not yeted");
	}
}
