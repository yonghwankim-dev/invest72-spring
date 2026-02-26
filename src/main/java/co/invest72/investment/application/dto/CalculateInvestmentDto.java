package co.invest72.investment.application.dto;

import co.invest72.financial_product.domain.ProductAmount;
import co.invest72.financial_product.domain.ProductMonths;
import co.invest72.investment.domain.InterestRate;
import co.invest72.investment.domain.TaxRate;
import co.invest72.investment.domain.interest.InterestType;
import co.invest72.investment.domain.investment.InvestmentType;
import co.invest72.investment.domain.tax.TaxType;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder
public class CalculateInvestmentDto {
	private final InvestmentType type;
	private final ProductAmount amount;
	private final ProductMonths months;
	private final InterestRate interestRate;
	private final InterestType interestType;
	private final TaxType taxType;
	private final TaxRate taxRate;
}
