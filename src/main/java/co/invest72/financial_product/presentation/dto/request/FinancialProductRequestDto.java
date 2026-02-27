package co.invest72.financial_product.presentation.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import co.invest72.common.validation.EnumValid;
import co.invest72.common.validation.FinancialAmount;
import co.invest72.common.validation.FinancialMonths;
import co.invest72.common.validation.FinancialProductName;
import co.invest72.common.validation.FinancialRate;
import co.invest72.investment.domain.interest.InterestType;
import co.invest72.investment.domain.investment.InvestmentType;
import co.invest72.investment.domain.tax.TaxType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class FinancialProductRequestDto {
	@FinancialProductName
	private String name;

	@EnumValid(enumClass = InvestmentType.class, message = "유효하지 않은 상품 유형입니다.")
	@NotNull(message = "상품 유형은 필수입니다.")
	private String investmentType;

	@FinancialAmount
	private BigDecimal amount;

	@FinancialMonths
	private Integer months;

	@FinancialRate
	private BigDecimal interestRate;

	@EnumValid(enumClass = InterestType.class, message = "유효하지 않은 이자 유형입니다.")
	@NotNull(message = "이자 유형은 필수입니다.")
	private String interestType;

	@EnumValid(enumClass = TaxType.class, message = "유효하지 않은 세금 유형입니다.")
	@NotNull(message = "세금 유형은 필수입니다.")
	private String taxType;

	@FinancialRate
	private BigDecimal taxRate;

	@NotNull(message = "시작 날짜는 필수입니다.")
	private LocalDate startDate;
}
