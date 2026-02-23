package source;

public class ProductMonthsTestDataProvider {
	public static java.util.stream.Stream<org.junit.jupiter.params.provider.Arguments> validValues() {
		return java.util.stream.Stream.of(
			org.junit.jupiter.params.provider.Arguments.of(0),
			org.junit.jupiter.params.provider.Arguments.of(1),
			org.junit.jupiter.params.provider.Arguments.of(12),
			org.junit.jupiter.params.provider.Arguments.of(9999)
		);
	}

	public static java.util.stream.Stream<org.junit.jupiter.params.provider.Arguments> invalidValues() {
		return java.util.stream.Stream.of(
			org.junit.jupiter.params.provider.Arguments.of((Integer)null),
			org.junit.jupiter.params.provider.Arguments.of(-1),
			org.junit.jupiter.params.provider.Arguments.of(-100),
			org.junit.jupiter.params.provider.Arguments.of(10000)
		);
	}
}
