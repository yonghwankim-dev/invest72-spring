package co.invest72.exchange_rate.application;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import co.invest72.exchange_rate.domain.ExchangeRateRepository;
import co.invest72.exchange_rate.domain.entity.ExchangeRate;
import co.invest72.exchange_rate.domain.service.ExchangeRateService;
import co.invest72.exchange_rate.infrastructure.api.ExchangeJsonResponse;
import co.invest72.exchange_rate.infrastructure.persistence.InMemoryExchangeRateRepository;
import co.invest72.money.domain.Currency;
import co.invest72.money.domain.CurrencyPair;

class ExchangeRateUpdateHandlerTest {

	@DisplayName("환율 업데이트 - 100단위 외화 API 응답 시 정규화된 1단위 환율 저장 및 원/외화 상호 변환이 정확해야 한다")
	@Test
	void updateRates() {
		// given
		ExchangeRateRepository repository = new InMemoryExchangeRateRepository();
		ExchangeRateService service = new ExchangeRateService(repository);
		// 이벤트 캡처 위해서 ArgumentCaptor 사용
		ExchangeRateUpdateHandler handler = new ExchangeRateUpdateHandler(service);
		// 외화 1단위당 원화 몇원
		ExchangeJsonResponse response = new ExchangeJsonResponse(1, "JPY(100)", "9.1234", "일본 엔화");
		// when
		handler.handleUpdateRates(response);
		// then
		Assertions.assertThat(
				service.getRate(new CurrencyPair(Currency.won(), Currency.jpy())).orElseThrow())
			.isEqualByComparingTo(new BigDecimal("10.9608260078"));
		Assertions.assertThat(service.getRate(new CurrencyPair(Currency.jpy(), Currency.won())).orElseThrow())
			.isEqualByComparingTo(new BigDecimal("0.091234"));

		BigDecimal wonAmount = BigDecimal.valueOf(1000);
		BigDecimal yenResult = wonAmount.multiply(
				service.getRate(new CurrencyPair(Currency.jpy(), Currency.won())).orElseThrow())
			.setScale(2, RoundingMode.HALF_EVEN);
		Assertions.assertThat(yenResult).isEqualTo(new BigDecimal("91.23"));

		BigDecimal yenAmount = new BigDecimal("91.23");
		BigDecimal wonResult = yenAmount.multiply(
				service.getRate(new CurrencyPair(Currency.won(), Currency.jpy())).orElseThrow())
			.setScale(2, RoundingMode.HALF_EVEN);
		Assertions.assertThat(wonResult).isEqualTo(new BigDecimal("999.96"));
	}

	@DisplayName("환율 업데이트 - DB 저장이 실패하면 캐시도 저장되지 않아야 한다")
	@Test
	void updateRates_whenFailSaveRate_thenNotSaveCache() {
		// given
		ExchangeRateRepository repository = Mockito.mock(ExchangeRateRepository.class);
		ExchangeRateService service = new ExchangeRateService(repository);
		// 환율 저장 무조건 실패
		BDDMockito.willThrow(RuntimeException.class)
			.given(repository)
			.save(ArgumentMatchers.any(ExchangeRate.class));
		ExchangeRateUpdateHandler handler = new ExchangeRateUpdateHandler(service);
		ExchangeJsonResponse response = new ExchangeJsonResponse(1, "JPY(100)", "9.1234", "일본 엔화");
		// when
		Throwable throwable = Assertions.catchThrowable(() -> handler.handleUpdateRates(response));

		// then
		Assertions.assertThat(throwable)
			.isInstanceOf(RuntimeException.class);
	}

	@DisplayName("환율 업데이트 - DB 저장은 성공했지만, 캐시 업데이트에 실패하는 경우")
	@Test
	void updateRates_whenSuccessDBAndFailCacheWork() {
		// given
		ExchangeRateRepository repository = new InMemoryExchangeRateRepository();
		ExchangeRateService service = new ExchangeRateService(repository);
		// 이벤트 캡처 위해서 ArgumentCaptor 사용
		ExchangeRateUpdateHandler handler = new ExchangeRateUpdateHandler(service);
		// 외화 1단위당 원화 몇원
		ExchangeJsonResponse response = new ExchangeJsonResponse(1, "JPY(100)", "9.1234", "일본 엔화");
		// when
		handler.handleUpdateRates(response);
		// then
		Assertions.assertThat(
				service.getRate(new CurrencyPair(Currency.won(), Currency.jpy())).orElseThrow())
			.isEqualByComparingTo(new BigDecimal("10.9608260078"));
		Assertions.assertThat(service.getRate(new CurrencyPair(Currency.jpy(), Currency.won())).orElseThrow())
			.isEqualByComparingTo(new BigDecimal("0.091234"));

		BigDecimal wonAmount = BigDecimal.valueOf(1000);
		BigDecimal yenResult = wonAmount.multiply(
				service.getRate(new CurrencyPair(Currency.jpy(), Currency.won())).orElseThrow())
			.setScale(2, RoundingMode.HALF_EVEN);
		Assertions.assertThat(yenResult).isEqualTo(new BigDecimal("91.23"));

		BigDecimal yenAmount = new BigDecimal("91.23");
		BigDecimal wonResult = yenAmount.multiply(
				service.getRate(new CurrencyPair(Currency.won(), Currency.jpy())).orElseThrow())
			.setScale(2, RoundingMode.HALF_EVEN);
		Assertions.assertThat(wonResult).isEqualTo(new BigDecimal("999.96"));
	}

}
