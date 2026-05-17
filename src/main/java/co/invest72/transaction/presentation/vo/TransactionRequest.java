package co.invest72.transaction.presentation.vo;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class TransactionRequest {
	private String type;
	private BigDecimal amount;
	private String content;
}
