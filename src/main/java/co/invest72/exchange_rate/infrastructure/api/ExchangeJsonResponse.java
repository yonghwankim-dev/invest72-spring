package co.invest72.exchange_rate.infrastructure.api;

import java.math.BigDecimal;
import java.util.Objects;

import lombok.Getter;

@Getter
public class ExchangeJsonResponse {
	private final Integer result; // 결과
	private final String currencyUnit; // 통화코드
	private final BigDecimal dealingBaseRate; // 매매기준율
	private final String name; // 통화 이름

	public ExchangeJsonResponse(Integer result, String currencyUnit, String dealingBaseRate, String name) {
		this.result = Objects.requireNonNull(result);
		this.currencyUnit = Objects.requireNonNull(currencyUnit);
		this.dealingBaseRate = new BigDecimal(dealingBaseRate.replace(",", ""));
		this.name = Objects.requireNonNull(name);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof ExchangeJsonResponse that))
			return false;
		return Objects.equals(result, that.result) && Objects.equals(currencyUnit, that.currencyUnit)
			&& Objects.equals(dealingBaseRate, that.dealingBaseRate) && Objects.equals(name, that.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(result, currencyUnit, dealingBaseRate, name);
	}

	@Override
	public String toString() {
		return "ExchangeJsonResponse{" +
			"result=" + result +
			", currencyUnit='" + currencyUnit + '\'' +
			", dealingBaseRate=" + dealingBaseRate +
			", name='" + name + '\'' +
			'}';
	}
}
