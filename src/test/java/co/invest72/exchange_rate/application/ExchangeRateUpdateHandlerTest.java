package co.invest72.exchange_rate.application;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.context.ApplicationEventPublisher;

import co.invest72.exchange_rate.domain.ExchangeRateRepository;
import co.invest72.exchange_rate.domain.service.ExchangeRateService;
import co.invest72.exchange_rate.infrastructure.api.ExchangeJsonResponse;
import co.invest72.exchange_rate.infrastructure.persistence.InMemoryExchangeRateRepository;
import co.invest72.money.domain.Currency;
import co.invest72.money.domain.CurrencyPair;

class ExchangeRateUpdateHandlerTest {

	@DisplayName("환율 업데이트 - 원-엔 환율이 9.1234이면 환율은 0.091234여야 한다")
	@Test
	void updateRates() {
		// given
		ExchangeRateService service = new ExchangeRateService();
		ExchangeRateRepository repository = new InMemoryExchangeRateRepository();
		ApplicationEventPublisher eventPublisher = BDDMockito.mock(ApplicationEventPublisher.class);
		ExchangeRateUpdateHandler handler = new ExchangeRateUpdateHandler(service, repository, eventPublisher);
		// 외화 1단위당 원화 몇원
		ExchangeJsonResponse response = new ExchangeJsonResponse(1, "JPY(100)", "9.1234", "일본 엔화");
		// when
		handler.handleUpdateRates(response);
		// then
		Assertions.assertThat(service.getRate(new CurrencyPair(Currency.won(), Currency.from("JPY"))).orElseThrow())
			.isEqualByComparingTo(new BigDecimal("10.9608260078"));
		Assertions.assertThat(service.getRate(new CurrencyPair(Currency.from("JPY"), Currency.won())).orElseThrow())
			.isEqualByComparingTo(new BigDecimal("0.091234"));

		BigDecimal wonAmount = BigDecimal.valueOf(1000);
		BigDecimal yenResult = wonAmount.multiply(
				service.getRate(new CurrencyPair(Currency.from("JPY"), Currency.won())).orElseThrow())
			.setScale(2, RoundingMode.HALF_EVEN);
		Assertions.assertThat(yenResult).isEqualTo(new BigDecimal("91.23"));

		BigDecimal yenAmount = new BigDecimal("91.23");
		BigDecimal wonResult = yenAmount.multiply(
				service.getRate(new CurrencyPair(Currency.won(), Currency.from("JPY"))).orElseThrow())
			.setScale(2, RoundingMode.HALF_EVEN);
		Assertions.assertThat(wonResult).isEqualTo(new BigDecimal("999.96"));
	}
}
