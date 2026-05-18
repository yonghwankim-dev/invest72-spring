package co.invest72.transaction.presentation.vo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TransactionResponse {
	private final String transactionId;
	private final String type;
	private final BigDecimal amount;
	private final String content;
	private final LocalDateTime createdAt;

	@Builder
	public TransactionResponse(String transactionId, String type, BigDecimal amount, String content,
		LocalDateTime createdAt) {
		this.transactionId = Objects.requireNonNull(transactionId);
		this.type = Objects.requireNonNull(type);
		this.amount = Objects.requireNonNull(amount);
		this.content = content;
		this.createdAt = Objects.requireNonNull(createdAt);
	}
}
