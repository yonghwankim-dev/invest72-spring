package co.invest72.transaction.presentation.vo;

import java.util.List;
import java.util.Objects;

import lombok.Getter;

@Getter
public class TransactionListResponse {
	private List<TransactionResponse> transactions;

	public TransactionListResponse(List<TransactionResponse> transactions) {
		this.transactions = Objects.requireNonNull(transactions);
	}
}
