package co.invest72.financial_product.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.invest72.financial_product.domain.FinancialProduct;
import co.invest72.financial_product.domain.FinancialProductRepository;
import co.invest72.financial_product.domain.ProductAmount;
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
			.user(user)
			.name(dto.getName())
			.productType(ProductType.valueOf(dto.getProductType()))
			.amount(new ProductAmount(dto.getAmount()))
			.months(dto.getMonths())
			.interestRate(new ProductRate(dto.getInterestRate()))
			.interestType(InterestType.valueOf(dto.getInterestType()))
			.taxType(TaxType.valueOf(dto.getTaxType()))
			.taxRate(new ProductRate(dto.getTaxRate()))
			.startDate(dto.getStartDate())
			.createdAt(LocalDateTime.now())
			.build();
		return repository.save(product);
	}

	public List<ProductResponseDto> getProductsByUser(User user) {
		List<ProductResponseDto> result = new ArrayList<>();
		List<FinancialProduct> products = repository.findAllById(user.getId());

		for (FinancialProduct product : products) {
			ProductResponseDto dto = ProductResponseDto.builder()
				.id(product.getId())
				.userId(product.getUser().getId())
				.name(product.getName())
				.productType(product.getProductType().name())
				.amount(product.getAmount().getValue().doubleValue())
				.months(product.getMonths())
				.interestRate(product.getInterestRate().getValue().doubleValue())
				.interestType(product.getInterestType().name())
				.taxType(product.getTaxType().name())
				.taxRate(product.getTaxRate().getValue().doubleValue())
				.startDate(product.getStartDate())
				.createdAt(product.getCreatedAt())
				.build();
			result.add(dto);
		}

		return result;
	}
}
