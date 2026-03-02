package co.invest72.financial_product.application;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.invest72.common.time.LocalDateProvider;
import co.invest72.financial_product.domain.FinancialProduct;
import co.invest72.financial_product.domain.FinancialProductRepository;
import co.invest72.financial_product.domain.SavingsProduct;
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
	public DetailedFinancialProductResponse getProductDetail(User user, String productId) {
		FinancialProduct product = findFinancialProduct(user, productId);
		LocalDate today = localDateProvider.now();
		Integer paymentDay = null;
		// todo: 각 상품 유형별로 paymentDay를 가져오는 로직이 중복되고 있는데, 이를 개선할 방법이 있을까?
		if (product instanceof SavingsProduct savingsProduct) {
			paymentDay = savingsProduct.getPaymentDay().getValue();
		}

		return DetailedFinancialProductResponse.builder()
			.id(product.getId())
			.userId(product.getUserId())
			.name(product.getName())
			.investmentType(product.getInvestmentType().name())
			.amount(product.getAmount().getValue())
			.months(product.getMonths().getValue())
			.paymentDay(paymentDay)
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

	@Transactional
	public void updateProduct(User user, String productId, FinancialProductRequestDto dto) {
		// 기존 상품 조회 및 검증
		FinancialProduct existingProduct = findFinancialProduct(user, productId);
		// 업데이트된 상품 정보로 새로운 객체 생성 (ID, userId, createdAt는 유지)
		FinancialProduct updatedProduct = financialProductFactory.create(user.getId(), dto);
		existingProduct.update(updatedProduct);
	}

	@Transactional
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
