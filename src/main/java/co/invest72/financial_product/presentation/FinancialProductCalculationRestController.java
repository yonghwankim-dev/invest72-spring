package co.invest72.financial_product.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.invest72.financial_product.application.FinancialProductService;
import co.invest72.financial_product.domain.FinancialProduct;
import co.invest72.investment.application.InvestmentFactory;
import co.invest72.security.PrincipalUser;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class FinancialProductCalculationRestController {
	private final FinancialProductService financialProductService;
	private final InvestmentFactory investmentFactory;

	@GetMapping("/{id}/calculate")
	public ResponseEntity<Object> calculateFinancialProduct(@AuthenticationPrincipal PrincipalUser user,
		@PathVariable String id) {
		// TODO: 상품 계산 로직 구현
		// 1. 상품 조회
		FinancialProduct product = financialProductService.getProductByUserAndProductId(user.getUser(), id);
		// 2. 계산 로직 수행

		return ResponseEntity.ok().body("계산 결과");
	}
}
