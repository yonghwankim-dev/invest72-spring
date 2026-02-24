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
@NotNull(message = "비율 설정은 필수입니다.")
@DecimalMin(value = "0", message = "비율은 0% 이상이어야 합니다.")
@DecimalMax(value = "9.9999", message = "비율은 최대 9.9999%까지 설정 가능합니다.")
@Documented
public @interface FinancialRate {
	String message() default "유효하지 않은 비율입니다.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
