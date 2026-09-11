package co.invest72.investment.domain.rp;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class RepurchaseAgreementTest {

	private RepurchaseAgreement rp;

	@BeforeEach
	void setUp() {
		LocalDate startDate = LocalDate.of(2026, 9, 11);
		rp = new TermRepurchaseAgreement(startDate);
	}

	@Test
	@DisplayName("약정형 RP 객체 생성")
	void create_instance() {
		Assertions.assertThat(rp)
			.isNotNull()
			.isInstanceOf(TermRepurchaseAgreement.class);
	}

	@Test
	@DisplayName("RP 상품의 만기일자를 계산한다")
	void should_calculate_expiration_date_correctly_when_months_given() {
		// given
		LocalDate startDate = LocalDate.of(2026, 9, 11);
		int days = 30;

		// when
		LocalDate expirationDate = rp.calculateExpirationDate(startDate, days);

		// then
		LocalDate expected = LocalDate.of(2026, 10, 11);
		Assertions.assertThat(expirationDate).isEqualTo(expected);
	}

	@ParameterizedTest
	@DisplayName("일수(days)가 0이하이면 시작일자를 반환한다.")
	@ValueSource(ints = {-1, 0})
	void should_return_start_date_when_days_is_zero_or_negative(int days) {
		// given
		LocalDate startDate = LocalDate.of(2026, 9, 11);

		// when
		LocalDate expirationDate = rp.calculateExpirationDate(startDate, days);

		// then
		Assertions.assertThat(expirationDate).isEqualTo(startDate);
	}
}
