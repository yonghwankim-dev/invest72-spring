package co.invest72.exchange_rate.domain.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import co.invest72.exchange_rate.domain.ExchangeRateRepository;
import co.invest72.exchange_rate.domain.entity.ExchangeRate;
import co.invest72.money.domain.Currency;
import co.invest72.money.domain.CurrencyPair;

class ExchangeRateServiceTest {

	private ExchangeRateService service;
	private ExchangeRateRepository repository;

	@BeforeEach
	void setUp() {
		repository = BDDMockito.mock(ExchangeRateRepository.class);
		service = new ExchangeRateService(repository);
	}

	@DisplayName("환율 저장 - 원달러 환율을 저장한다")
	@Test
	void should_save_exchange_rate_data() {
		// given
		Currency dollar = Currency.dollar();
		BigDecimal rate = BigDecimal.valueOf(1300);
		ExchangeRate exchangeRate = new ExchangeRate(dollar.getCode(), dollar.getName(), rate);
		// when
		service.saveRate(exchangeRate);
		// then
		BDDMockito.verify(repository, Mockito.times(1))
			.save(exchangeRate);
	}

	@DisplayName("환율 조회 - 달러 환율 정보는 존재하지만, 원환율 정보는 존재하지 않으면 최종적으로 빈 Optional을 반환해야 한다")
	@Test
	void should_return_empty_optional_when_exchange_rate_has_dollar_and_not_has_won() {
		// given
		Currency from = Currency.dollar();
		Currency to = Currency.won();
		BigDecimal rate = BigDecimal.ONE;
		ExchangeRate exchangeRate = new ExchangeRate(from.getCode(), from.getName(), rate);

		BDDMockito.given(repository.findByCode(from.getCode()))
			.willReturn(Optional.of(exchangeRate));
		BDDMockito.given(repository.findByCode(to.getCode()))
			.willReturn(Optional.empty());
		// when
		Optional<BigDecimal> result = service.getRate(new CurrencyPair(from, to));
		// then
		Assertions.assertThat(result).isEmpty();
	}

	@DisplayName("환율 조회 - 원화 -> 달러에 대한 환율 조회")
	@Test
	void should_return_won_dollar_rate_when_from_is_won_to_is_dollar() {
		// given
		Currency from = Currency.won();
		Currency to = Currency.dollar();
		ExchangeRate wonExchangeRate = new ExchangeRate(from.getCode(), from.getName(), BigDecimal.ONE);
		BDDMockito.given(repository.findByCode(from.getCode()))
			.willReturn(Optional.of(wonExchangeRate));
		ExchangeRate dollarExchangeRate = new ExchangeRate(to.getCode(), to.getName(), BigDecimal.valueOf(1000));
		BDDMockito.given(repository.findByCode(to.getCode()))
			.willReturn(Optional.of(dollarExchangeRate));
		// when
		Optional<BigDecimal> rate = service.getRate(new CurrencyPair(from, to));
		// then
		Assertions.assertThat(rate)
			.usingValueComparator(BigDecimal::compareTo)
			.contains(BigDecimal.valueOf(0.001));
	}

	@DisplayName("환율 조회 - 통화가 동일한 경우 1이 반환되어야 한다")
	@Test
	void should_return_one_rate_when_same_currency() {
		// given
		Currency won = Currency.won();
		// when
		Optional<BigDecimal> rate = service.getRate(new CurrencyPair(won, won));
		// then
		Assertions.assertThat(rate)
			.usingValueComparator(BigDecimal::compareTo)
			.contains(BigDecimal.ONE);
	}
}
