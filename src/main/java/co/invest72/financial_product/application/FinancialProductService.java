package co.invest72.financial_product.application;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.invest72.financial_product.domain.FinancialProduct;
import co.invest72.financial_product.domain.FinancialProductRepository;
import co.invest72.financial_product.domain.ProductAmount;
import co.invest72.financial_product.domain.ProductMonths;
import co.invest72.financial_product.domain.ProductRate;
import co.invest72.financial_product.domain.ProductType;
import co.invest72.financial_product.presentation.dto.request.CreateFinancialProductDto;
import co.invest72.financial_product.presentation.dto.response.ProductResponseDto;
import co.invest72.investment.domain.interest.InterestType;
import co.invest72.investment.domain.tax.TaxType;
import co.invest72.user.domain.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FinancialProductService {

	private final FinancialProductRepository repository;

	@Transactional
	public String createProduct(User user, CreateFinancialProductDto dto) {
		FinancialProduct product = FinancialProduct.builder()
			.userId(user.getId())
			.name(dto.getName())
			.productType(ProductType.valueOf(dto.getProductType()))
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
	public List<ProductResponseDto> getProductsByUser(User user) {
		return repository.findAllByUserId(user.getId()).stream()
			.map(this::buildProductResponseDto)
			.toList();
	}

	private ProductResponseDto buildProductResponseDto(FinancialProduct product) {
		return ProductResponseDto.builder()
			.id(product.getId())
			.userId(product.getUserId())
			.name(product.getName())
			.productType(product.getProductType().name())
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
	public ProductResponseDto getProductDetail(User user, String productId) {
		FinancialProduct product = repository.findByProductId(productId);
		if (product == null || !product.getUserId().equals(user.getId())) {
			throw new IllegalArgumentException("상품을 찾을 수 없거나 접근 권한이 없습니다.");
		}
		return buildProductResponseDto(product);
	}

	@Transactional
	public void deleteProduct(User user, String productId) {
		FinancialProduct product = repository.findByProductId(productId);
		if (product == null || !product.getUserId().equals(user.getId())) {
			throw new IllegalArgumentException("상품을 찾을 수 없거나 접근 권한이 없습니다.");
		}
		repository.deleteByProductId(user.getId(), productId);
	}
}
