package co.invest72.financial_product.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import co.invest72.financial_product.infrastructure.ProductIdGenerator;
import co.invest72.investment.domain.interest.AnnualInterestRate;
import co.invest72.investment.domain.interest.InterestType;
import co.invest72.investment.domain.investment.InvestmentType;
import co.invest72.investment.domain.tax.FixedTaxRate;
import co.invest72.investment.domain.tax.TaxType;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Inheritance(strategy = jakarta.persistence.InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "product_type", discriminatorType = jakarta.persistence.DiscriminatorType.STRING)
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Getter
@SuperBuilder(toBuilder = true)
public abstract class FinancialProduct {
	@Id
	private String id;

	@Column(name = "user_id", nullable = false, updatable = false)
	private String userId;

	@Column(name = "name", nullable = false, length = 100)
	private String name;

	@Embedded
	private ProductInvestmentType productInvestmentType;

	@Embedded
	private ProductAmount amount; // 원금 또는 월 적립액

	@Embedded
	@AttributeOverride(name = "value", column = @Column(name = "months", nullable = false))
	private ProductMonths months; // 기간 (개월)
	
	@Embedded
	private ProductAnnualInterestRate productAnnualInterestRate; // 연이율

	@Enumerated(EnumType.STRING)
	@Column(name = "interest_type", nullable = false, length = 100)
	private InterestType interestType; // 이자 유형 (단리, 복리)

	@Enumerated(EnumType.STRING)
	@Column(name = "tax_type", nullable = false, length = 100)
	private TaxType taxType; // 세금 유형 (과세, 비과세)

	@Embedded
	@AttributeOverride(name = "value", column = @Column(name = "tax_rate", nullable = false, precision = 5, scale = 4))
	private FixedTaxRate taxRate; // 세율 (예: 0.15 for 15%)

	@Column(name = "start_date", nullable = false)
	private LocalDate startDate; // 투자 시작일

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt; // 생성 일시

	private static final IdGenerator idGenerator = new ProductIdGenerator("product");

	protected FinancialProduct(FinancialProductBuilder<?, ?> b) {
		this.id = b.id != null ? b.id : Objects.requireNonNull(idGenerator.generateId()); // 빌더에서 ID가 주어지지 않으면 생성
		this.userId = Objects.requireNonNull(b.userId);
		this.name = Objects.requireNonNull(b.name);
		this.productInvestmentType = Objects.requireNonNull(b.productInvestmentType);
		this.amount = Objects.requireNonNull(b.amount);
		this.months = Objects.requireNonNull(b.months);
		this.productAnnualInterestRate = Objects.requireNonNull(b.productAnnualInterestRate);
		this.interestType = Objects.requireNonNull(b.interestType);
		this.taxType = Objects.requireNonNull(b.taxType);
		this.taxRate = Objects.requireNonNull(b.taxRate);
		this.startDate = Objects.requireNonNull(b.startDate);
		this.createdAt = Objects.requireNonNull(b.createdAt);
	}

	/**
	 * 잔액 계산<br>
	 * @param today 현재 날짜
	 * @return 잔액 (현금 상품은 투자 금액 그대로 반환, 적금은 경과한 개월 수에 따라 누적된 금액 반환)
	 */
	public abstract BigDecimal getBalanceByLocalDate(LocalDate today);

	/**
	 * 상품 정보 업데이트<br>
	 * 업데이트된 상품 정보로 현재 객체의 필드 값을 변경 (ID, userId, investmentType, createdAt는 유지)
	 * @param updatedProduct 업데이트된 상품 정보 (ID, userId, investmentType, createdAt는 무시되고 유지됨)
	 */
	public void update(FinancialProduct updatedProduct) {
		validateUpdate(updatedProduct);
		this.name = updatedProduct.getName();
		this.amount = updatedProduct.getAmount();
		this.months = updatedProduct.getMonths();
		this.productAnnualInterestRate = updatedProduct.getProductAnnualInterestRate();
		this.interestType = updatedProduct.getInterestType();
		this.taxType = updatedProduct.getTaxType();
		this.taxRate = updatedProduct.getTaxRate();
		this.startDate = updatedProduct.getStartDate();
	}

	private void validateUpdate(FinancialProduct updatedProduct) {
		if (!getId().equals(updatedProduct.getId())) {
			throw new IllegalArgumentException("상품 ID는 변경할 수 없습니다.");
		}
		if (!getUserId().equals(updatedProduct.getUserId())) {
			throw new IllegalArgumentException("상품 소유자(userId)는 변경할 수 없습니다.");
		}
		if (!getProductInvestmentType().equals(updatedProduct.getProductInvestmentType())) {
			throw new IllegalArgumentException("투자 유형(InvestmentType)은 변경할 수 없습니다.");
		}
		if (!getCreatedAt().equals(updatedProduct.getCreatedAt())) {
			throw new IllegalArgumentException("생성 날짜(createdAt)는 변경할 수 없습니다.");
		}
	}

	/**
	 * 만기일 계산<br>
	 * @return 만기일 (현금 상품의 경우 LocalDate.MAX 반환)
	 */
	public LocalDate getExpirationDate() {
		return InvestmentType.valueOf(productInvestmentType.getName())
			.calculateExpirationDate(startDate, months.getValue());
	}

	/**
	 * 진행률 계산<br>
	 * @param today 현재 날짜
	 * @return 진행률 (0.0 ~ 1.0 사이의 값, 현금 상품은 항상 1.0 반환)
	 */
	public BigDecimal getProgressByLocalDate(LocalDate today) {
		return InvestmentType.valueOf(productInvestmentType.getName())
			.calculateProgress(startDate, getExpirationDate(), today);
	}

	/**
	 * 남은 일수 계산<br>
	 * @param today 현재 날짜
	 * @return 남은 일수 (만기일이 지났거나 일시금 상품인 경우 0 반환)
	 */
	public long getRemainingDaysByLocalDate(LocalDate today) {
		return InvestmentType.valueOf(productInvestmentType.getName())
			.calculateRemainingDays(today, getExpirationDate());
	}

	/**
	 * 납입일 여부 확인<br>
	 * 현금/예금은 기본적으로 항상 false, 적금은 paymentDay에 따라 true/false 반환
	 * @param today 현재 날짜
	 * @return 납입일 여부 (현금/예금은 항상 false, 적금은 paymentDay에 따라 true/false 반환)
	 */
	public boolean isPaidOn(LocalDate today) {
		return false;
	}

	/**
	 * 납입일 계산<br>
	 * 현금/예금은 납입일이 없으므로 null 반환, 적금은 paymentDay 값을 반환
	 * @return 납입일
	 */
	public Integer getPaymentDayValue() {
		return null;
	}

	public AnnualInterestRate getInterestRate() {
		return new AnnualInterestRate(getProductAnnualInterestRate().getValue());
	}
}
