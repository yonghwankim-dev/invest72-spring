package co.invest72.rp.application;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.NoSuchElementException;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.invest72.common.time.LocalDateProvider;
import co.invest72.exchange_rate.domain.entity.ExchangeRate;
import co.invest72.exchange_rate.domain.service.ExchangeRateService;
import co.invest72.financial_product.domain.IdGenerator;
import co.invest72.financial_product.domain.ProductAmount;
import co.invest72.financial_product.domain.ProductAnnualInterestRate;
import co.invest72.financial_product.domain.ProductInterestType;
import co.invest72.financial_product.domain.ProductInvestmentType;
import co.invest72.financial_product.domain.ProductTaxRate;
import co.invest72.financial_product.domain.ProductTaxType;
import co.invest72.investment.domain.tax.TaxType;
import co.invest72.rp.domain.RepurchaseAgreement;
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
	private final ExchangeRateService exchangeRateService;
	private final RpDomainMapper mapper;

	@Transactional
	@CacheEvict(value = {"productSummary"}, key = "#user.id")
	public String createRp(User user, RpCreateRequest request) {
		// 환율 찾기
		ExchangeRate exchangeRate = exchangeRateService.findExchangeRate(request.getCurrencyCode());

		RepurchaseAgreementEntity entity = RepurchaseAgreementEntity.builder()
			.id(idGenerator.generateId())
			.productInvestmentType(ProductInvestmentType.from(request.getInvestmentType()))
			.name(request.getName())
			.amount(ProductAmount.of(request.getAmount(), exchangeRate))
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
			.map(entity -> {
				RepurchaseAgreement rp = mapper.toDomain(entity);

				BigDecimal maturityInterest = rp.calculateMaturityInterest().getValue();
				LocalDate now = localDateProvider.now();
				BigDecimal currentInterest = rp.calculateInterestForDate(now).getValue();
				BigDecimal currentInterestRate = rp.calculateInterestRateForDate(now).round();
				return RpDetailedResponse.builder()
					.id(entity.getId())
					.investmentType(entity.getTypeName())
					.name(entity.getName())
					.amount(entity.getAmount().getValue())
					.currency(entity.getAmount().getCurrencyCode())
					.interestRate(entity.getProductAnnualInterestRate().getValue())
					.startDate(entity.getStartDate())
					.termOfAgreement(entity.getDays())
					.maturityInterest(maturityInterest)
					.currentInterest(currentInterest)
					.currentInterestRate(currentInterestRate)
					.isAutoReinvest(true)
					.build();
			})
			.orElseThrow(() -> new NoSuchElementException("not found rp, id=" + id));
	}
}
