package co.invest72.exchange_rate.application;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import co.invest72.exchange_rate.domain.ExchangeRateProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExchangeRateScheduler {
	private final ExchangeRateProvider provider;

	/**
	 * 환율 업데이트 스케줄링
	 * <p>
	 * 매일 평일 11시부터 18시까지 1시간마다 업데이트
	 * </p>
	 */
	@Scheduled(cron = "0 0 11-18 * * MON-FRI")
	public void scheduleUpdate() {
		provider.updateRates()
			.subscribe(
				response -> log.info("환율 업데이트 성공 : {}", response.getCode()),
				error -> log.error("환율 업데이트 실패", error),
				() -> log.info("모든 환율 업데이트 완료"));
	}
}
