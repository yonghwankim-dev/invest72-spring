package co.invest72.investment.console;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import co.invest72.investment.application.CalculateExpirationInvestment;
import co.invest72.investment.application.InvestmentFactory;
import co.invest72.investment.application.TaxPercentFormatter;
import co.invest72.investment.console.input.delegator.CalculateExpirationInvestmentReaderDelegator;
import co.invest72.investment.console.input.reader.CalculateInvestmentRequestReader;
import co.invest72.investment.console.input.registry.InvestmentAmountReaderStrategyRegistry;
import co.invest72.investment.console.input.registry.MapBasedInvestmentAmountReaderStrategyRegistry;
import co.invest72.investment.console.input.strategy.FixedDepositAmountReaderStrategy;
import co.invest72.investment.console.input.strategy.InstallmentSavingAmountReaderStrategy;
import co.invest72.investment.console.input.strategy.InvestmentAmountReaderStrategy;
import co.invest72.investment.console.output.InvestmentResultPrinter;
import co.invest72.investment.console.output.PrintStreamBasedInvestmentResultPrinter;
import co.invest72.investment.console.output.guide.BufferedWriterBasedGuidePrinter;
import co.invest72.investment.console.output.guide.GuidePrinter;
import co.invest72.investment.domain.investment.InvestmentType;

class CalculateExpirationInvestmentConsoleRunnerTest {

	private ByteArrayOutputStream outputStream;
	private GuidePrinter guidePrinter;
	private InputStream in;
	private CalculateExpirationInvestmentConsoleRunner runner;
	private CalculateExpirationInvestmentReaderDelegator investmentReaderDelegator;
	private PrintStream err;
	private BufferedReader reader;
	private InvestmentAmountReaderStrategyRegistry amountReaderStrategyRegistry;
	private InvestmentResultPrinter investmentResultPrinter;
	private CalculateInvestmentRequestReader calculateInvestmentRequestReader;
	private CalculateExpirationInvestment investment;

	public static Stream<Arguments> inputFileSource() {
		return Stream.of(
			inputPair("test_input1.txt", "expected_output1.txt"),
			inputPair("test_input2.txt", "expected_output2.txt"),
			inputPair("test_input3.txt", "expected_output3.txt"),
			inputPair("test_input4.txt", "expected_output4.txt")
		);
	}

	private static Arguments inputPair(String inputFileName, String expectedFileName) {
		return Arguments.of(
			toTestFile(inputFileName),
			toTestFile(expectedFileName)
		);
	}

	private static File toTestFile(String fileName) {
		return new File("src/test/resources/" + fileName);
	}

	private String getExpectedFileContent(File file) {
		try (BufferedReader expectedReader = new BufferedReader(
			new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
			return expectedReader.lines()
				.collect(Collectors.joining(System.lineSeparator(), "", System.lineSeparator()));
		} catch (IOException e) {
			throw new IllegalArgumentException("파일을 읽는 중 오류 발생: " + file, e);
		}
	}

	@BeforeEach
	void setUp() {
		PrintStream out = System.out;
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(out);
		BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
		in = System.in;
		outputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(outputStream);
		err = System.err;
		guidePrinter = new BufferedWriterBasedGuidePrinter(bufferedWriter, err);
		reader = new BufferedReader(new InputStreamReader(in));
		Map<InvestmentType, InvestmentAmountReaderStrategy> amountReaderStrategies = Map.of(
			InvestmentType.FIXED_DEPOSIT, new FixedDepositAmountReaderStrategy(guidePrinter),
			InvestmentType.INSTALLMENT_SAVING, new InstallmentSavingAmountReaderStrategy(guidePrinter)
		);
		amountReaderStrategyRegistry = new MapBasedInvestmentAmountReaderStrategyRegistry(amountReaderStrategies);
		calculateInvestmentRequestReader = new CalculateInvestmentRequestReader(reader, guidePrinter);
		investmentReaderDelegator = new CalculateExpirationInvestmentReaderDelegator(amountReaderStrategyRegistry,
			calculateInvestmentRequestReader);
		investmentResultPrinter = new PrintStreamBasedInvestmentResultPrinter(printStream);
		InvestmentFactory factory = new InvestmentFactory();
		investment = new CalculateExpirationInvestment(factory, new TaxPercentFormatter());
		runner = new CalculateExpirationInvestmentConsoleRunner(
			err,
			investmentReaderDelegator,
			investmentResultPrinter,
			investment
		);
	}

	private void assertOutput(String expected, String output) {
		assertEquals(expected, output);
	}

	@Test
	void created() {
		assertNotNull(runner);
	}

	@ParameterizedTest
	@MethodSource(value = "inputFileSource")
	void shouldPrintAmount(File inputFile, File expectedFile) throws FileNotFoundException {
		in = new FileInputStream(inputFile);
		reader = new BufferedReader(new InputStreamReader(in));
		calculateInvestmentRequestReader = new CalculateInvestmentRequestReader(reader, guidePrinter);
		investmentReaderDelegator = new CalculateExpirationInvestmentReaderDelegator(amountReaderStrategyRegistry,
			calculateInvestmentRequestReader);
		runner = new CalculateExpirationInvestmentConsoleRunner(
			err,
			investmentReaderDelegator,
			investmentResultPrinter,
			investment
		);

		runner.run();

		String output = outputStream.toString(StandardCharsets.UTF_8);
		String expected = getExpectedFileContent(expectedFile);
		assertOutput(expected, output);
	}
}
