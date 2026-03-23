package source;

import java.math.BigDecimal;
import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;

public class ProductTaxRateDataProvider {

	public static Stream<Arguments> provideTaxRateValues() {
		BigDecimal pivot = BigDecimal.valueOf(0.154);
		return Stream.of(
			Arguments.of(pivot, BigDecimal.valueOf(0.154)),
			Arguments.of(pivot, BigDecimal.valueOf(0.1540)),
			Arguments.of(pivot, BigDecimal.valueOf(0.15401)),
			Arguments.of(pivot, BigDecimal.valueOf(0.15405))
		);
	}

	public static Stream<Arguments> provideDiffTaxRateValue() {
		BigDecimal pivot = BigDecimal.valueOf(0.154);
		return Stream.of(
			Arguments.of(pivot, BigDecimal.valueOf(0.15406)),
			Arguments.of(pivot, BigDecimal.valueOf(0.15407)),
			Arguments.of(pivot, BigDecimal.valueOf(0.15408)),
			Arguments.of(pivot, BigDecimal.valueOf(0.15409))
		);
	}
}
