package co.invest72.financial_product.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.invest72.financial_product.application.FinancialProductCalculationService;
import co.invest72.financial_product.application.FinancialProductService;
import co.invest72.financial_product.domain.FinancialProduct;
import co.invest72.financial_product.presentation.dto.response.FinancialProductCalculationResponseDto;
import co.invest72.investment.application.InvestmentFactory;
import co.invest72.investment.domain.Investment;
import co.invest72.security.PrincipalUser;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class FinancialProductCalculationRestController {
	private final FinancialProductService financialProductService;
	private final InvestmentFactory investmentFactory;
	private final FinancialProductCalculationService service;

	// TODO: 복리도 계산할 수 있도록 수정 필요
	@GetMapping("/{id}/calculate")
	public ResponseEntity<FinancialProductCalculationResponseDto> calculateFinancialProduct(
		@AuthenticationPrincipal PrincipalUser user,
		@PathVariable String id) {
		// 1. 상품 조회
		FinancialProduct product = financialProductService.getProductByUserAndProductId(user.getUser(), id);
		// 2. 계산 로직 수행
		Investment investment = investmentFactory.createBy(product);
		FinancialProductCalculationResponseDto response = service.calculate(investment);
		return ResponseEntity.ok().body(response);
	}
}
