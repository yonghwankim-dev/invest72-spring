package co.invest72.exchange_rate.application;

import java.math.BigDecimal;
import java.util.Objects;

import org.springframework.context.ApplicationEvent;

import co.invest72.money.domain.Currency;
import lombok.Getter;

@Getter
public class ExchangeRateUpdateEvent extends ApplicationEvent {
	private final Currency from;
	private final Currency to;
	private final BigDecimal rate;

	public ExchangeRateUpdateEvent(Currency from, Currency to, BigDecimal rate) {
		super(System.currentTimeMillis());
		this.from = Objects.requireNonNull(from);
		this.to = Objects.requireNonNull(to);
		this.rate = Objects.requireNonNull(rate);
	}
}
