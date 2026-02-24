package co.invest72.financial_product.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import co.invest72.investment.domain.interest.InterestType;
import co.invest72.investment.domain.tax.TaxType;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@Builder(toBuilder = true)
@Getter
public class FinancialProduct {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;

	@Column(name = "user_id", nullable = false, updatable = false)
	private String userId;

	@Column(name = "name", nullable = false, length = 100)
	private String name;

	@Enumerated(EnumType.STRING)
	private ProductType productType;

	@Embedded
	private ProductAmount amount; // 원금 또는 월 적립액

	@Column(name = "months", nullable = false)
	private Integer months; // 기간 (개월)

	@Embedded
	@AttributeOverride(name = "value", column = @Column(name = "interest_rate", nullable = false, precision = 5, scale = 4))
	private ProductRate interestRate; // 연이율

	@Enumerated(EnumType.STRING)
	private InterestType interestType; // 이자 유형 (단리, 복리)

	@Enumerated(EnumType.STRING)
	private TaxType taxType; // 세금 유형 (과세, 비과세)

	@Embedded
	@AttributeOverride(name = "value", column = @Column(name = "tax_rate", nullable = false, precision = 5, scale = 4))
	private ProductRate taxRate; // 세율 (예: 0.15 for 15%)

	@Column(name = "start_date", nullable = false)
	private LocalDate startDate; // 투자 시작일

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt; // 생성 일시
}
