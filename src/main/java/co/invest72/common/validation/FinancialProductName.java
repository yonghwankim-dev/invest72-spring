package co.invest72.common.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@NotBlank(message = "상품 이름은 필수입니다.")
@Size(min = 1, max = 100, message = "상품 이름은 최대 100자까지 입력 가능합니다.")
@Documented
public @interface FinancialProductName {
	String message() default "유효하지 않은 상품 이름입니다.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
