package co.invest72.exchange_rate.infrastructure.persistence;

import java.math.BigDecimal;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import co.invest72.money.domain.Currency;
import co.invest72.exchange_rate.domain.ExchangeRateRepository;
import co.invest72.exchange_rate.domain.entity.ExchangeRate;

class InMemoryExchangeRateRepositoryTest {

	private ExchangeRateRepository repository;

	@BeforeEach
	void setUp() {
		repository = new InMemoryExchangeRateRepository();
	}

	@DisplayName("통화 저장")
	@Test
	void save() {
		// given
		Currency dollar = Currency.dollar();
		ExchangeRate exchangeRate = new ExchangeRate(dollar.getCode(), dollar.getName(), BigDecimal.valueOf(1000));
		// when
		repository.save(exchangeRate);
		// then
		Assertions.assertThat(repository.findByCode("USD")).contains(exchangeRate);
	}

	@DisplayName("특정 통화 조회")
	@Test
	void findByCode() {
		// given
		Currency jpy = Currency.of("JPY", "엔화");
		ExchangeRate exchangeRate = new ExchangeRate(jpy.getCode(), jpy.getName(), BigDecimal.valueOf(9.5105));

		// when
		repository.save(exchangeRate);

		Assertions.assertThat(repository.findByCode("JPY")).contains(exchangeRate);
	}

	@DisplayName("통화 전체 조회")
	@Test
	void findAll() {
		// given
		Currency jpy = Currency.of("JPY", "엔화");
		ExchangeRate exchangeRate = new ExchangeRate(jpy.getCode(), jpy.getName(), BigDecimal.valueOf(9.5105));
		repository.save(exchangeRate);
		// when
		List<ExchangeRate> currencies = repository.findAll();
		// then
		Assertions.assertThat(currencies).hasSize(3);
	}
}
