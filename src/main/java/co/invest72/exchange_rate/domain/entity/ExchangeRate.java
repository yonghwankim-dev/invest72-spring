package co.invest72.exchange_rate.domain.entity;

import java.math.BigDecimal;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ExchangeRate {
	@Id
	private String currencyCode; // 통화코드
	@Column(name = "currency_name", nullable = false)
	private String currencyName; // 통화명
	@Column(name = "basic_rate_of_exchange", nullable = false)
	private BigDecimal basicRateOfExchange; // 매매기준율

	public ExchangeRate(String currencyCode, String currencyName, BigDecimal basicRateOfExchange) {
		this.currencyCode = Objects.requireNonNull(currencyCode);
		this.currencyName = Objects.requireNonNull(currencyName);
		this.basicRateOfExchange = Objects.requireNonNull(basicRateOfExchange);
	}
}
