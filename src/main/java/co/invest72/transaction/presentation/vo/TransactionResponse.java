package co.invest72.transaction.presentation.vo;

import java.util.Objects;

import lombok.Getter;

@Getter
public class TransactionResponse {
	private final String transactionId;

	public TransactionResponse(String transactionId) {
		this.transactionId = Objects.requireNonNull(transactionId);
	}
}
