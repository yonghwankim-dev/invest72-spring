package co.invest72.common.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@NotNull(message = "투자 금액은 필수입니다.")
@DecimalMin(value = "0", message = "투자 금액은 0원 이상이어야 합니다.")
@DecimalMax(value = "10000000000000", message = "투자 금액은 10조원 이하이어야 합니다.")
@Documented
public @interface FinancialAmount {
	String message() default "유효하지 않은 금액입니다.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
