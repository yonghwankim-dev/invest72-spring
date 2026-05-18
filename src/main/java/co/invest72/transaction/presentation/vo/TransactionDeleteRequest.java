package co.invest72.transaction.presentation.vo;

import java.util.List;

import lombok.Getter;

@Getter
public class TransactionDeleteRequest {
	private final List<String> transactionIds;

	public TransactionDeleteRequest(List<String> transactionIds) {
		this.transactionIds = transactionIds;
	}
}
