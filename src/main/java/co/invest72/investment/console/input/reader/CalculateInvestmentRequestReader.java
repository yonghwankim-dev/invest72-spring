package co.invest72.investment.console.input.reader;

import java.io.BufferedReader;
import java.io.IOException;

import co.invest72.investment.console.output.guide.GuidePrinter;

public class CalculateInvestmentRequestReader implements InvestmentAmountReader {

	private final BufferedReader reader;
	private final GuidePrinter guidePrinter;

	public CalculateInvestmentRequestReader(BufferedReader reader, GuidePrinter guidePrinter) {
		this.reader = reader;
		this.guidePrinter = guidePrinter;
	}

	public String readInvestmentType() throws IOException {
		guidePrinter.printInvestmentTypeInputGuide();
		return reader.readLine();
	}

	public String readPeriodType() throws IOException {
		guidePrinter.printPeriodTypeInputGuide();
		return reader.readLine();
	}

	public int readPeriod() throws IOException {
		guidePrinter.printPeriodInputGuide();
		return Integer.parseInt(reader.readLine());
	}

	public String readInterestType() throws IOException {
		guidePrinter.printInterestTypeInputGuide();
		return reader.readLine();
	}

	public double readInterestRate() throws IOException {
		guidePrinter.printInterestPercentInputGuide();
		int percent = Integer.parseInt(reader.readLine());
		return toRate(percent);
	}

	private double toRate(double value) {
		return value / 100.0;
	}

	public String readTaxType() throws IOException {
		guidePrinter.printTaxTypeInputGuide();
		return reader.readLine();
	}

	public double readTaxRate() throws IOException {
		guidePrinter.printTaxRateInputGuide();
		return toRate(Double.parseDouble(reader.readLine()));
	}

	public String readAmount() throws IOException {
		return reader.readLine();
	}

	public String readCurrency() throws IOException {
		return reader.readLine();
	}
}
