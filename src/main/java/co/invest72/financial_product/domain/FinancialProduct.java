package co.invest72.financial_product.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import co.invest72.financial_product.infrastructure.ProductIdGenerator;
import co.invest72.investment.domain.interest.InterestType;
import co.invest72.investment.domain.investment.InvestmentType;
import co.invest72.investment.domain.tax.TaxType;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Getter
public class FinancialProduct {
	@Id
	private String id;

	@Column(name = "user_id", nullable = false, updatable = false)
	private String userId;

	@Column(name = "name", nullable = false, length = 100)
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(name = "product_type", nullable = false, length = 100)
	private InvestmentType investmentType;

	@Embedded
	private ProductAmount amount; // 원금 또는 월 적립액

	@Embedded
	@AttributeOverride(name = "value", column = @Column(name = "months", nullable = false))
	private ProductMonths months; // 기간 (개월)

	@Embedded
	@AttributeOverride(name = "value", column = @Column(name = "interest_rate", nullable = false, precision = 5, scale = 4))
	private ProductRate interestRate; // 연이율

	@Enumerated(EnumType.STRING)
	@Column(name = "interest_type", nullable = false, length = 100)
	private InterestType interestType; // 이자 유형 (단리, 복리)

	@Enumerated(EnumType.STRING)
	@Column(name = "tax_type", nullable = false, length = 100)
	private TaxType taxType; // 세금 유형 (과세, 비과세)

	@Embedded
	@AttributeOverride(name = "value", column = @Column(name = "tax_rate", nullable = false, precision = 5, scale = 4))
	private ProductRate taxRate; // 세율 (예: 0.15 for 15%)

	@Column(name = "start_date", nullable = false)
	private LocalDate startDate; // 투자 시작일

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt; // 생성 일시

	private static final IdGenerator idGenerator = new ProductIdGenerator("product");

	@Builder(toBuilder = true)
	private FinancialProduct(String userId, String name, InvestmentType investmentType, ProductAmount amount,
		ProductMonths months, ProductRate interestRate, InterestType interestType, TaxType taxType, ProductRate taxRate,
		LocalDate startDate, LocalDateTime createdAt) {
		// ID가 외부에서 주입되지 않았다면 스스로 생성 (In-memory, JPA 공통 적용)
		this.id = idGenerator.generateId();
		this.userId = userId;
		this.name = name;
		this.investmentType = investmentType;
		this.amount = amount;
		this.months = months;
		this.interestRate = interestRate;
		this.interestType = interestType;
		this.taxType = taxType;
		this.taxRate = taxRate;
		this.startDate = startDate;
		this.createdAt = createdAt;
	}

	public void update(FinancialProduct updatedProduct) {
		this.name = updatedProduct.getName();
		this.investmentType = updatedProduct.getInvestmentType();
		this.amount = updatedProduct.getAmount();
		this.months = updatedProduct.getMonths();
		this.interestRate = updatedProduct.getInterestRate();
		this.interestType = updatedProduct.getInterestType();
		this.taxType = updatedProduct.getTaxType();
		this.taxRate = updatedProduct.getTaxRate();
		this.startDate = updatedProduct.getStartDate();
	}

	public LocalDate getExpirationDate() {
		return startDate.plusMonths(months.getValue());
	}

	public BigDecimal getProgressByLocalDate(LocalDate today) {
		if (today.isBefore(startDate)) {
			return BigDecimal.ZERO;
		}
		if (today.isAfter(getExpirationDate())) {
			return BigDecimal.ONE;
		}
		long totalDays = startDate.until(getExpirationDate(), ChronoUnit.DAYS);
		long elapsedDays = startDate.until(today, ChronoUnit.DAYS);
		return BigDecimal.valueOf(elapsedDays)
			.divide(BigDecimal.valueOf(totalDays), 4, RoundingMode.HALF_EVEN);
	}

	public long getRemainingDaysByLocalDate(LocalDate today) {
		if (today.isAfter(getExpirationDate())) {
			return 0;
		}
		return today.until(getExpirationDate(), ChronoUnit.DAYS);
	}
}
