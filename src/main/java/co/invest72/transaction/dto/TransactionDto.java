package co.invest72.transaction.dto;

import java.math.BigDecimal;
import java.util.Objects;

import co.invest72.transaction.domain.TransactionType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TransactionDto {
	private final String type;
	private final BigDecimal amount;
	private final String content;
	private final String userId;

	@Builder
	public TransactionDto(TransactionType type, BigDecimal amount, String content, String userId) {
		this.type = Objects.requireNonNull(type.name());
		this.amount = Objects.requireNonNull(amount);
		this.content = Objects.requireNonNull(content);
		this.userId = Objects.requireNonNull(userId);
	}
}
