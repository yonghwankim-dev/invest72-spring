package co.invest72.investment.domain.investment.factory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import co.invest72.investment.domain.InterestRate;
import co.invest72.investment.domain.InvestPeriod;
import co.invest72.investment.domain.InvestmentAmount;
import co.invest72.investment.domain.interest.InterestType;
import co.invest72.investment.domain.investment.InvestmentDetail;
import co.invest72.money.domain.Money;
import lombok.Builder;

@Builder(toBuilder = true)
public class SavingDetailFactory implements InvestmentDetailFactory {
	private final InvestmentAmount investmentAmount;
	private final InterestRate interestRate;
	private final InvestPeriod investPeriod;
	private final InterestType interestType;

	public SavingDetailFactory(InvestmentAmount investmentAmount, InterestRate interestRate, InvestPeriod investPeriod,
		InterestType interestType) {
		this.investmentAmount = Objects.requireNonNull(investmentAmount);
		this.interestRate = Objects.requireNonNull(interestRate);
		this.investPeriod = Objects.requireNonNull(investPeriod);
		this.interestType = Objects.requireNonNull(interestType);
	}

	@Override
	public List<InvestmentDetail> createMonthlyDetails() {
		return generateMonthlyBaseDetails();
	}

	@Override
	public List<InvestmentDetail> createYearlyDetails() {
		return new ArrayList<>();
	}

	private List<InvestmentDetail> generateMonthlyBaseDetails() {
		List<InvestmentDetail> result = new ArrayList<>();
		Money accInvestmentAmount = investmentAmount.getAmount().times(BigDecimal.ZERO);
		Money principal = investmentAmount.getAmount().times(BigDecimal.ZERO);
		Money interest = investmentAmount.getAmount().times(BigDecimal.ZERO);
		Money profit = investmentAmount.getAmount().times(BigDecimal.ZERO);

		result.add(new InvestmentDetail(0, principal, interest, profit));
		for (int i = 1; i <= investPeriod.getMonths(); i++) {
			accInvestmentAmount = accInvestmentAmount.add(investmentAmount.getAmount());
			principal = profit.add(investmentAmount.getAmount());
			interest = interestRate.calMonthlyInterest(accInvestmentAmount);
			profit = principal.add(interest);
			result.add(new InvestmentDetail(i, principal, interest, profit));
		}
		return result;
	}
}
