package co.invest72.investment.domain.period;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import co.invest72.investment.domain.InvestPeriod;

class MonthlyInvestPeriodTest {

	private InvestPeriod investPeriod;

	@Nested
	@DisplayName("생성자 제약 조건 검증")
	class ConstructorValidationTest {
		@ParameterizedTest(name = "months={0}")
		@ValueSource(ints = {-1, 11989})
		@DisplayName("유효하지 않은 개월수가 주어지면 객체 생성이 불가능하다")
		void should_throw_exception_when_invalid_months(int months) {
			// when & then
			Assertions.assertThatThrownBy(() -> new MonthlyInvestPeriod(months))
				.isInstanceOf(IllegalArgumentException.class);
		}

		@ParameterizedTest(name = "months={0}")
		@DisplayName("유효한 개월수가 주어졌을때 객체 생성이 가능하다")
		@ValueSource(ints = {0, 1, 11988})
		void can_create_instance_when_months_is_between_0_and_11988(int months) {
			// when
			InvestPeriod sut = new MonthlyInvestPeriod(months);
			// then
			Assertions.assertThat(sut).isNotNull();
		}
	}

	@BeforeEach
	void setUp() {
		investPeriod = new MonthlyInvestPeriod(12);
	}

	@Test
	@DisplayName("투자 개월수를 반환한다")
	void should_return_invest_months() {
		int actualMonths = investPeriod.getMonths();

		int expectedMonths = 12;
		assertEquals(expectedMonths, actualMonths);
	}

	@Nested
	@DisplayName("투자 개월수(months) 계산 검증")
	class getMonthsTest {
		@Test
		@DisplayName("시작일자가 2026년 1월 1일 기준으로 투자 개월수를 계산하여 반환한다")
		void should_return_invest_months_given_start_date() {
			// given
			InvestPeriod sut = new MonthlyInvestPeriod(1);
			LocalDate startDate = LocalDate.of(2026, 1, 1);
			// when
			int months = sut.getMonths(startDate);
			// then
			Assertions.assertThat(months).isEqualTo(1);
		}

		@ParameterizedTest(name = "투자 개월수가 {0}개월일때 {1}개월수를 반환한다")
		@CsvSource({
			"0, 0",
			"1, 1",
			"2, 2",
			"12, 12"
		})
		@DisplayName("투자 개월수를 계산")
		void should_return_invest_months__given_start_date_when_get_months(int months, int expectedMonths) {
			// given
			InvestPeriod sut = new MonthlyInvestPeriod(months);
			LocalDate startDate = LocalDate.of(2026, 1, 1);
			// when
			int investMonths = sut.getMonths(startDate);
			// then
			Assertions.assertThat(investMonths).isEqualTo(expectedMonths);
		}
	}

	@Nested
	@DisplayName("투자 일수(days) 계산 검증")
	class getDaysTest {
		@ParameterizedTest(name = "투자 개월수가 {0}개월일때 {1}일을 반환한다")
		@DisplayName("2026년 1월 1일을 기준으로 투자 일수를 계산한다")
		@CsvSource({
			"0, 0", // 1월1일 ~ 1월1일, 총 0일
			"1, 31", // 1월1일 ~ 2월1일, 총 31일
			"2, 59", // 1월1일 ~ 3월1일, 총 59일
			"12, 365" // 1월1일 ~ 27년 1월1일, 총 365
		})
		void should_return_invest_days_when_months_is_one(int months, int expectedMonths) {
			// given
			investPeriod = new MonthlyInvestPeriod(months);
			LocalDate startDate = LocalDate.of(2026, 1, 1);
			// when
			int days = investPeriod.getDays(startDate);
			// then
			Assertions.assertThat(days).isEqualTo(expectedMonths);
		}
	}

}
