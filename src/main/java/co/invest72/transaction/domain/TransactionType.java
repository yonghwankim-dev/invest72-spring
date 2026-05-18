package co.invest72.transaction.domain;

import lombok.Getter;

@Getter
public enum TransactionType {
	INCOME("수입"), EXPENSE("지출");

	private final String title;

	TransactionType(String title) {
		this.title = title;
	}
}
