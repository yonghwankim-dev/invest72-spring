package co.invest72.common.validation;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<EnumValid, String> {
	private Set<String> enumValues;

	@Override
	public void initialize(EnumValid annotation) {
		enumValues = Stream.of(annotation.enumClass().getEnumConstants())
			.map(Enum::name)
			.collect(Collectors.toSet());
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null) {
			return false; // @NotBlank 역할 포함
		}
		return enumValues.contains(value);
	}
}
