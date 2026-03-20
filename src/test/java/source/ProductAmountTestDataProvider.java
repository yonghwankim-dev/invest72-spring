package source;

import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;

/**
 * {@code ProductAmountTest} 클래스의 단위 테스트에서 사용되는 테스트 데이터를 제공하는 유틸리티 클래스입니다.
 */
public final class ProductAmountTestDataProvider {
	private ProductAmountTestDataProvider() {
		// utility class
	}

	/**
	 * 유효한 금액과 해당 금액에 대한 설명을 포함하는 {@code Stream<Arguments>}를 반환합니다.
	 *
	 * 각 {@code Arguments}는 테스트 메서드 {@code newInstance_whenAmountIsValid_thenCreateInstance}에 전달될 매개변수로 사용됩니다.
	 * 금액은 {@code BigDecimal}로 표현되며, 설명은 해당 금액이 어떤 범위에 속하는지를 나타내는 문자열입니다.
	 */
	public static Stream<Arguments> validAmounts() {
		return Stream.of(
			Arguments.of(new java.math.BigDecimal("0"), "0원"),
			Arguments.of(new java.math.BigDecimal("0.01"), "0.01원"),
			Arguments.of(new java.math.BigDecimal("1000"), "천원"),
			Arguments.of(new java.math.BigDecimal("9999999999999.99"), "10조 미만"),
			Arguments.of(new java.math.BigDecimal("10000000000000"), "10조"),
			Arguments.of(new java.math.BigDecimal("99999999999999998.99"), "99999조 미만"),
			Arguments.of(new java.math.BigDecimal("99999999999999999"), "99999조")
		);
	}

	public static Stream<Arguments> invalidAmounts() {
		return Stream.of(
			Arguments.of(new java.math.BigDecimal("-0.01")),
			Arguments.of(new java.math.BigDecimal("-1")),
			Arguments.of(new java.math.BigDecimal("100000000000000000.01")),
			Arguments.of(new java.math.BigDecimal("100000000000000000"))
		);
	}
}
