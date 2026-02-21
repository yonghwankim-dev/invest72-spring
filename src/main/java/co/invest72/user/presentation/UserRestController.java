package co.invest72.user.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import co.invest72.security.PrincipalUser;
import co.invest72.user.application.UserService;
import co.invest72.user.domain.User;
import co.invest72.user.presentation.response.UserResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserRestController {

	private final UserService service;

	@GetMapping("/api/v1/users/me")
	public ResponseEntity<UserResponse> getUserInfo(@AuthenticationPrincipal PrincipalUser principal) {
		User user = service.getUserById(principal.getName());
		UserResponse response = new UserResponse(user.getId(), user.getEmail(), user.getNickname());
		return ResponseEntity.ok(response);
	}
}
