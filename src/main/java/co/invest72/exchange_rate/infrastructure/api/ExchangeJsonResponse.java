package co.invest72.exchange_rate.infrastructure.api;

import java.util.Objects;

import lombok.Getter;

@Getter
public class ExchangeJsonResponse {
	private final Integer result; // 결과
	private final String currencyUnit; // 통화코드
	private final String dealingBaseRate; // 매매기준율

	public ExchangeJsonResponse(Integer result, String currencyUnit, String dealingBaseRate) {
		this.result = Objects.requireNonNull(result);
		this.currencyUnit = Objects.requireNonNull(currencyUnit);
		this.dealingBaseRate = Objects.requireNonNull(dealingBaseRate);
	}
}
