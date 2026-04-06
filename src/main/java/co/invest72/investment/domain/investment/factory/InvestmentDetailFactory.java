package co.invest72.investment.domain.investment.factory;

import java.util.List;

import co.invest72.investment.domain.investment.InvestmentDetail;

public interface InvestmentDetailFactory {
	List<InvestmentDetail> createMonthlyDetails();

	List<InvestmentDetail> createYearlyDetails();
}
