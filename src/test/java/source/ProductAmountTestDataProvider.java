package source;

import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;

public class ProductAmountTestDataProvider {
	public static Stream<Arguments> validAmounts() {
		return Stream.of(
			Arguments.of(new java.math.BigDecimal("0")),
			Arguments.of(new java.math.BigDecimal("0.01")),
			Arguments.of(new java.math.BigDecimal("1000")),
			Arguments.of(new java.math.BigDecimal("9999999999999.99")),
			Arguments.of(new java.math.BigDecimal("10000000000000"))
		);
	}

	public static Stream<Arguments> invalidAmounts() {
		return Stream.of(
			Arguments.of(new java.math.BigDecimal("-0.01")),
			Arguments.of(new java.math.BigDecimal("-1")),
			Arguments.of(new java.math.BigDecimal("10000000000000.01")),
			Arguments.of(new java.math.BigDecimal("10000000000001"))
		);
	}
}
