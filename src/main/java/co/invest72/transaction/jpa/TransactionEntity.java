package co.invest72.transaction.jpa;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import co.invest72.financial_product.domain.IdGenerator;
import co.invest72.transaction.infrastructure.TransactionIdGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "transaction")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"})
public class TransactionEntity {
	@Id
	private String id;

	@Column(name = "type", nullable = false)
	private String type;

	@Column(name = "amount", nullable = false, precision = 19, scale = 2)
	private BigDecimal amount;

	@Column(name = "content")
	private String content;

	@Column(name = "user_id", nullable = false)
	private String userId;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	private static final IdGenerator idGenerator = new TransactionIdGenerator("transaction");

	@Builder(toBuilder = true)
	public TransactionEntity(String id, String type, BigDecimal amount, String content, String userId,
		LocalDateTime createdAt) {
		this.id = Objects.requireNonNull(id == null ? idGenerator.generateId() : id);
		this.type = Objects.requireNonNull(type);
		this.amount = Objects.requireNonNull(amount);
		this.content = content;
		this.userId = Objects.requireNonNull(userId);
		this.createdAt = Objects.requireNonNull(createdAt == null ? LocalDateTime.now() : createdAt);
	}

	public void update(TransactionEntity updatedTransaction) {
		validate(updatedTransaction);
		this.type = Objects.requireNonNull(updatedTransaction.type);
		this.amount = Objects.requireNonNull(updatedTransaction.amount);
		this.content = updatedTransaction.content;
	}

	private void validate(TransactionEntity updatedTransaction) {
		if (!id.equals(updatedTransaction.getId())) {
			throw new IllegalArgumentException("거래 내역 ID는 변경할 수 없습니다.");
		}
		if (!userId.equals(updatedTransaction.getUserId())) {
			throw new IllegalArgumentException("거래 내역 소유자(userId)는 변경할 수 없습니다.");
		}
		if (!createdAt.equals(updatedTransaction.getCreatedAt())) {
			throw new IllegalArgumentException("생성 날짜(createdAt)는 변경할 수 없습니다.");
		}
	}
}
