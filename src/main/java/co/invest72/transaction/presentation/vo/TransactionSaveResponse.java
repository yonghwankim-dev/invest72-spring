package co.invest72.transaction.presentation.vo;

import java.util.Objects;

import lombok.Getter;

@Getter
public class TransactionSaveResponse {
	private final String transactionId;

	public TransactionSaveResponse(String transactionId) {
		this.transactionId = Objects.requireNonNull(transactionId);
	}
}
