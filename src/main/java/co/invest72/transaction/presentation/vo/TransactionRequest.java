package co.invest72.transaction.presentation.vo;

import java.math.BigDecimal;

import co.invest72.common.validation.EnumValid;
import co.invest72.transaction.domain.TransactionType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class TransactionRequest {
	@EnumValid(enumClass = TransactionType.class, message = "유효하지 않은 거래 종류입니다.")
	@NotNull(message = "거래 종류는 null이면 안됩니다.")
	private TransactionType type;
	private BigDecimal amount;
	private String content;
}
