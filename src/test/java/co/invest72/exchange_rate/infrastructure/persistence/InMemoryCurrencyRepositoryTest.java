package co.invest72.exchange_rate.infrastructure.persistence;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import co.invest72.exchange_rate.domain.Currency;
import co.invest72.exchange_rate.domain.CurrencyRepository;

class InMemoryCurrencyRepositoryTest {

	private CurrencyRepository repository;

	@BeforeEach
	void setUp() {
		repository = new InMemoryCurrencyRepository();
	}

	@DisplayName("통화 저장")
	@Test
	void save() {
		// given
		Currency dollar = Currency.dollar();
		// when
		repository.save(dollar);
		// then
		Assertions.assertThat(repository.findByCode("USD")).contains(dollar);
	}

	@DisplayName("특정 통화 조회")
	@Test
	void findByCode() {
		// given
		Currency jpy = Currency.of("JPY", "¥", "엔화");
		// when
		repository.save(jpy);

		Assertions.assertThat(repository.findByCode("JPY")).contains(jpy);
	}

	@DisplayName("통화 전체 조회")
	@Test
	void findAll() {
		// given
		Currency jpy = Currency.of("JPY", "¥", "엔화");
		repository.save(jpy);
		// when
		List<Currency> currencies = repository.findAll();
		// then
		Assertions.assertThat(currencies).hasSize(3);
	}
}
