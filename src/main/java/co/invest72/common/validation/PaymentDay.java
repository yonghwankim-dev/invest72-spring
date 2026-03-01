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

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@Min(value = 1, message = "납입일은 1일부터 31일까지 가능합니다.")
@Max(value = 31, message = "납입일은 1일부터 31일까지 가능합니다.")
@Documented
public @interface PaymentDay {
	String message() default "유효하지 않은 납입일입니다.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
