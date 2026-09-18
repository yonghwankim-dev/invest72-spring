package co.invest72.rp.presentation;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.invest72.financial_product.presentation.dto.request.RpCreateRequest;
import co.invest72.rp.application.RpService;
import co.invest72.security.PrincipalUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/rp")
@RequiredArgsConstructor
public class RpRestController {

	private final RpService service;

	@PostMapping
	public ResponseEntity<Map<String, Object>> createRp(@AuthenticationPrincipal PrincipalUser user, @Valid @RequestBody
	RpCreateRequest request) {
		String id = service.createRp(user.getUser(), request);
		Map<String, Object> responseBody = Map.of("id", id);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(responseBody);
	}
}
