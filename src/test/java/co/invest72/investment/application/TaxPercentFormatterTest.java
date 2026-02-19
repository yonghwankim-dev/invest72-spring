package co.invest72.investment.application;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class TaxPercentFormatterTest {

	@Test
	void canCreated() {
		TaxFormatter formatter = new TaxPercentFormatter();

		Assertions.assertThat(formatter).isNotNull();
	}

	@ParameterizedTest
	@MethodSource(value = "source.TestDataProvider#getTaxPercentFormatSource")
	void format(double value, String expected) {
		TaxFormatter formatter = new TaxPercentFormatter();

		String formatted = formatter.format(value);

		Assertions.assertThat(formatted).isEqualTo(expected);
	}

}
