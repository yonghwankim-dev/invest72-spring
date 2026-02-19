package testutil;

import java.math.BigDecimal;
import java.util.Comparator;

import org.junit.jupiter.api.Assertions;

public class BigDecimalAssertion {
	private static final BigDecimal deltaBigDecimal = BigDecimal.valueOf(0.000001);

	public static Comparator<BigDecimal> bigDecimalComparator() {
		return (bd1, bd2) -> {
			BigDecimal diff = bd1.subtract(bd2).abs();
			if (diff.compareTo(deltaBigDecimal) <= 0) {
				return 0;
			} else {
				return bd1.compareTo(bd2);
			}
		};
	}

	public static void assertBigDecimalEquals(BigDecimal expected, BigDecimal actual) {
		BigDecimal diff = expected.subtract(actual).abs();
		Assertions.assertTrue(diff.compareTo(deltaBigDecimal) <= 0,
			() -> "Expected: " + expected + ", but was: " + actual + ", difference: " + diff);
	}
}
