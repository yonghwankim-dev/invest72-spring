package co.invest72.investment.domain;

import java.math.BigDecimal;

/**
 * 예치 금액 관리하는 인터페이스
 */
public interface LumpSumInvestmentAmount extends InvestmentAmount {
	BigDecimal getDepositAmount();

}
