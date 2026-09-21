package co.invest72.rp.application;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.invest72.common.time.LocalDateProvider;
import co.invest72.financial_product.domain.IdGenerator;
import co.invest72.financial_product.domain.ProductAmount;
import co.invest72.financial_product.domain.ProductAnnualInterestRate;
import co.invest72.financial_product.domain.ProductInterestType;
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
		Optional<RepurchaseAgreementEntity> foundedRp = repository.findById(id);
		RepurchaseAgreementEntity rp = foundedRp.orElseThrow(
			() -> new NoSuchElementException("not found rp, id=" + id));
		return RpDetailedResponse.fromEntity(rp);
	}
}
