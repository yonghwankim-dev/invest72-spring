package source;

import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;

public class ProductMonthsTestDataProvider {
	public static Stream<Arguments> validValues() {
		return java.util.stream.Stream.of(
			Arguments.of(0),
			Arguments.of(1),
			Arguments.of(12),
			Arguments.of(9999)
		);
	}

	public static Stream<Arguments> invalidValues() {
		return java.util.stream.Stream.of(
			Arguments.of((Integer)null),
			Arguments.of(-1),
			Arguments.of(-100),
			Arguments.of(10000)
		);
	}
}
