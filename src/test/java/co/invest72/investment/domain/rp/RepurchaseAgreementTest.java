package co.invest72.investment.domain.rp;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RepurchaseAgreementTest {

	@Test
	@DisplayName("약정형 RP 객체 생성")
	void create_instance() {
		RepurchaseAgreement rp = new TermRepurchaseAgreement();

		Assertions.assertThat(rp)
			.isNotNull()
			.isInstanceOf(TermRepurchaseAgreement.class);
	}

	@Test
	@DisplayName("RP 상품의 만기일자를 계산한다")
	void should_calculate_expiration_date_correctly_when_months_given() {
		// given
		RepurchaseAgreement rp = new TermRepurchaseAgreement();
		LocalDate startDate = LocalDate.of(2026, 9, 11);
		int days = 30;

		// when
		LocalDate expirationDate = rp.calculateExpirationDate(startDate, days);

		// then
		LocalDate expected = LocalDate.of(2026, 10, 11);
		Assertions.assertThat(expirationDate).isEqualTo(expected);
	}

}
