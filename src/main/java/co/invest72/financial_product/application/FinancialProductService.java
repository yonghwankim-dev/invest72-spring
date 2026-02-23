package co.invest72.financial_product.application;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.invest72.financial_product.domain.FinancialProduct;
import co.invest72.financial_product.domain.FinancialProductRepository;
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
			.amount(BigDecimal.valueOf(dto.getAmount()))
			.months(dto.getMonths())
			.interestRate(BigDecimal.valueOf(dto.getInterestRate()))
			.interestType(InterestType.valueOf(dto.getInterestType()))
			.taxType(TaxType.valueOf(dto.getTaxType()))
			.taxRate(BigDecimal.valueOf(dto.getTaxRate()))
			.build();
		return repository.save(product);
	}

	public List<ProductResponseDto> getProductsByUser(User user) {
		List<FinancialProduct> products = repository.findAllById(user.getId());
		return products.stream()
			.map(ProductResponseDto::from)
			.toList();
	}
}
