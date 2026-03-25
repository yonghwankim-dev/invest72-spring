package co.invest72.financial_product.application;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.invest72.common.time.LocalDateProvider;
import co.invest72.financial_product.domain.FinancialProduct;
import co.invest72.financial_product.domain.FinancialProductRepository;
import co.invest72.financial_product.domain.service.FinancialProductCalculator;
import co.invest72.financial_product.presentation.dto.request.FinancialProductRequest;
import co.invest72.financial_product.presentation.dto.response.DetailedFinancialProductResponse;
import co.invest72.financial_product.presentation.dto.response.FinancialProductSummary;
import co.invest72.financial_product.presentation.dto.response.ProductCurrency;
import co.invest72.investment.application.InvestmentFactory;
import co.invest72.money.domain.Currency;
import co.invest72.user.domain.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FinancialProductService {

	private final FinancialProductRepository repository;
	private final LocalDateProvider localDateProvider;
	private final InvestmentFactory investmentFactory;
	private final FinancialProductFactory financialProductFactory;
	private final FinancialProductCalculator calculator;

	@Transactional
	@CacheEvict(value = {"productSummary"}, key = "#user.id")
	public String createProduct(User user, FinancialProductRequest dto) {
		FinancialProduct product = financialProductFactory.create(user.getId(), dto);
		return repository.save(product);
	}

	@Transactional(readOnly = true)
	@Cacheable(value = "productDetail", key = "#user.id + '-' + #productId")
	public DetailedFinancialProductResponse getProductDetail(User user, String productId) {
		FinancialProduct product = findFinancialProduct(user, productId);
		LocalDate today = localDateProvider.now();

		LocalDate expirationDate = calculator.calculateExpirationDate(product);
		BigDecimal balance = calculator.calculateBalance(product, today);
		BigDecimal progress = calculator.calculateProgress(product, today);
		Long remainingDays = calculator.calculateRemainingDays(product, today);

		Currency currency = Currency.from(product.getAmount().getCurrency());
		ProductCurrency productCurrency = ProductCurrency.from(currency);
		return DetailedFinancialProductResponse.builder()
			.id(product.getId())
			.userId(product.getUserId())
			.name(product.getName())
			.investmentType(product.getProductInvestmentType().getName())
			.amount(product.getAmount().getValue())
			.months(product.getMonths().getValue())
			.paymentDay(product.getPaymentDayValue())
			.interestRate(product.getProductAnnualInterestRate().getValue())
			.interestType(product.getProductInterestType().getName())
			.taxType(product.getProductTaxType().getName())
			.taxRate(product.getProductTaxRate().getValue())
			.startDate(product.getStartDate())
			.createdAt(product.getCreatedAt())
			.expirationDate(expirationDate)
			.balance(balance)
			.progress(progress)
			.remainingDays(remainingDays)
			.productCurrency(productCurrency)
			.build();
	}

	private FinancialProduct findFinancialProduct(User user, String productId) {
		FinancialProduct product = repository.findByProductId(productId);
		validateFinancialProduct(user, product);
		return product;
	}

	private void validateFinancialProduct(User user, FinancialProduct product) {
		if (product == null || !product.getUserId().equals(user.getId())) {
			throw new IllegalArgumentException("상품을 찾을 수 없거나 접근 권한이 없습니다.");
		}
	}

	/**
	 * 상품 정보 업데이트<br>
	 * 업데이트된 상품 정보로 현재 객체의 필드 값을 변경
	 * 상품 수정시 투자 유형(InvestmentType)은 변경할 수 없다.<br>
	 * @param user 업데이트 요청을 하는 사용자 (상품 소유자와 일치해야 함)
	 * @param productId 업데이트할 상품의 ID
	 * @param dto 업데이트할 상품 정보
	 */
	@Transactional
	@Caching(evict = {
		// 1. 해당 유저의 상품 요약 목록 캐시 삭제
		@CacheEvict(value = "productSummary", key = "#user.id"),
		// 2. 수정된 특정 상품의 상세 정보 캐시 삭제
		@CacheEvict(value = "productDetail", key = "#user.id + '-' + #productId")
	})
	public void updateProduct(User user, String productId, FinancialProductRequest dto) {
		FinancialProduct existingProduct = findFinancialProduct(user, productId);
		FinancialProduct updatedProduct = financialProductFactory.createUpdatedProduct(existingProduct, dto);
		existingProduct.update(updatedProduct);
	}

	@Transactional
	@Caching(evict = {
		// 1. 해당 유저의 상품 요약 목록 캐시 삭제
		@CacheEvict(value = "productSummary", key = "#user.id"),
		// 2. 수정된 특정 상품의 상세 정보 캐시 삭제
		@CacheEvict(value = "productDetail", key = "#user.id + '-' + #productId")
	})
	public void deleteProduct(User user, String productId) {
		findFinancialProduct(user, productId);
		repository.deleteByProductId(productId);
	}

	@Transactional(readOnly = true)
	public FinancialProduct getProductByUserAndProductId(User user, String productId) {
		return findFinancialProduct(user, productId);
	}

	/**
	 * 상품 요약 목록 조회
	 * <p>
	 * 정렬 기준<br>
	 * - 1차 정렬 기준 : 시작일자 내림차순<br>
	 * - 2차 정렬 기준 : 만기일 오름차순<br>
	 * - 3차 정렬 기준 : 금액 내림차순<br>
	 * - 4차 정렬 기준 : 생성일자 오름차순<br>
	 * - 마지막 정렬 기준 : 식별자 오름차순<br>
	 * @param user 조회 대상 사용자
	 * @return 상품 요약 정보 리스트
	 */
	@Transactional(readOnly = true)
	@Cacheable(value = "productSummary", key = "#user.id")
	public List<FinancialProductSummary> getSummaryProductsByUser(User user) {
		List<FinancialProductSummary> result = new ArrayList<>();
		List<FinancialProduct> products = repository.findAllByUserId(user.getId());

		LocalDate today = localDateProvider.now();
		for (FinancialProduct product : products) {
			FinancialProductSummary data = FinancialProductSummary.from(
				product,
				investmentFactory.createBy(product),
				today
			);
			result.add(data);
		}

		return result.stream()
			.sorted(getFinancialProductSummaryResponseComparator())
			.toList();
	}

	private Comparator<FinancialProductSummary> getFinancialProductSummaryResponseComparator() {
		return Comparator.comparing(FinancialProductSummary::getStartDate, Comparator.reverseOrder())
			.thenComparing(FinancialProductSummary::getExpirationDate,
				Comparator.nullsLast(Comparator.naturalOrder()))
			.thenComparing(FinancialProductSummary::getBalance, Comparator.reverseOrder())
			.thenComparing(FinancialProductSummary::getCreatedAt)
			.thenComparing(FinancialProductSummary::getId);
	}
}
