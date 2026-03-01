package co.invest72.investment.domain.investment;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PeriodPolicyTest {

	@DisplayName("진행률 계산 - STANDARD, 기준일자가 시작일자 하루전인 경우 진행률은 0%이다")
	@Test
	void calculateProgress_standard_beforeStartDate() {
		// Given
		PeriodPolicy policy = PeriodPolicy.STANDARD;
		LocalDate startDate = LocalDate.of(2024, 1, 1);
		LocalDate expirationDate = LocalDate.of(2024, 12, 31);
		LocalDate today = LocalDate.of(2023, 12, 31);
		// When
		BigDecimal progress = policy.calculateProgress(startDate, expirationDate, today);
		// then
		Assertions.assertThat(progress)
			.isEqualByComparingTo(BigDecimal.ZERO);
	}

	@DisplayName("진행률 계산 - STANDARD, 기준일자가 만기일자 하루후인 경우 진행률은 100%이다")
	@Test
	void calculateProgress_standard_afterExpirationDate() {
		// Given
		PeriodPolicy policy = PeriodPolicy.STANDARD;
		LocalDate startDate = LocalDate.of(2024, 1, 1);
		LocalDate expirationDate = LocalDate.of(2024, 12, 31);
		LocalDate today = LocalDate.of(2025, 1, 1);
		// When
		BigDecimal progress = policy.calculateProgress(startDate, expirationDate, today);
		// then
		Assertions.assertThat(progress)
			.isEqualByComparingTo(BigDecimal.ONE);
	}

	@DisplayName("진행률 계산 - STANDARD, 기준일자가 시작일자와 만기일자 사이인 경우 진행률은 0과 1 사이의 값이다")
	@Test
	void calculateProgress_standard_betweenStartAndExpirationDate() {
		// Given
		PeriodPolicy policy = PeriodPolicy.STANDARD;
		LocalDate startDate = LocalDate.of(2024, 1, 1);
		LocalDate expirationDate = LocalDate.of(2024, 12, 31);
		LocalDate today = LocalDate.of(2024, 6, 30);
		// When
		BigDecimal progress = policy.calculateProgress(startDate, expirationDate, today);
		// then
		Assertions.assertThat(progress)
			.isGreaterThan(BigDecimal.ZERO)
			.isLessThan(BigDecimal.ONE)
			.isEqualByComparingTo(BigDecimal.valueOf(0.50)); // 6개월 경과 -> 181/365 = 0.50
	}

	@DisplayName("진행률 계산 - STANDARD, 시작일자와 만기일자가 동일한 경우 진행률은 100%이다")
	@Test
	void calculateProgress_standard_sameStartAndExpirationDate() {
		// Given
		PeriodPolicy policy = PeriodPolicy.STANDARD;
		LocalDate startDate = LocalDate.of(2024, 1, 1);
		LocalDate expirationDate = LocalDate.of(2024, 1, 1);
		LocalDate today = LocalDate.of(2024, 1, 1);
		// When
		BigDecimal progress = policy.calculateProgress(startDate, expirationDate, today);
		// then
		Assertions.assertThat(progress)
			.isEqualByComparingTo(BigDecimal.ONE);
	}

	@DisplayName("진행률 계산 - STANDARD, 시작일자가 만기일자보다 크면 예외가 발생한다")
	@Test
	void calculateProgress_standard_startDateAfterExpirationDate_throwsException() {
		// Given
		PeriodPolicy policy = PeriodPolicy.STANDARD;
		LocalDate startDate = LocalDate.of(2024, 1, 2);
		LocalDate expirationDate = LocalDate.of(2024, 1, 1);
		LocalDate today = LocalDate.of(2024, 1, 2);
		// When & Then
		Assertions.assertThatThrownBy(() -> policy.calculateProgress(startDate, expirationDate, today))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("시작일자는 만기일자보다 이전이어야 합니다.");
	}

	@DisplayName("진행률 계산 - INDEFINITE, 항상 진행률은 100%이다")
	@Test
	void calculateProgress_indefinite_alwaysOne() {
		// Given
		PeriodPolicy policy = PeriodPolicy.INDEFINITE;
		LocalDate startDate = LocalDate.of(2024, 1, 1);
		LocalDate expirationDate = LocalDate.MAX;
		LocalDate today = LocalDate.of(2024, 6, 30);
		// When
		BigDecimal progress = policy.calculateProgress(startDate, expirationDate, today);
		// then
		Assertions.assertThat(progress)
			.isEqualByComparingTo(BigDecimal.ONE);
	}

	@DisplayName("남은 일수 계산 - STANDARD, 기준일자가 만기일자 하루후인 경우 남은 일수는 0이다")
	@Test
	void remainingDays_standard_afterExpirationDate() {
		// Given
		PeriodPolicy policy = PeriodPolicy.STANDARD;
		LocalDate expirationDate = LocalDate.of(2024, 12, 31);
		LocalDate today = LocalDate.of(2025, 1, 1);
		// When
		long remainingDays = policy.remainingDays(today, expirationDate);
		// then
		Assertions.assertThat(remainingDays).isZero();
	}

	@DisplayName("남은 일수 계산 - STANDARD, 기준일자가 만기일자 하루전인 경우 남은 일수는 1이다")
	@Test
	void remainingDays_standard_beforeExpirationDate() {
		// Given
		PeriodPolicy policy = PeriodPolicy.STANDARD;
		LocalDate expirationDate = LocalDate.of(2024, 12, 31);
		LocalDate today = LocalDate.of(2024, 12, 30);
		// When
		long remainingDays = policy.remainingDays(today, expirationDate);
		// then
		Assertions.assertThat(remainingDays).isEqualTo(1);
	}

	@DisplayName("남은 일수 계산 - STANDARD, 기준일자가 만기일자와 동일한 경우 남은 일수는 0이다")
	@Test
	void remainingDays_standard_sameAsExpirationDate() {
		// Given
		PeriodPolicy policy = PeriodPolicy.STANDARD;
		LocalDate expirationDate = LocalDate.of(2024, 12, 31);
		LocalDate today = LocalDate.of(2024, 12, 31);
		// When
		long remainingDays = policy.remainingDays(today, expirationDate);
		// then
		Assertions.assertThat(remainingDays).isZero();
	}

	@DisplayName("남은 일수 계산 - INDEFINITE, 항상 남은 일수는 0이다")
	@Test
	void remainingDays_indefinite_alwaysZero() {
		// Given
		PeriodPolicy policy = PeriodPolicy.INDEFINITE;
		LocalDate expirationDate = LocalDate.MAX;
		LocalDate today = LocalDate.of(2024, 6, 30);
		// When
		long remainingDays = policy.remainingDays(today, expirationDate);
		// then
		Assertions.assertThat(remainingDays).isZero();
	}

	@DisplayName("만기일 계산 - STANDARD, 시작일자에 개월 수를 더한 날짜가 만기일이다")
	@Test
	void calculateExpiration_standard() {
		// Given
		PeriodPolicy policy = PeriodPolicy.STANDARD;
		LocalDate startDate = LocalDate.of(2024, 1, 1);
		int months = 12;
		// When
		LocalDate expirationDate = policy.calculateExpiration(startDate, months);
		// then
		Assertions.assertThat(expirationDate).isEqualTo(LocalDate.of(2025, 1, 1));
	}

	@DisplayName("만기일 계산 - STANDARD, 개월수가 음수이면 예외가 발생해야 한다")
	@Test
	void calculateExpiration_standard_negativeMonths_throwsException() {
		// Given
		PeriodPolicy policy = PeriodPolicy.STANDARD;
		LocalDate startDate = LocalDate.of(2024, 1, 1);
		int months = -1;
		// When & Then
		Assertions.assertThatThrownBy(() -> policy.calculateExpiration(startDate, months))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("개월 수는 음수일 수 없습니다.");
	}

	@DisplayName("만기일 계산 - INDEFINITE, 만기일은 LocalDate.MAX이다")
	@Test
	void calculateExpiration_indefinite() {
		// Given
		PeriodPolicy policy = PeriodPolicy.INDEFINITE;
		LocalDate startDate = LocalDate.of(2024, 1, 1);
		int months = 12;
		// When
		LocalDate expirationDate = policy.calculateExpiration(startDate, months);
		// then
		Assertions.assertThat(expirationDate).isEqualTo(LocalDate.MAX);
	}
}
