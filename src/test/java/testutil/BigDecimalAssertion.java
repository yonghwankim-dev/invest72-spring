package testutil;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;

public class BigDecimalAssertion {
	private static final BigDecimal deltaBigDecimal = BigDecimal.valueOf(0.000001);

	public static void assertBigDecimalEquals(BigDecimal expected, BigDecimal actual) {
		BigDecimal diff = expected.subtract(actual).abs();
		Assertions.assertTrue(diff.compareTo(deltaBigDecimal) <= 0,
			() -> "Expected: " + expected + ", but was: " + actual + ", difference: " + diff);
	}
}
