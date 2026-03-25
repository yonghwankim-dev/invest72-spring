package co.invest72.investment.console.output.guide;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BufferedWriterBasedGuidePrinterTest {

	private OutputStream outputStream;
	private GuidePrinter guidePrinter;

	@BeforeEach
	void setUp() {
		outputStream = new ByteArrayOutputStream();
		OutputStreamWriter writer = new OutputStreamWriter(outputStream);
		BufferedWriter bufferedWriter = new BufferedWriter(writer);
		PrintStream err = System.err;
		guidePrinter = new BufferedWriterBasedGuidePrinter(bufferedWriter, err);
	}

	@Test
	void created() {
		assertNotNull(guidePrinter);
	}

	@Test
	void shouldPrintFixedDepositAmountInputGuide() {
		guidePrinter.printFixedDepositAmountInputGuide();

		String output = outputStream.toString();
		assertTrue(output.contains("예치 금액(원)을 입력하세요"));
	}

	@Test
	void shouldPrintInstallmentInvestmentInputGuide() {
		guidePrinter.printInstallmentInvestmentInputGuide();

		String output = outputStream.toString();
		String expectedOutput = "\uD83D\uDCB0 투자 기간 단위와 금액을 한 줄로 입력해주세요.\n"
			+ "\n"
			+ "\uD83D\uDCDD 형식:\n"
			+ "[단위] [투자금액]\n"
			+ "\n"
			+ "\uD83D\uDCCC 단위 예시:\n"
			+ "- \"월\" → 적금 (매월 납입 금액)\n"
			+ "- \"년\" → 적금 (매년 납입 금액)\n"
			+ "\n"
			+ "\uD83D\uDCCC 예시 입력:\n"
			+ "- 월 1000000\n"
			+ "- 년 5000000\n"
			+ "\n"
			+ "\uD83D\uDC49 입력: \n";
		assertEquals(expectedOutput, output);
	}

	@Test
	void shouldPrintInvestmentTypeInputGuide() {
		guidePrinter.printInvestmentTypeInputGuide();

		String output = outputStream.toString();

		assertEquals("투자 유형을 입력하세요 (DEPOSIT or SAVINTS): ", output);
	}

	@Test
	void shouldPrintPeriodTypeInputGuide() {
		guidePrinter.printPeriodTypeInputGuide();

		String output = outputStream.toString();
		assertTrue(output.contains("기간 종류를 입력하세요 (월 or 년): "));
	}

	@Test
	void shouldPrintPeriodInputGuide() {
		guidePrinter.printPeriodInputGuide();

		String output = outputStream.toString();
		assertTrue(output.contains("기간을 입력하세요 (숫자): "));
	}

	@Test
	void shouldPrintInterestTypeInputGuide() {
		guidePrinter.printInterestTypeInputGuide();

		String output = outputStream.toString();
		assertTrue(output.contains("이자 방식을 입력하세요 (단리 or 복리): "));
	}

	@Test
	void shouldPrintInterestRatePercentInputGuide() {
		guidePrinter.printInterestPercentInputGuide();

		String output = outputStream.toString();
		assertTrue(output.contains("이자율을 입력하세요 (%): "));
	}

	@Test
	void shouldPrintTaxTypeInputGuide() {
		guidePrinter.printTaxTypeInputGuide();

		String output = outputStream.toString();
		assertTrue(output.contains("과세 유형을 입력하세요 (일반과세, 비과세, 세금우대): "));
	}

	@Test
	void shouldPrintTaxRateInputGuide() {
		guidePrinter.printTaxRateInputGuide();

		String output = outputStream.toString();
		assertTrue(output.contains("세율을 입력하세요 (세금우대형일 경우 %, 아니면 0): "));
	}
}
