package co.invest72.financial_product.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import co.invest72.financial_product.infrastructure.ProductIdGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
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
	private ProductMonths months; // 기간 (개월)

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

	private static final IdGenerator idGenerator = new ProductIdGenerator("product");

	protected FinancialProduct(FinancialProductBuilder<?, ?> b) {
		this.id = b.id != null ? b.id : Objects.requireNonNull(idGenerator.generateId()); // 빌더에서 ID가 주어지지 않으면 생성
		this.userId = Objects.requireNonNull(b.userId);
		this.name = Objects.requireNonNull(b.name);
		this.productInvestmentType = Objects.requireNonNull(b.productInvestmentType);
		this.amount = Objects.requireNonNull(b.amount);
		this.months = Objects.requireNonNull(b.months);
		this.productAnnualInterestRate = Objects.requireNonNull(b.productAnnualInterestRate);
		this.productInterestType = Objects.requireNonNull(b.productInterestType);
		this.productTaxType = Objects.requireNonNull(b.productTaxType);
		this.productTaxRate = Objects.requireNonNull(b.productTaxRate);
		this.startDate = Objects.requireNonNull(b.startDate);
		this.createdAt = Objects.requireNonNull(b.createdAt);
	}

	/**
	 * 상품 정보 업데이트<br>
	 * 업데이트된 상품 정보로 현재 객체의 필드 값을 변경 (ID, userId, investmentType, createdAt는 유지)
	 * @param updatedProduct 업데이트된 상품 정보 (ID, userId, investmentType, createdAt는 무시되고 유지됨)
	 */
	public void update(FinancialProduct updatedProduct) {
		validateUpdate(updatedProduct);
		this.name = Objects.requireNonNull(updatedProduct.getName());
		this.amount = Objects.requireNonNull(updatedProduct.getAmount());
		this.months = Objects.requireNonNull(updatedProduct.getMonths());
		this.productAnnualInterestRate = Objects.requireNonNull(updatedProduct.getProductAnnualInterestRate());
		this.productInterestType = Objects.requireNonNull(updatedProduct.getProductInterestType());
		this.productTaxType = Objects.requireNonNull(updatedProduct.getProductTaxType());
		this.productTaxRate = Objects.requireNonNull(updatedProduct.getProductTaxRate());
		this.startDate = Objects.requireNonNull(updatedProduct.getStartDate());
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

	public String getInvestmentTypeName() {
		return getProductInvestmentType().getName();
	}

	public Integer getMonthsValue() {
		return getMonths().getValue();
	}
}
