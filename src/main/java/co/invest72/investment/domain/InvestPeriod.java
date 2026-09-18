package co.invest72.investment.domain;

import java.time.LocalDate;

public interface InvestPeriod {
	/**
	 * 투자 기간을 개월수(months)로 계산해서 반환한다.
	 * @return 개월수
	 */
	int getMonths();

	/**
	 * 지정한 시작일로부터 설정된 기간이 경과했을 때의 총 투자 개월수를 계산한다
	 * @param startDate 시작일자
	 * @return 개월수
	 */
	int getMonths(LocalDate startDate);

	/**
	 * 지정한 시작일로부터 설정된 기간이 경과했을 때의 총 투자 일(경과 일수)를 계산한다
	 *
	 * <p><b>계산 규칙 및 주의사항:</b>
	 * <ul>
	 *   <li><b>초일 불산입 (시작일 제외, 만기일 포함):</b> 표준 금융 이자 계산 관례에 따라
	 *       시작일 당일은 제외하고, 만기일 당일까지의 일수를 계산합니다.
	 *       ({@code ChronoUnit.DAYS.between(startDate, endDate)})</li>
	 *   <li><b>가변 일수 산출:</b> 시작월의 날수(28~31일) 및 윤년 여부에 따라
	 *       동일한 1개월이라도 반환되는 일수가 다릅니다.
	 *       (예: 1월 1일 시작 시 31일, 2월 1일 시작 시 28일/29일)</li>
	 *   <li><b>월말 처리 (Last-Day Effect):</b> 시작일이 해당 월의 말일인 경우,
	 *       Java {@link LocalDate#plusMonths(long)} 스펙에 따라 만기일도 해당 월의 말일로 정렬됩니다.
	 *       (예: 1월 31일의 1개월 뒤는 2월 28일/29일)</li>
	 * </ul>
	 * @param startDate
	 * @return
	 */
	int getDays(LocalDate startDate);
}
