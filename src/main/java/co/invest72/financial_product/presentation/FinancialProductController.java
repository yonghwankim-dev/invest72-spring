package co.invest72.financial_product.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.invest72.financial_product.application.FinancialProductService;
import co.invest72.financial_product.domain.FinancialProduct;
import co.invest72.financial_product.presentation.dto.request.CreateFinancialProductRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class FinancialProductController {

	private final FinancialProductService service;

	// 상품 생성
	@PostMapping
	public ResponseEntity<String> createProduct(@RequestBody CreateFinancialProductRequest request) {
		FinancialProduct newProduct = request.toEntity();
		String id = service.createProduct(newProduct);
		return ResponseEntity.ok(id);
	}
	// 상품 목록 조회
	// 상품 상세 조회
	// 상품 수정
	// 상품 삭제
}
