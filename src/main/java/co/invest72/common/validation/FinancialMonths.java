package co.invest72.common.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@NotNull(message = "투자 기간은 필수입니다.")
@Min(value = 0, message = "투자 기간은 최소 0개월 이상이어야 합니다.")
@Max(value = 11988, message = "투자 기간은 최대 11988개월 이하이어야 합니다.")
@Documented
public @interface FinancialMonths {
	String message() default "유효하지 않은 투자 기간입니다.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
