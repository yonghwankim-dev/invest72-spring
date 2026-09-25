package co.invest72.rp.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import co.invest72.investment.domain.DailyInvestPeriod;
import co.invest72.investment.domain.InterestRate;
import co.invest72.investment.domain.InvestPeriod;
import co.invest72.investment.domain.InvestmentAmount;
import co.invest72.investment.domain.amount.FixedDepositAmount;
import co.invest72.investment.domain.interest.AnnualInterestRate;
import co.invest72.investment.domain.period.MonthlyInvestPeriod;
import co.invest72.investment.domain.period.YearlyInvestPeriod;
import co.invest72.money.domain.Money;

class TermRepurchaseAgreementTest {

	private RepurchaseAgreement rp;

	@BeforeEach
	void setUp() {
		InvestmentAmount investmentAmount = new FixedDepositAmount(Money.won(1_000_000));
		InterestRate interestRate = new AnnualInterestRate(BigDecimal.valueOf(0.05));
		LocalDate startDate = LocalDate.of(2026, 9, 11);
		InvestPeriod investPeriod = new DailyInvestPeriod(startDate, 30);
		rp = TermRepurchaseAgreement.builder()
			.investmentAmount(investmentAmount)
			.interestRate(interestRate)
			.startDate(startDate)
			.investPeriod(investPeriod)
			.build();
	}

	@Nested
	@DisplayName("약정형 RP 생성자 제약 조건 검증")
	class termRepurchaseAgreementConstructorValidationTest {
		@Test
		@DisplayName("약정형 RP 객체 생성")
		void create_instance() {
			// when & then
			Assertions.assertThat(rp)
				.isNotNull()
				.isInstanceOf(TermRepurchaseAgreement.class);
		}

		@Test
		@DisplayName("RP 인스턴스 생성시 매개변수가 null이면 예외를 발생시켜야 한다.")
		void should_throw_exception_when_param_is_null() {
			// when
			Throwable throwable = Assertions.catchThrowable(() -> TermRepurchaseAgreement.builder().build());
			// then
			Assertions.assertThat(throwable)
				.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	@DisplayName("RP 상품의 만기 일자 계산 검증")
	class calculateExpirationDateTest {
		@Test
		@DisplayName("RP 상품의 만기일자를 계산한다")
		void should_calculate_expiration_date_correctly_when_months_given() {
			// given
			int days = 30;

			// when
			LocalDate expirationDate = rp.calculateExpirationDate(days);

			// then
			LocalDate expected = LocalDate.of(2026, 10, 11);
			Assertions.assertThat(expirationDate).isEqualTo(expected);
		}

		@ParameterizedTest
		@DisplayName("일수(days)가 0이하이면 시작일자를 반환한다.")
		@ValueSource(ints = {-1, 0})
		void should_return_start_date_when_days_is_zero_or_negative(int days) {
			// when
			LocalDate expirationDate = rp.calculateExpirationDate(days);

			// then
			LocalDate expected = LocalDate.of(2026, 9, 11);
			Assertions.assertThat(expirationDate).isEqualTo(expected);
		}
	}

	@Nested
	@DisplayName("특정 일수까지의 이자 금액 계산 검증")
	class calculateInterestForDaysTest {
		@Test
		@DisplayName("만기일(30일)까지의 이자 금액 계산")
		void should_return_interest_amount_when_days_is_30() {
			// given
			int days = 30;
			// when
			Money interest = rp.calculateInterestForDays(days);
			// then
			Money expected = Money.won(4_110);
			Assertions.assertThat(interest).isEqualTo(expected);
		}

		@Test
		@DisplayName("15일까지의 이자 금액 계산")
		void should_return_interest_amount_when_days_is_15() {
			// given
			int days = 15;
			// when
			Money interest = rp.calculateInterestForDays(days);
			// then
			Money expected = Money.won(2_055);
			Assertions.assertThat(interest).isEqualTo(expected);
		}

		@Test
		@DisplayName("원금이 200만원이고 만기까지의 이자 금액 계산")
		void should_return_interest_amount_when_days_is_expiration_days() {
			// given
			RepurchaseAgreement newRp = ((TermRepurchaseAgreement)rp).toBuilder()
				.investmentAmount(new FixedDepositAmount(Money.won(2_000_000)))
				.build();
			int days = 30;
			// when
			Money interest = newRp.calculateInterestForDays(days);
			// then
			Money expected = Money.won(8_219);
			Assertions.assertThat(interest).isEqualTo(expected);
		}

		@Test
		@DisplayName("연수익율이 10%이고 만기까지의 이자 금액 계산")
		void given_annual_interest_is_10_percent_when_days_is_expiration_days_then_return_interest() {
			// given
			RepurchaseAgreement newRp = ((TermRepurchaseAgreement)rp).toBuilder()
				.interestRate(new AnnualInterestRate(BigDecimal.valueOf(0.1)))
				.build();
			int days = 30;
			// when
			Money interest = newRp.calculateInterestForDays(days);
			// then
			Money expected = Money.won(8_219);
			Assertions.assertThat(interest).isEqualTo(expected);
		}

		@ParameterizedTest
		@DisplayName("약정일수가 0 이하인 경우에는 이자는 0원을 반환해야 한다")
		@ValueSource(ints = {-1, 0})
		void should_return_zero_interest_when_days_is_zero_or_negative(int days) {
			// when
			Money interest = rp.calculateInterestForDays(days);
			// then
			Money expected = Money.won(0);
			Assertions.assertThat(interest)
				.isEqualTo(expected);
		}
	}

	@Nested
	@DisplayName("만기일자 계산 검증")
	class getExpirationDateTest {
		@Test
		@DisplayName("약정 일수가 1달인 RP 상품의 만기일자를 조회한다")
		void should_return_expiration_date_when_days_is_30() {
			// given
			RepurchaseAgreement newRp = ((TermRepurchaseAgreement)rp).toBuilder()
				.investPeriod(new MonthlyInvestPeriod(1))
				.build();
			// when
			LocalDate expirationDate = newRp.getExpirationDate();
			// then
			LocalDate expected = LocalDate.of(2026, 10, 11);
			Assertions.assertThat(expirationDate).isEqualTo(expected);
		}

		@Test
		@DisplayName("약정 일수가 60일인 RP 상품의 만기일자를 조회한다")
		void should_return_expiration_date_when_days_is_60() {
			// given
			RepurchaseAgreement newRp = ((TermRepurchaseAgreement)rp).toBuilder()
				.investPeriod(new MonthlyInvestPeriod(2))
				.build();
			// when
			LocalDate expirationDate = newRp.getExpirationDate();
			// then
			LocalDate expected = LocalDate.of(2026, 11, 11);
			Assertions.assertThat(expirationDate).isEqualTo(expected);
		}

		@Test
		@DisplayName("약정 일수가 90일인 RP 상품의 만기일자를 조회한다")
		void should_return_expiration_date_when_days_is_90() {
			// given
			LocalDate startDate = LocalDate.of(2026, 9, 11);
			RepurchaseAgreement newRp = ((TermRepurchaseAgreement)rp).toBuilder()
				.investPeriod(new DailyInvestPeriod(startDate, 90))
				.build();
			// when
			LocalDate expirationDate = newRp.getExpirationDate();
			// then
			LocalDate expected = LocalDate.of(2026, 12, 10);
			Assertions.assertThat(expirationDate).isEqualTo(expected);
		}

		@Test
		@DisplayName("약정 일수가 1년인 RP 상품의 만기일자를 조회한다")
		void should_return_expiration_date_when_years_is_one() {
			// given
			RepurchaseAgreement newRp = ((TermRepurchaseAgreement)rp).toBuilder()
				.investPeriod(new YearlyInvestPeriod(1))
				.build();
			// when
			LocalDate expirationDate = newRp.getExpirationDate();
			// then
			LocalDate expected = LocalDate.of(2027, 9, 11);
			Assertions.assertThat(expirationDate).isEqualTo(expected);
		}
	}

	@Nested
	@DisplayName("만기 이자 금액 계산 검증")
	class calculateMaturityInterestTest {
		@Test
		@DisplayName("만기 이자 금액 계산")
		void should_return_maturity_interest() {
			Money money = rp.calculateMaturityInterest();
			throw new RuntimeException("todo");
		}
	}
}
