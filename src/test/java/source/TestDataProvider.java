package source;

import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;

public class TestDataProvider {
	public static Stream<Arguments> getPrincipalWithMonthSource() {
		return Stream.of(
			Arguments.of(-1, 0),
			Arguments.of(0, 0),
			Arguments.of(1, 0),
			Arguments.of(12, 11_232_055)
		);
	}

	public static Stream<Arguments> getInterestWithMonthSource() {
		return Stream.of(
			Arguments.of(-1, 0),
			Arguments.of(0, 0),
			Arguments.of(1, 0),
			Arguments.of(2, 4_167),
			Arguments.of(12, 46_800)
		);
	}

	public static Stream<Arguments> getMonthAndExpectedAccumulatedInterest() {
		return Stream.of(
			Arguments.of(-1, 0),
			Arguments.of(0, 0),
			Arguments.of(1, 4_167),
			Arguments.of(2, 8_333),
			Arguments.of(3, 12_500),
			Arguments.of(4, 16_667),
			Arguments.of(5, 20_833),
			Arguments.of(6, 25_000),
			Arguments.of(7, 29_167),
			Arguments.of(8, 33_333),
			Arguments.of(9, 37_500),
			Arguments.of(10, 41_667),
			Arguments.of(11, 45_833),
			Arguments.of(12, 50_000),
			Arguments.of(13, 50_000)
		);
	}

	public static Stream<Arguments> getTotalProfitWithMonthSource() {
		return Stream.of(
			Arguments.of(-1, 0),
			Arguments.of(0, 0),
			Arguments.of(1, 0),
			Arguments.of(2, 1_004_167),
			Arguments.of(12, 11_278_855)
		);
	}

	public static Stream<Arguments> getTaxPercentFormatSource() {
		return Stream.of(
			Arguments.of(0.154, "15.4%"),
			Arguments.of(0.155, "15.5%"),
			Arguments.of(0.1556, "15.56%"),
			Arguments.of(0.123446, "12.34%"),
			Arguments.of(0.123456, "12.35%"),
			Arguments.of(0.1, "10%"),
			Arguments.of(0.0, "0%")
		);
	}
}
