package co.invest72.transaction.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TransactionDto {
	private final String transactionId;
	private final String type;
	private final BigDecimal amount;
	private final String content;
	private final String userId;
	private final LocalDateTime createdAt;

	@Builder(toBuilder = true)
	public TransactionDto(String transactionId, String type, BigDecimal amount, String content, String userId,
		LocalDateTime createdAt) {
		this.transactionId = transactionId;
		this.type = type;
		this.amount = amount;
		this.content = content;
		this.userId = userId;
		this.createdAt = createdAt;
	}

	public TransactionDto withTransactionId(String transactionId) {
		return this.toBuilder()
			.transactionId(transactionId)
			.build();
	}

	public TransactionDto withUserId(String userId) {
		return this.toBuilder()
			.userId(userId)
			.build();
	}

	public TransactionDto withCreatedAt(LocalDateTime createdAt) {
		return this.toBuilder()
			.createdAt(createdAt)
			.build();
	}
}
