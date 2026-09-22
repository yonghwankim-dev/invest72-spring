package co.invest72.rp.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.invest72.rp.application.RpService;
import co.invest72.rp.presentation.dto.RpCreateRequest;
import co.invest72.rp.presentation.dto.RpCreateResponse;
import co.invest72.rp.presentation.dto.RpDetailedResponse;
import co.invest72.security.PrincipalUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/rp")
@RequiredArgsConstructor
public class RpRestController {

	private final RpService service;

	@PostMapping
	public ResponseEntity<RpCreateResponse> createRp(@AuthenticationPrincipal PrincipalUser user, @Valid @RequestBody
	RpCreateRequest request) {
		String id = service.createRp(user.getUser(), request);
		RpCreateResponse response = new RpCreateResponse(id);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(response);
	}

	@GetMapping
	public ResponseEntity<RpDetailedResponse> getRp(@RequestParam String id) {
		RpDetailedResponse response = service.getRp(id);
		return ResponseEntity.ok()
			.body(response);
	}
}
