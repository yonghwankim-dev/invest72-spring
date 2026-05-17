package co.invest72.transaction.dto;

import java.math.BigDecimal;

import co.invest72.transaction.domain.TransactionType;
import lombok.Getter;

@Getter
public class TransactionDto {
	private final String type;
	private final BigDecimal amount;
	private final String content;
	private final String userId;

	public TransactionDto(TransactionType type, BigDecimal amount, String content, String userId) {
		this.type = type.getTitle();
		this.amount = amount;
		this.content = content;
		this.userId = userId;
	}
}
