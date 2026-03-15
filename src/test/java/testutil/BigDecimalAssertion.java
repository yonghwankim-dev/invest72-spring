package testutil;

import java.math.BigDecimal;

import org.assertj.core.api.Assertions;

public class BigDecimalAssertion {
	private static final BigDecimal deltaBigDecimal = BigDecimal.valueOf(0.000001);

	public static void assertBigDecimalEquals(BigDecimal expected, BigDecimal actual) {
		BigDecimal diff = expected.subtract(actual).abs();
		Assertions.assertThat(diff)
			.withFailMessage("Expected <%s> but was <%s>", expected, actual)
			.isLessThanOrEqualTo(deltaBigDecimal);
	}
}
