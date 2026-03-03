package co.invest72.investment.domain.tax;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import co.invest72.investment.domain.TaxRate;

class FixedTaxRateTest {

	private TaxRate fixedTaxRate;

	public static Stream<Arguments> invalidRates() {
		return Stream.of(
			Arguments.of(-0.1),
			Arguments.of(1.0),
			Arguments.of(1.5)
		);
	}

	@BeforeEach
	void setUp() {
		fixedTaxRate = new FixedTaxRate(0.154);
	}

	@Test
	void created() {
		assertNotNull(fixedTaxRate);
	}

	@ParameterizedTest
	@MethodSource(value = "invalidRates")
	void shouldThrowException_whenRateIsOutOfValidRange(double rate) {
		assertThrows(IllegalArgumentException.class, () -> new FixedTaxRate(rate));
	}

	@Test
	void shouldReturnTaxAmount_whenApplyToAmount() {
		int amount = 1000;

		int taxAmount = fixedTaxRate.applyTo(amount);

		int expectedTaxAmount = 154;
		assertEquals(expectedTaxAmount, taxAmount);
	}

	@Test
	void shouldReturnRate_whenGetRate() {
		BigDecimal value = fixedTaxRate.getValue();

		double expectedRate = 0.154;
		assertEquals(BigDecimal.valueOf(expectedRate), value);
	}
}
