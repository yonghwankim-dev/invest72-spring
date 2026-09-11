package co.invest72.investment.domain.rp;

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
}
