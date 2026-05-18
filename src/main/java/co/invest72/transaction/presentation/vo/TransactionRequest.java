package co.invest72.transaction.presentation.vo;

import java.math.BigDecimal;

import co.invest72.common.validation.BigDecimalAmount;
import co.invest72.transaction.domain.TransactionType;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class TransactionRequest {
	@NotNull(message = "거래 종류는 null이면 안됩니다.")
	private TransactionType type;
	@BigDecimalAmount
	private BigDecimal amount;
	@Nullable
	private String content;
}
