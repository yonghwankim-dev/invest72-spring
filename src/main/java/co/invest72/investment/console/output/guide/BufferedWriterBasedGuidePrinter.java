package co.invest72.investment.console.output.guide;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintStream;

public class BufferedWriterBasedGuidePrinter implements GuidePrinter {

	private final BufferedWriter writer;
	private final PrintStream err;

	public BufferedWriterBasedGuidePrinter(BufferedWriter writer) {
		this(writer, System.err);
	}

	public BufferedWriterBasedGuidePrinter(BufferedWriter writer, PrintStream err) {
		this.writer = writer;
		this.err = err;
	}

	@Override
	public void printFixedDepositAmountInputGuide() {
		String text = "예치 금액(원)을 입력하세요: ";
		write(text);
	}

	private void write(String text) {
		try {
			writer.write(text);
		} catch (IOException e) {
			err.println("Failed to write guide message: " + e.getMessage());
		} finally {
			try {
				writer.flush();
			} catch (IOException e) {
				err.println("Failed to flush writer: " + e.getMessage());
			}
		}
	}

	@Override
	public void printInstallmentInvestmentInputGuide() {
		String text = getInstallmentInvestmentInputGuide();
		write(text);
	}

	private String getInstallmentInvestmentInputGuide() {
		return "\uD83D\uDCB0 투자 기간 단위와 금액을 한 줄로 입력해주세요.\n"
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
	}

	@Override
	public void printInvestmentTypeInputGuide() {
		String text = "투자 유형을 입력하세요 (DEPOSIT or SAVINTS): ";
		write(text);
	}

	@Override
	public void printPeriodTypeInputGuide() {
		String text = "기간 종류를 입력하세요 (월 or 년): ";
		write(text);
	}

	@Override
	public void printPeriodInputGuide() {
		String text = "기간을 입력하세요 (숫자): ";
		write(text);
	}

	@Override
	public void printInterestTypeInputGuide() {
		String text = "이자 방식을 입력하세요 (단리 or 복리): ";
		write(text);
	}

	@Override
	public void printInterestPercentInputGuide() {
		String text = "이자율을 입력하세요 (%): ";
		write(text);
	}

	@Override
	public void printTaxTypeInputGuide() {
		String text = "과세 유형을 입력하세요 (일반과세, 비과세, 세금우대): ";
		write(text);
	}

	@Override
	public void printTaxRateInputGuide() {
		String text = "세율을 입력하세요 (세금우대형일 경우 %, 아니면 0): ";
		write(text);
	}

	@Override
	public void printTargetAmountInputGuide() {
		String text = "목표 금액을 입력하세요 (예: 10000000): ";
		write(text);
	}

	@Override
	public void printMonthlyInvestmentInputGuide() {
		String text = "월 투자 금액을 입력하세요 (예: 1000000): ";
		write(text);
	}
}
