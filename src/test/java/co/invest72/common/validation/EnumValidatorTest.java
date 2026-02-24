package co.invest72.common.validation;

import java.lang.annotation.Annotation;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import co.invest72.financial_product.domain.ProductType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.Payload;

class EnumValidatorTest {

	private ConstraintValidator<EnumValid, String> enumValidator;

	@BeforeEach
	void setUp() {
		enumValidator = new EnumValidator();
		enumValidator.initialize(getProductTypeEnumValid());
	}

	@ParameterizedTest
	@EnumSource(value = ProductType.class)
	@DisplayName("유효 판단 - 유효한 값")
	void isValid_whenValueIsValid_thenReturnTrue(ProductType productType) {
		// Given
		String validValue = productType.name();

		// When
		boolean result = enumValidator.isValid(validValue, null);

		// Then
		Assertions.assertThat(result).isTrue();
	}

	@Test
	@DisplayName("유효 판단 - 유효하지 않은 값")
	void isValid_whenValueIsInvalid_thenReturnFalse() {
		// Given
		String invalidValue = "INVALID_TYPE";

		// When
		boolean result = enumValidator.isValid(invalidValue, null);

		// Then
		Assertions.assertThat(result).isFalse();
	}

	@DisplayName("유효 판단 - Null 값은 유효한 것으로 간주")
	@Test
	void isValid_whenValueIsNull_thenReturnTrue() {
		// When
		boolean result = enumValidator.isValid(null, null);

		// Then
		Assertions.assertThat(result).isTrue();
	}

	private EnumValid getProductTypeEnumValid() {
		return new EnumValid() {

			@Override
			public Class<? extends Annotation> annotationType() {
				return EnumValid.class;
			}

			@Override
			public String message() {
				return "유효하지 않은 값입니다.";
			}

			@Override
			public Class<?>[] groups() {
				return new Class[0];
			}

			@Override
			public Class<? extends Payload>[] payload() {
				return new Class[0];
			}

			@Override
			public Class<? extends Enum<?>> enumClass() {
				return ProductType.class;
			}
		};
	}
}
