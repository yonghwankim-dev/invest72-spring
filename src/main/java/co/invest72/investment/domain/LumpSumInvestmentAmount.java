package co.invest72.investment.domain;

import java.math.BigDecimal;

import co.invest72.money.domain.Money;

/**
 * 예치 금액 관리하는 인터페이스
 */
public interface LumpSumInvestmentAmount extends InvestmentAmount {
	BigDecimal getDepositAmount();

	default Money getDepositAmount_temp() {
		return null;
	}

}
