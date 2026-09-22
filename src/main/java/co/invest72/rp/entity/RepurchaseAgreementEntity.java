package co.invest72.rp.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import co.invest72.financial_product.domain.ProductAmount;
import co.invest72.financial_product.domain.ProductAnnualInterestRate;
import co.invest72.financial_product.domain.ProductInterestType;
import co.invest72.financial_product.domain.ProductInvestmentType;
import co.invest72.financial_product.domain.ProductTaxRate;
import co.invest72.financial_product.domain.ProductTaxType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RepurchaseAgreementEntity {
	@Id
	private String id;

	@Column(name = "user_id", nullable = false, updatable = false)
	private String userId;

	@Embedded
	private ProductInvestmentType productInvestmentType;

	@Column(name = "name", nullable = false, length = 100)
	private String name;

	@Embedded
	private ProductAmount amount;

	@Column(name = "days", nullable = false)
	private Integer days; // 약정 일수

	@Embedded
	private ProductAnnualInterestRate productAnnualInterestRate; // 연이율

	@Embedded
	private ProductInterestType productInterestType;

	@Embedded
	private ProductTaxType productTaxType;

	@Embedded
	private ProductTaxRate productTaxRate; // 세율 (예: 0.15 for 15%)

	@Column(name = "start_date", nullable = false)
	private LocalDate startDate; // 투자 시작일

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt; // 생성 일시

	@Builder(toBuilder = true)
	public RepurchaseAgreementEntity(String id, String userId, ProductInvestmentType productInvestmentType, String name,
		ProductAmount amount, Integer days,
		ProductAnnualInterestRate productAnnualInterestRate, ProductInterestType productInterestType,
		ProductTaxType productTaxType, ProductTaxRate productTaxRate, LocalDate startDate, LocalDateTime createdAt) {
		validateDays(days);
		this.id = Objects.requireNonNull(id);
		this.userId = Objects.requireNonNull(userId);
		this.productInvestmentType = Objects.requireNonNull(productInvestmentType);
		this.name = Objects.requireNonNull(name);
		this.amount = Objects.requireNonNull(amount);
		this.days = Objects.requireNonNull(days);
		this.productAnnualInterestRate = Objects.requireNonNull(productAnnualInterestRate);
		this.productInterestType = Objects.requireNonNull(productInterestType);
		this.productTaxType = Objects.requireNonNull(productTaxType);
		this.productTaxRate = Objects.requireNonNull(productTaxRate);
		this.startDate = Objects.requireNonNull(startDate);
		this.createdAt = Objects.requireNonNull(createdAt);
	}

	private void validateDays(Integer days) {
		if (days <= 0) {
			throw new IllegalArgumentException("days must not zero or not negative, days=" + days);
		}
	}

	public String getTypeName() {
		return getProductInvestmentType().getName();
	}
}
