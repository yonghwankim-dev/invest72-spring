package source;

import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;

public class ProductRateTestDataProvider {
	// 0 ~ 9.9999 범위의 유효한 이율/세율 값을 제공하는 메서드
	public static Stream<Arguments> validValues() {
		return Stream.of(
			Arguments.of(new java.math.BigDecimal("0")),
			Arguments.of(new java.math.BigDecimal("0.01")),
			Arguments.of(new java.math.BigDecimal("1.2345")),
			Arguments.of(new java.math.BigDecimal("9.9999"))
		);
	}

	public static Stream<Arguments> invalidValues() {
		return Stream.of(
			Arguments.of((java.math.BigDecimal)null),
			Arguments.of(new java.math.BigDecimal("-0.01")),
			Arguments.of(new java.math.BigDecimal("-1")),
			Arguments.of(new java.math.BigDecimal("10000000000000.01")),
			Arguments.of(new java.math.BigDecimal("10000000000001"))
		);
	}
}
