package co.invest72.financial_product.presentation;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.invest72.financial_product.application.FinancialProductService;
import co.invest72.financial_product.presentation.dto.request.CreateFinancialProductDto;
import co.invest72.financial_product.presentation.dto.response.CreateFinancialProductResponse;
import co.invest72.financial_product.presentation.dto.response.ProductResponseDto;
import co.invest72.security.PrincipalUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class FinancialProductRestController {

	private final FinancialProductService service;

	// 상품 생성
	@PostMapping
	public ResponseEntity<CreateFinancialProductResponse> createProduct(@AuthenticationPrincipal PrincipalUser user,
		@Valid @RequestBody CreateFinancialProductDto dto) {
		String id = service.createProduct(user.getUser(), dto);
		CreateFinancialProductResponse response = new CreateFinancialProductResponse(id);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(response);
	}

	// 상품 목록 조회
	@GetMapping
	public ResponseEntity<List<ProductResponseDto>> getProducts(@AuthenticationPrincipal PrincipalUser user) {
		return ResponseEntity.ok(service.getProductsByUser(user.getUser()));
	}

	// 상품 상세 조회
	@GetMapping("/{id}")
	public ResponseEntity<ProductResponseDto> getProductDetail(@AuthenticationPrincipal PrincipalUser user,
		@PathVariable String id) {
		return ResponseEntity.ok(service.getProductDetail(user.getUser(), id));
	}

	// 상품 수정
	@PutMapping("/{id}")
	public ResponseEntity<Void> updateProduct(@AuthenticationPrincipal PrincipalUser user, @PathVariable String id,
		@Valid @RequestBody CreateFinancialProductDto dto) {
		service.updateProduct(user.getUser(), id, dto);
		return ResponseEntity.noContent().build();
	}

	// 상품 삭제
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteProduct(@AuthenticationPrincipal PrincipalUser user, @PathVariable String id) {
		service.deleteProduct(user.getUser(), id);
		return ResponseEntity.noContent().build();
	}
}
