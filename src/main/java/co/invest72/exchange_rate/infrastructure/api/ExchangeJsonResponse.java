package co.invest72.exchange_rate.infrastructure.api;

import java.math.BigDecimal;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchangeJsonResponse {
	private final Integer result; // 결과
	private final String code; // 통화코드
	private final BigDecimal dealingBaseRate; // 매매기준율
	private final String name; // 통화 이름

	@JsonCreator
	public ExchangeJsonResponse(
		@JsonProperty("result") Integer result,
		@JsonProperty("cur_unit") String code,
		@JsonProperty("deal_bas_r") String dealingBaseRate,
		@JsonProperty("cur_nm") String name) {
		this.result = Objects.requireNonNull(result);
		this.code = Objects.requireNonNull(code);
		this.dealingBaseRate = new BigDecimal(Objects.requireNonNull(dealingBaseRate).replace(",", ""));
		this.name = Objects.requireNonNull(name);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof ExchangeJsonResponse that))
			return false;
		return Objects.equals(result, that.result) && Objects.equals(code, that.code)
			&& Objects.equals(dealingBaseRate, that.dealingBaseRate) && Objects.equals(name, that.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(result, code, dealingBaseRate, name);
	}

	@Override
	public String toString() {
		return "ExchangeJsonResponse{" +
			"result=" + result +
			", currencyUnit='" + code + '\'' +
			", dealingBaseRate=" + dealingBaseRate +
			", name='" + name + '\'' +
			'}';
	}
}
