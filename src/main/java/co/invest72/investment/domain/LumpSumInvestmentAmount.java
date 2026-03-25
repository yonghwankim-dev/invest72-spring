package co.invest72.investment.domain;

import co.invest72.money.domain.Money;

/**
 * 예치 금액 관리하는 인터페이스
 */
public interface LumpSumInvestmentAmount extends InvestmentAmount {
	Money getDepositAmount();

}
