package co.invest72.investment.console.input.parser;

import java.math.BigDecimal;

import co.invest72.investment.domain.InvestmentAmount;
import co.invest72.investment.domain.amount.AmountType;
import co.invest72.investment.domain.amount.MonthlyInstallmentInvestmentAmount;
import co.invest72.investment.domain.amount.YearlyInstallmentInvestmentAmount;
import co.invest72.money.domain.Money;

public class InstallmentInvestmentAmountParser implements InvestmentAmountParser {

	@Override
	public InvestmentAmount parse(String line) {
		String[] parts = line.split(" ");
		if (parts.length != 2) {
			throw new IllegalArgumentException("투자 기간 단위와 금액을 올바르게 입력해주세요.");
		}
		String periodType = parts[0];
		if (!periodType.equals(AmountType.MONTHLY.getDescription()) && !periodType.equals(
			AmountType.YEARLY.getDescription())) {
			throw new IllegalArgumentException("투자 기간 단위는 'MONTHLY' 또는 'YEARLY'이어야 합니다.");
		} else if (periodType.equals(AmountType.MONTHLY.getDescription())) {
			int amount = Integer.parseInt(parts[1]);
			return new MonthlyInstallmentInvestmentAmount(Money.of(BigDecimal.valueOf(amount), "KRW"));
		} else {
			int amount = Integer.parseInt(parts[1]);
			return new YearlyInstallmentInvestmentAmount(Money.won(BigDecimal.valueOf(amount)));
		}
	}
}
