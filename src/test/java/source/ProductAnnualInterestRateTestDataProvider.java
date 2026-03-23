package source;

import java.math.BigDecimal;
import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;

public class ProductAnnualInterestRateTestDataProvider {
	// 0 ~ 9.9999 범위의 유효한 이율/세율 값을 제공하는 메서드
	public static Stream<Arguments> provideValues() {
		return Stream.of(
			Arguments.of(new BigDecimal("0")),
			Arguments.of(new BigDecimal("0.01")),
			Arguments.of(new BigDecimal("1.2345")),
			Arguments.of(new BigDecimal("9.9999"))
		);
	}

	public static Stream<Arguments> invalidValues() {
		return Stream.of(
			Arguments.of((BigDecimal)null),
			Arguments.of(new BigDecimal("-0.01")),
			Arguments.of(new BigDecimal("-1")),
			Arguments.of(new BigDecimal("10000000000000.01")),
			Arguments.of(new BigDecimal("10000000000001"))
		);
	}

	public static Stream<Arguments> provideScaledValues() {
		BigDecimal pivot = BigDecimal.valueOf(0.1234);
		return Stream.of(
			Arguments.of(pivot, new BigDecimal("0.12340")),
			Arguments.of(pivot, new BigDecimal("0.12341")),
			Arguments.of(pivot, new BigDecimal("0.12342")),
			Arguments.of(pivot, new BigDecimal("0.12343")),
			Arguments.of(pivot, new BigDecimal("0.12344")),
			Arguments.of(pivot, new BigDecimal("0.12345"))
		);
	}
}
