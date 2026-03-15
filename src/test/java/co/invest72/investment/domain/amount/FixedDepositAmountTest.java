package co.invest72.investment.domain.amount;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import co.invest72.investment.domain.InterestRate;
import co.invest72.investment.domain.LumpSumInvestmentAmount;
import co.invest72.investment.domain.interest.AnnualInterestRate;
import co.invest72.money.domain.Money;

class FixedDepositAmountTest {

	private LumpSumInvestmentAmount investmentAmount;

	@BeforeEach
	void setUp() {
		investmentAmount = new FixedDepositAmount(BigDecimal.valueOf(1_000_000), "KRW");
	}

	@Test
	void created() {
		assertNotNull(investmentAmount);
	}

	@Test
	void shouldThrowException_whenAmountIsNegative() {
		assertThrows(IllegalArgumentException.class, () -> new FixedDepositAmount(BigDecimal.valueOf(-1), "KRW"));
	}

	@Test
	void shouldReturnDepositAmount() {
		Money depositAmount = investmentAmount.getDepositAmount();

		Money expected = Money.won(1_000_000);
		assertEquals(expected, depositAmount);
	}

	@Test
	void shouldReturnInterest() {
		InterestRate interestRate = new AnnualInterestRate(0.05);

		Money interest = investmentAmount.calAnnualInterest(interestRate);

		Money expectedInterest = Money.won(BigDecimal.valueOf(50_000));
		Assertions.assertThat(interest).isEqualTo(expectedInterest);
	}

	@DisplayName("연이자 계산 - 예치금이 10조원인 상태에서 이자를 정확히 계삲되어야 한다.")
	@Test
	void calAnnualInterest_shouldReturnAnnualInterest() {
		investmentAmount = new FixedDepositAmount(new BigDecimal("10000000000000"), "KRW"); // 10조원
		InterestRate interestRate = new AnnualInterestRate(0.05);

		Money interest = investmentAmount.calAnnualInterest(interestRate);

		Money expectedInterest = Money.won(new BigDecimal("500000000000")); // 5천억
		Assertions.assertThat(interest).isEqualTo(expectedInterest);
	}

	@DisplayName("월이자 계산 - 연이율이 5%인 경우에 월이자가 정확히 계산되어야 한다.")
	@Test
	void calMonthlyInterest_shouldReturnMonthlyInterest() {
		InterestRate interestRate = new AnnualInterestRate(0.05);

		Money interest = investmentAmount.calMonthlyInterest(interestRate);

		Money expectedInterest = Money.won(BigDecimal.valueOf(4166.666666666667000000));
		Assertions.assertThat(interest).isEqualTo(expectedInterest);
	}

	@DisplayName("금액 반환 - 원화 Money 타입 예치금 반환")
	@Test
	void getDepositAmount_temp_shouldReturnMoney() {
		Money depositAmount = investmentAmount.getDepositAmount();

		Money expected = Money.won(1_000_000);
		Assertions.assertThat(depositAmount).isEqualTo(expected);
	}

	@DisplayName("금액 반환 - 달러 Money 타입 예치금 반환")
	@Test
	void getDepositAmount_shouldReturnMoneyInDollars() {
		investmentAmount = new FixedDepositAmount(BigDecimal.valueOf(5), "USD");
		Money depositAmount = investmentAmount.getDepositAmount();

		Money expected = Money.dollar(5);
		Assertions.assertThat(depositAmount).isEqualTo(expected);
	}
}
