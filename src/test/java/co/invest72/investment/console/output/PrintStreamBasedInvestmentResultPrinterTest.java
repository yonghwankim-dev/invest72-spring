package co.invest72.investment.console.output;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PrintStreamBasedInvestmentResultPrinterTest {

	private InvestmentResultPrinter printer;
	private OutputStream outputStream;

	public static Stream<Arguments> taxSource() {
		return Stream.of(
			Arguments.of(0, "0"),
			Arguments.of(-1, "-1"),
			Arguments.of(50_823, "-50,823"),
			Arguments.of(100_000, "-100,000")
		);
	}

	@BeforeEach
	void setUp() {
		outputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(outputStream);
		printer = new PrintStreamBasedInvestmentResultPrinter(printStream);
	}

	@Test
	void printTotalPrincipal() {
		printer.printTotalPrincipal(BigDecimal.valueOf(12_000_000));

		String output = outputStream.toString();
		Assertions.assertEquals("원금 합계: 12,000,000원\n", output);
	}

	@Test
	void printInterest() {
		printer.printInterest(BigDecimal.valueOf(330_017));

		String output = outputStream.toString();
		Assertions.assertEquals("세전 이자: 330,017원\n", output);
	}

	@ParameterizedTest
	@MethodSource(value = "taxSource")
	void printTax_shouldMinus_whenTaxIsPositive(int amount, String expectedFormattedAmount) {
		printer.printTax(BigDecimal.valueOf(amount));

		String output = outputStream.toString();
		Assertions.assertEquals("이자 과세: " + expectedFormattedAmount + "원\n", output);
	}

	@Test
	void printTotalProfit() {
		printer.printTotalProfit(BigDecimal.valueOf(1_051_162));

		String output = outputStream.toString();
		Assertions.assertEquals("세후 수령액: 1,051,162원\n", output);
	}
}
