package co.invest72.exchange_rate.application;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import co.invest72.exchange_rate.domain.entity.ExchangeRate;
import co.invest72.exchange_rate.domain.service.ExchangeRateService;
import co.invest72.exchange_rate.infrastructure.api.ExchangeJsonResponse;

class ExchangeRateUpdateHandlerTest {

	private ExchangeRateService service;

	@BeforeEach
	void setUp() {
		service = BDDMockito.mock(ExchangeRateService.class);
	}

	@DisplayName("환율 업데이트 - 100단위 외화 API 응답 시 정규화된 1단위 환율 저장 및 원/외화 상호 변환이 정확해야 한다")
	@Test
	void updateRates() {
		// given
		ExchangeRateUpdateHandler handler = new ExchangeRateUpdateHandler(service);
		// 외화 1단위당 원화 몇원
		ExchangeJsonResponse response = new ExchangeJsonResponse(1, "JPY(100)", "9.1234", "일본 엔화");
		// when
		handler.handleUpdateRates(response);
		// then
		ExchangeRate exchangeRate = new ExchangeRate("JPY", "일본 엔", new BigDecimal("0.091234"));
		BDDMockito.verify(service, Mockito.times(1))
			.saveRate(exchangeRate);
	}
}
