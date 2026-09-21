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
				BigDecimal maturityInterest = calculateMaturityInterest(rp);
				BigDecimal currentInterest = calculateCurrentInterest(rp);
				BigDecimal currentInterestRate = BigDecimal.valueOf(0.034);
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
		return amount.multiply(annualInterest)
			.multiply(BigDecimal.valueOf(days))
			.divide(BigDecimal.valueOf(365), 0, RoundingMode.HALF_EVEN);
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
		return principal.multiply(annualInterest)
			.multiply(BigDecimal.valueOf(holdingPeriod))
			.divide(BigDecimal.valueOf(365), 0, RoundingMode.HALF_EVEN);
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
}
