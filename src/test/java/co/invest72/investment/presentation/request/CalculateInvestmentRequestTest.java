package co.invest72.investment.presentation.request;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import co.invest72.investment.domain.amount.AmountType;
import co.invest72.money.domain.Currency;

class CalculateInvestmentRequestTest {

	@Test
	void canCreated() {
		CalculateInvestmentRequest request = CalculateInvestmentRequest.builder()
			.type("예금")
			.amountType(AmountType.ONE_TIME.getDescription())
			.amount(1_000_000)
			.periodType("년")
			.periodValue(1)
			.interestType("단리")
			.annualInterestRate(0.05)
			.taxType("일반과세")
			.taxRate(0.154)
			.currencyCode(Currency.won().getCode())
			.build();

		Assertions.assertThat(request).isNotNull();
	}

	@Test
	void newInstance_whenInvalidValue_thenThrowException() {
		Throwable throwable = Assertions.catchThrowable(() -> CalculateInvestmentRequest.builder().build());

		Assertions.assertThat(throwable)
			.isInstanceOf(NullPointerException.class);
	}
}
