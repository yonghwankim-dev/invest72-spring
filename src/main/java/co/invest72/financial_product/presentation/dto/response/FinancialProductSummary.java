package co.invest72.financial_product.presentation.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import co.invest72.financial_product.domain.FinancialProduct;
import co.invest72.investment.domain.Investment;
import co.invest72.money.domain.Currency;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
@Getter
public class FinancialProductSummary {
	private final String id; // 식별자
	private final String name; // 상품명
	private final String investmentType; // 투자 유형
	private final BigDecimal interestRate; // 이자율
	private final LocalDate startDate; // 시작일
	private final LocalDate expirationDate; // 만기일
	private final BigDecimal balance; // 현재 잔액(원금)
	private final BigDecimal expectedInterest; // 예상 이자
	private final BigDecimal progress; // 진행률
	private final long remainingDays; // 남은 일수
	private final LocalDateTime createdAt; // 생성시
	private final ProductCurrency productCurrency; // 통화 정보

	public static FinancialProductSummary from(
		FinancialProduct product,
		Investment investment,
		LocalDate today
	) {
		Currency currency = Currency.from(product.getAmount().getCurrency());
		ProductCurrency productCurrency = ProductCurrency.from(currency);
		return FinancialProductSummary.builder()
			.id(product.getId())
			.name(product.getName())
			.investmentType(product.getProductInvestmentType().getName())
			.interestRate(product.getProductAnnualInterestRate().getValue())
			.startDate(product.getStartDate())
			.expirationDate(product.getExpirationDate())
			.balance(product.getBalanceByLocalDate(today))
			.expectedInterest(investment.getTotalInterest().getValue())
			.progress(product.getProgressByLocalDate(today))
			.remainingDays(product.getRemainingDaysByLocalDate(today))
			.createdAt(product.getCreatedAt())
			.productCurrency(productCurrency)
			.build();
	}
}
