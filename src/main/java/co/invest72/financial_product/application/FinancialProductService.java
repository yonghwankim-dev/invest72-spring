package co.invest72.financial_product.application;

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
import co.invest72.financial_product.presentation.dto.request.FinancialProductRequestDto;
import co.invest72.financial_product.presentation.dto.response.DetailedFinancialProductResponse;
import co.invest72.financial_product.presentation.dto.response.FinancialProductDto;
import co.invest72.financial_product.presentation.dto.response.FinancialProductSummaryResponse;
import co.invest72.investment.application.InvestmentFactory;
import co.invest72.user.domain.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FinancialProductService {

	private final FinancialProductRepository repository;
	private final LocalDateProvider localDateProvider;
	private final InvestmentFactory investmentFactory;
	private final FinancialProductFactory financialProductFactory;

	@Transactional
	@CacheEvict(value = {"productSummary"}, allEntries = true)
	public String createProduct(User user, FinancialProductRequestDto dto) {
		FinancialProduct product = financialProductFactory.create(user.getId(), dto);
		return repository.save(product);
	}

	@Transactional(readOnly = true)
	public List<FinancialProductDto> getProductsByUser(User user) {
		return repository.findAllByUserId(user.getId()).stream()
			.map(this::buildProductResponseDto)
			.toList();
	}

	private FinancialProductDto buildProductResponseDto(FinancialProduct product) {
		return FinancialProductDto.builder()
			.id(product.getId())
			.userId(product.getUserId())
			.name(product.getName())
			.investmentType(product.getInvestmentType().name())
			.amount(product.getAmount().getValue())
			.months(product.getMonths().getValue())
			.interestRate(product.getInterestRate().getValue())
			.interestType(product.getInterestType().name())
			.taxType(product.getTaxType().name())
			.taxRate(product.getTaxRate().getValue())
			.startDate(product.getStartDate())
			.createdAt(product.getCreatedAt())
			.build();
	}

	@Transactional(readOnly = true)
	@Cacheable(value = "productDetail", key = "#productId")
	public DetailedFinancialProductResponse getProductDetail(User user, String productId) {
		FinancialProduct product = findFinancialProduct(user, productId);
		LocalDate today = localDateProvider.now();

		return DetailedFinancialProductResponse.builder()
			.id(product.getId())
			.userId(product.getUserId())
			.name(product.getName())
			.investmentType(product.getInvestmentType().name())
			.amount(product.getAmount().getValue())
			.months(product.getMonths().getValue())
			.paymentDay(product.getPaymentDayValue())
			.interestRate(product.getInterestRate().getValue())
			.interestType(product.getInterestType().name())
			.taxType(product.getTaxType().name())
			.taxRate(product.getTaxRate().getValue())
			.startDate(product.getStartDate())
			.createdAt(product.getCreatedAt())
			.expirationDate(product.getExpirationDate())
			.balance(product.getBalanceByLocalDate(today))
			.progress(product.getProgressByLocalDate(today))
			.remainingDays(product.getRemainingDaysByLocalDate(today))
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
		@CacheEvict(value = "productDetail", key = "#productId")
	})
	public void updateProduct(User user, String productId, FinancialProductRequestDto dto) {
		FinancialProduct existingProduct = findFinancialProduct(user, productId);
		FinancialProduct updatedProduct = financialProductFactory.createUpdatedProduct(existingProduct, dto);
		existingProduct.update(updatedProduct);
	}

	@Transactional
	@Caching(evict = {
		// 1. 해당 유저의 상품 요약 목록 캐시 삭제
		@CacheEvict(value = "productSummary", key = "#user.id"),
		// 2. 수정된 특정 상품의 상세 정보 캐시 삭제
		@CacheEvict(value = "productDetail", key = "#productId")
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
	public List<FinancialProductSummaryResponse> getSummaryProductsByUser(User user) {
		List<FinancialProductSummaryResponse> result = new ArrayList<>();
		List<FinancialProduct> products = repository.findAllByUserId(user.getId());

		LocalDate today = localDateProvider.now();
		for (FinancialProduct product : products) {
			FinancialProductSummaryResponse data = FinancialProductSummaryResponse.from(
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

	private Comparator<FinancialProductSummaryResponse> getFinancialProductSummaryResponseComparator() {
		return Comparator.comparing(FinancialProductSummaryResponse::getStartDate, Comparator.reverseOrder())
			.thenComparing(FinancialProductSummaryResponse::getExpirationDate,
				Comparator.nullsLast(Comparator.naturalOrder()))
			.thenComparing(FinancialProductSummaryResponse::getBalance, Comparator.reverseOrder())
			.thenComparing(FinancialProductSummaryResponse::getCreatedAt)
			.thenComparing(FinancialProductSummaryResponse::getId);
	}
}
