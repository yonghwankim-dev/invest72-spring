package co.invest72.financial_product.application;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.invest72.financial_product.domain.FinancialProduct;
import co.invest72.financial_product.domain.FinancialProductRepository;
import co.invest72.financial_product.domain.ProductAmount;
import co.invest72.financial_product.domain.ProductMonths;
import co.invest72.financial_product.domain.ProductRate;
import co.invest72.financial_product.presentation.dto.request.FinancialProductRequestDto;
import co.invest72.financial_product.presentation.dto.response.FinancialProductDto;
import co.invest72.financial_product.presentation.dto.response.FinancialProductSummaryResponse;
import co.invest72.investment.domain.interest.InterestType;
import co.invest72.investment.domain.investment.InvestmentType;
import co.invest72.investment.domain.tax.TaxType;
import co.invest72.user.domain.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FinancialProductService {

	private final FinancialProductRepository repository;

	@Transactional
	public String createProduct(User user, FinancialProductRequestDto dto) {
		FinancialProduct product = FinancialProduct.builder()
			.userId(user.getId())
			.name(dto.getName())
			.investmentType(InvestmentType.valueOf(dto.getInvestmentType()))
			.amount(new ProductAmount(dto.getAmount()))
			.months(new ProductMonths(dto.getMonths()))
			.interestRate(new ProductRate(dto.getInterestRate()))
			.interestType(InterestType.valueOf(dto.getInterestType()))
			.taxType(TaxType.valueOf(dto.getTaxType()))
			.taxRate(new ProductRate(dto.getTaxRate()))
			.startDate(dto.getStartDate())
			.createdAt(LocalDateTime.now())
			.build();
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
	public FinancialProductDto getProductDetail(User user, String productId) {
		FinancialProduct product = findFinancialProduct(user, productId);
		return buildProductResponseDto(product);
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
		FinancialProduct updatedProduct = existingProduct.toBuilder()
			.name(dto.getName())
			.investmentType(InvestmentType.valueOf(dto.getInvestmentType()))
			.amount(new ProductAmount(dto.getAmount()))
			.months(new ProductMonths(dto.getMonths()))
			.interestRate(new ProductRate(dto.getInterestRate()))
			.interestType(InterestType.valueOf(dto.getInterestType()))
			.taxType(TaxType.valueOf(dto.getTaxType()))
			.taxRate(new ProductRate(dto.getTaxRate()))
			.startDate(dto.getStartDate())
			.build();
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
	 * @param user 조회 대상 사용자
	 * @return 상품 요약 정보 리스트
	 */
	@Transactional(readOnly = true)
	public List<FinancialProductSummaryResponse> getSummaryProductsByUser(User user) {
		List<FinancialProductSummaryResponse> result = new ArrayList<>();
		// 기본적으로 최근 생성된 상품이 먼저 보이도록 정렬
		List<FinancialProduct> products = repository.findAllByUserId(user.getId());

		for (FinancialProduct product : products) {
			FinancialProductSummaryResponse data = null;
			if (product.getInvestmentType() == InvestmentType.CASH) {
				LocalDate expirationDate = null;
				BigDecimal expectedInterest = BigDecimal.ZERO;
				BigDecimal progress = BigDecimal.ONE;
				long remainingDays = 0;
				data = FinancialProductSummaryResponse.builder()
					.id(product.getId())
					.name(product.getName())
					.investmentType(product.getInvestmentType().name())
					.interestRate(product.getInterestRate().getValue())
					.startDate(product.getStartDate())
					.expirationDate(expirationDate)
					.balance(product.getAmount().getValue())
					.expectedInterest(expectedInterest)
					.progress(progress)
					.remainingDays(remainingDays)
					.createAt(product.getCreatedAt())
					.build();
			} else if (product.getInvestmentType() == InvestmentType.DEPOSIT) {
				LocalDate today = LocalDate.of(2026, 2, 27);
				LocalDate expirationDate = product.getExpirationDate();
				BigDecimal expectedInterest = BigDecimal.valueOf(50_000);
				BigDecimal progress = product.getProgressByLocalDate(today);
				long remainingDays = product.getRemainingDaysByLocalDate(today);
				data = FinancialProductSummaryResponse.builder()
					.id(product.getId())
					.name(product.getName())
					.investmentType(product.getInvestmentType().name())
					.interestRate(product.getInterestRate().getValue())
					.startDate(product.getStartDate())
					.expirationDate(expirationDate)
					.balance(product.getAmount().getValue())
					.expectedInterest(expectedInterest)
					.progress(progress)
					.remainingDays(remainingDays)
					.createAt(product.getCreatedAt())
					.build();
			}
			result.add(data);
		}

		return result.stream()
			.sorted(Comparator.comparing(FinancialProductSummaryResponse::getStartDate, Comparator.reverseOrder())
				.thenComparing(FinancialProductSummaryResponse::getExpirationDate,
					Comparator.nullsLast(Comparator.naturalOrder()))
				.thenComparing(FinancialProductSummaryResponse::getBalance, Comparator.reverseOrder())
				.thenComparing(FinancialProductSummaryResponse::getCreateAt))
			.toList();
	}
}
