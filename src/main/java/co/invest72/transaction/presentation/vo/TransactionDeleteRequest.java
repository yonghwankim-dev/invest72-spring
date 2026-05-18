package co.invest72.transaction.presentation.vo;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class TransactionDeleteRequest {
	@NotNull(message = "거래 내역 식별자 리스트는 null이면 안됩니다.")
	private final List<String> transactionIds;

	public TransactionDeleteRequest(List<String> transactionIds) {
		this.transactionIds = transactionIds;
	}
}
