package co.invest72.transaction.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import co.invest72.security.PrincipalUser;
import co.invest72.transaction.application.TransactionService;
import co.invest72.transaction.dto.TransactionDto;
import co.invest72.transaction.presentation.vo.TransactionRequest;
import co.invest72.transaction.presentation.vo.TransactionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TransactionRestController {
	private final TransactionService service;

	@PostMapping("/api/v1/transactions")
	public ResponseEntity<TransactionResponse> saveTransaction(@AuthenticationPrincipal PrincipalUser user,
		@RequestBody @Valid TransactionRequest request) {
		TransactionDto dto = TransactionDto.builder()
			.type(request.getType())
			.amount(request.getAmount())
			.content(request.getContent())
			.userId(user.getUser().getId())
			.build();
		String transactionId = service.save(dto);
		TransactionResponse response = new TransactionResponse(transactionId);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
}
