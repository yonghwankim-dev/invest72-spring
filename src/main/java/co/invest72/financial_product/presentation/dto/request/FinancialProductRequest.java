package co.invest72.financial_product.presentation.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import co.invest72.common.validation.EnumValid;
import co.invest72.common.validation.FinancialAmount;
import co.invest72.common.validation.FinancialMonths;
import co.invest72.common.validation.FinancialProductName;
import co.invest72.common.validation.FinancialRate;
import co.invest72.common.validation.PaymentDay;
import co.invest72.financial_product.domain.entity.FinancialProductData;
import co.invest72.investment.domain.interest.InterestType;
import co.invest72.investment.domain.investment.InvestmentType;
import co.invest72.investment.domain.tax.TaxType;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder(toBuilder = true)
public class FinancialProductRequest implements FinancialProductData {
	@FinancialProductName
	private String name;

	@EnumValid(enumClass = InvestmentType.class, message = "유효하지 않은 상품 유형입니다.")
	@NotNull(message = "상품 유형은 필수입니다.")
	private String investmentType;

	@FinancialAmount
	private BigDecimal amount;

	@FinancialMonths
	private Integer months;

	@PaymentDay
	private Integer paymentDay; // 납입일 (적금 상품에만 적용, 현금/예금은 null)

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

	@NotNull(message = "통화는 필수입니다.")
	private String currencyCode;

	@Nullable
	private String productId;

	@Nullable
	private String userId;

	@Nullable
	private LocalDateTime createdAt;

	@Override
	public Optional<String> getProductId() {
		return Optional.ofNullable(this.productId);
	}

	@Override
	public Optional<String> getUserId() {
		return Optional.ofNullable(this.userId);
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getInvestmentType() {
		return this.investmentType;
	}

	@Override
	public BigDecimal getAmount() {
		return this.amount;
	}

	@Override
	public Integer getMonths() {
		return this.months;
	}

	@Override
	public Optional<Integer> getPaymentDay() {
		return Optional.ofNullable(this.paymentDay);
	}

	@Override
	public BigDecimal getInterestRate() {
		return this.interestRate;
	}

	@Override
	public String getInterestType() {
		return interestType;
	}

	@Override
	public String getTaxType() {
		return this.taxType;
	}

	@Override
	public BigDecimal getTaxRate() {
		return this.taxRate;
	}

	@Override
	public LocalDate getStartDate() {
		return this.startDate;
	}

	@Override
	public String getCurrencyCode() {
		return this.currencyCode;
	}

	@Override
	public LocalDateTime getCreatedAt() {
		return this.createdAt;
	}

	@Override
	public FinancialProductData withProductId(String productId) {
		return this.toBuilder()
			.productId(productId)
			.build();
	}

	@Override
	public FinancialProductData withUserId(String userId) {
		return this.toBuilder()
			.userId(userId)
			.build();
	}

	@Override
	public FinancialProductData withCreatedAt(LocalDateTime createdAt) {
		return this.toBuilder()
			.createdAt(createdAt)
			.build();
	}
}
