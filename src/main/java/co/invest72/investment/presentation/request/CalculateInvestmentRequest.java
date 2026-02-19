package co.invest72.investment.presentation.request;

import java.util.Objects;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@Getter
public class CalculateInvestmentRequest {
	@NotBlank(message = "type must not be blank")
	private String type;

	@NotBlank(message = "amountType must not be blank")
	private String amountType;

	@Min(value = 0, message = "amount must be non-negative")
	@NotNull(message = "amount must not be null")
	private Integer amount;

	@NotBlank(message = "periodType must not be blank")
	private String periodType;

	@Min(value = 0, message = "periodValue must be at least 1")
	@Max(value = 999, message = "periodValue must not be greater than 999")
	@NotNull(message = "periodValue must not be null")
	private Integer periodValue;

	@NotBlank(message = "interestType must not be blank")
	private String interestType;

	@Min(value = 0, message = "annualInterestRate must be non-negative")
	@Max(value = 1, message = "annualInterestRate must not be greater than 1")
	@NotNull(message = "annualInterestRate must not be null")
	private Double annualInterestRate;

	@NotBlank(message = "taxType must not be blank")
	private String taxType;

	@Min(value = 0, message = "taxRate must be non-negative")
	@Max(value = 1, message = "taxRate must not be greater than 1")
	@NotNull(message = "taxRate must not be null")
	private Double taxRate;

	@Builder(toBuilder = true)
	private CalculateInvestmentRequest(String type, String amountType, Integer amount, String periodType,
		Integer periodValue, String interestType, Double annualInterestRate, String taxType, Double taxRate) {
		this.type = Objects.requireNonNull(type, "type must not be null");
		this.amountType = Objects.requireNonNull(amountType, "amountType must not be null");
		this.amount = Objects.requireNonNull(amount, "amount must not be null");
		this.periodType = Objects.requireNonNull(periodType, "periodType must not be null");
		this.periodValue = Objects.requireNonNull(periodValue, "periodValue must not be null");
		this.interestType = Objects.requireNonNull(interestType, "interestType must not be null");
		this.annualInterestRate = Objects.requireNonNull(annualInterestRate, "annualInterestRate must not be null");
		this.taxType = Objects.requireNonNull(taxType, "taxType must not be null");
		this.taxRate = Objects.requireNonNull(taxRate, "taxRate must not be null");
	}

	@Override
	public String toString() {
		return "CalculateInvestmentRequest[" +
			"type=" + type + ", " +
			"amountType=" + amountType + ", " +
			"amount=" + amount + ", " +
			"periodType=" + periodType + ", " +
			"periodValue=" + periodValue + ", " +
			"interestType=" + interestType + ", " +
			"annualInterestRate=" + annualInterestRate + ", " +
			"taxType=" + taxType + ", " +
			"taxRate=" + taxRate + ']';
	}
}
