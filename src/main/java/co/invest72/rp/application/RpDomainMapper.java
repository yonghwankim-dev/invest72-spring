package co.invest72.rp.application;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import co.invest72.investment.domain.DailyInvestPeriod;
import co.invest72.investment.domain.InterestRate;
import co.invest72.investment.domain.InvestPeriod;
import co.invest72.investment.domain.InvestmentAmount;
import co.invest72.investment.domain.amount.FixedDepositAmount;
import co.invest72.investment.domain.interest.AnnualInterestRate;
import co.invest72.money.domain.Currency;
import co.invest72.rp.domain.RepurchaseAgreement;
import co.invest72.rp.domain.TermRepurchaseAgreement;
import co.invest72.rp.entity.RepurchaseAgreementEntity;

@Component
public class RpDomainMapper {
	public RepurchaseAgreement toDomain(RepurchaseAgreementEntity entity) {
		InvestmentAmount investmentAmount = getInvestmentAmount(entity);
		InterestRate interestRate = new AnnualInterestRate(entity.getProductAnnualInterestRate().getValue());
		LocalDate startDate = entity.getStartDate();
		InvestPeriod investPeriod = new DailyInvestPeriod(startDate, entity.getDays());
		return TermRepurchaseAgreement.builder()
			.investmentAmount(investmentAmount)
			.interestRate(interestRate)
			.startDate(startDate)
			.investPeriod(investPeriod)
			.build();
	}

	private InvestmentAmount getInvestmentAmount(RepurchaseAgreementEntity entity) {
		String currencyCode = entity.getAmount().getCurrencyCode();
		String currencyName = entity.getAmount().getExchangeRate().getCurrencyName();
		Currency currency = Currency.of(currencyCode, currencyName);
		return new FixedDepositAmount(entity.getAmount().getValue(), currency);
	}
}
