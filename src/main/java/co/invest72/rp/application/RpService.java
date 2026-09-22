package co.invest72.rp.application;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.NoSuchElementException;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.invest72.common.time.LocalDateProvider;
import co.invest72.financial_product.domain.IdGenerator;
import co.invest72.financial_product.domain.ProductAmount;
import co.invest72.financial_product.domain.ProductAnnualInterestRate;
import co.invest72.financial_product.domain.ProductInterestType;
import co.invest72.financial_product.domain.ProductInvestmentType;
import co.invest72.financial_product.domain.ProductTaxRate;
import co.invest72.financial_product.domain.ProductTaxType;
import co.invest72.investment.domain.tax.TaxType;
import co.invest72.rp.entity.RepurchaseAgreementEntity;
import co.invest72.rp.infrastructure.RpRepository;
import co.invest72.rp.presentation.dto.RpCreateRequest;
import co.invest72.rp.presentation.dto.RpDetailedResponse;
import co.invest72.user.domain.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RpService {

	private final IdGenerator idGenerator;
	private final LocalDateProvider localDateProvider;
	private final RpRepository repository;

	@Transactional
	@CacheEvict(value = {"productSummary"}, key = "#user.id")
	public String createRp(User user, RpCreateRequest request) {
		RepurchaseAgreementEntity entity = RepurchaseAgreementEntity.builder()
			.id(idGenerator.generateId())
			.productInvestmentType(ProductInvestmentType.from(request.getInvestmentType()))
			.name(request.getName())
			.amount(ProductAmount.of(request.getAmount(), request.getCurrencyCode()))
			.days(request.getDays())
			.productAnnualInterestRate(new ProductAnnualInterestRate(request.getInterestRate()))
			.productInterestType(ProductInterestType.from(request.getInterestType()))
			.productTaxType(ProductTaxType.from(TaxType.valueOf(request.getTaxType())))
			.productTaxRate(new ProductTaxRate(request.getTaxRate()))
			.startDate(request.getStartDate())
			.createdAt(localDateProvider.nowDateTime())
			.userId(user.getId())
			.build();
		repository.save(entity);
		return entity.getId();
	}

	@Transactional(readOnly = true)
	public RpDetailedResponse getRp(String id) throws NoSuchElementException {
		return repository.findById(id)
			.map(rp -> {
				// todo: convert rp entity to rp domain
				BigDecimal maturityInterest = calculateMaturityInterest(rp);
				BigDecimal currentInterest = calculateCurrentInterest(rp);
				BigDecimal currentInterestRate = calculateCurrentInterestRate(rp);
				return RpDetailedResponse.builder()
					.id(rp.getId())
					.investmentType(rp.getTypeName())
					.name(rp.getName())
					.amount(rp.getAmount().getValue())
					.currency(rp.getAmount().getCurrency())
					.interestRate(rp.getProductAnnualInterestRate().getValue())
					.startDate(rp.getStartDate())
					.termOfAgreement(rp.getDays())
					.maturityInterest(maturityInterest)
					.currentInterest(currentInterest)
					.currentInterestRate(currentInterestRate)
					.isAutoReinvest(true)
					.build();
			})
			.orElseThrow(() -> new NoSuchElementException("not found rp, id=" + id));
	}

	/**
	 * 만기 이자 금액 계산하여 반환
	 * <p>
	 * - 원금 x 연 이자율 x (투자 일수 / 365)
	 * <p>
	 * - 만기 이자 금액 반환시 정수 형태로 반올림하여 반환
	 * @param entity {@link RepurchaseAgreementEntity}
	 * @return 만기 시 이자 금액
	 */
	private BigDecimal calculateMaturityInterest(RepurchaseAgreementEntity entity) {
		BigDecimal amount = entity.getAmount().getValue();
		BigDecimal annualInterest = entity.getProductAnnualInterestRate().getValue();
		Integer days = entity.getDays();

		BigDecimal interestForHoldingPeriod = calculateInterestForHoldingPeriod(amount, annualInterest, days);
		return applyDailyInterest(interestForHoldingPeriod);
	}

	private BigDecimal calculateInterestForHoldingPeriod(BigDecimal principal, BigDecimal annualInterestRate,
		Integer holdingPeriod) {
		return principal.multiply(annualInterestRate)
			.multiply(BigDecimal.valueOf(holdingPeriod));
	}

	private BigDecimal applyDailyInterest(BigDecimal interest) {
		if (interest == null) {
			return BigDecimal.ZERO;
		}
		return interest.divide(BigDecimal.valueOf(365), 0, RoundingMode.HALF_EVEN);
	}

	/**
	 * 현재 이자 금액 계산
	 * <p>
	 * - RP 상품의 시작일자 및 약정일수 기반으로 현재 이자 금액을 계산
	 * <p>
	 * - 현재 이자 금액 = 원금 x 연이율 x (예치 일수 / 365)
	 * @return BigDecimal 현재 이자 금액
	 */
	private BigDecimal calculateCurrentInterest(RepurchaseAgreementEntity rp) {
		BigDecimal principal = rp.getAmount().getValue();
		BigDecimal annualInterest = rp.getProductAnnualInterestRate().getValue();
		int holdingPeriod = calculateHoldingPeriod(rp);

		BigDecimal interestForHoldingPeriod = calculateInterestForHoldingPeriod(principal, annualInterest,
			holdingPeriod);
		return applyDailyInterest(interestForHoldingPeriod);
	}

	/**
	 * 예치 일수 계산하여 반환한다.
	 * <p>
	 * - 예치 일수 = 현재 일자 - 시작 일자
	 * @return int 예치 일수
	 */
	private int calculateHoldingPeriod(RepurchaseAgreementEntity rp) {
		LocalDate nowDate = localDateProvider.now();
		LocalDate startDate = rp.getStartDate();
		return (int)startDate.until(nowDate, ChronoUnit.DAYS);
	}

	/**
	 * 현재 이자 금액에 대한 수익률을 비율(Ratio) 형태의 실수값으로 계산하여 반환한다.
	 * <p><b>계산식:</b>
	 * <ul>
	 *   <li>{@code 현재 이자 수익률 = 현재 이자 금액 / 원금}</li>
	 * </ul>
	 *
	 * <p><b>반환 형식 및 반올림 정책:</b>
	 * <ul>
	 *   <li>백분율(%)이 아닌 <b>소수점 형태의 비율(Ratio) 값</b>으로 반환한다. (예: 2% → {@code 0.02})</li>
	 *   <li>소수점 이하 둘째 자리까지 표기하며, {@link RoundingMode#HALF_EVEN} (Banker's Rounding) 정책을 적용한다.</li>
	 * </ul>
	 *
	 * @param rp {@link RepurchaseAgreementEntity}
	 * @return {@link BigDecimal} 현재 이자 금액 수익율
	 */
	private BigDecimal calculateCurrentInterestRate(RepurchaseAgreementEntity rp) {
		// 현재 이자 금액 계산
		BigDecimal principal = rp.getAmount().getValue();
		BigDecimal annualInterest = rp.getProductAnnualInterestRate().getValue();
		int holdingPeriod = calculateHoldingPeriod(rp);
		BigDecimal dailyInterest = applyDailyInterest(
			calculateInterestForHoldingPeriod(principal, annualInterest, holdingPeriod));
		// 현재 아지 금액 수익율 계산
		return applyInterestRate(dailyInterest, principal);
	}

	private BigDecimal applyInterestRate(BigDecimal interest, BigDecimal principal) {
		return interest.divide(principal, 4, RoundingMode.HALF_EVEN);
	}
}
