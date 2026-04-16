package co.invest72.exchange_rate.application;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import co.invest72.exchange_rate.domain.ExchangeRateProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExchangeRateInitializer {
	private final ExchangeRateProvider provider;

	@EventListener(ApplicationReadyEvent.class)
	public void init() {
		log.info("애플리케이션 시작: 초기 환율 데이터를 로드합니다.");
		provider.updateRates().subscribe();
	}
}
