package co.invest72.transaction.presentation;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.invest72.security.PrincipalUser;
import co.invest72.transaction.application.TransactionService;
import co.invest72.transaction.domain.TransactionType;
import co.invest72.transaction.dto.TransactionDto;
import co.invest72.transaction.presentation.vo.TransactionListResponse;
import co.invest72.transaction.presentation.vo.TransactionRequest;
import co.invest72.transaction.presentation.vo.TransactionResponse;
import co.invest72.transaction.presentation.vo.TransactionSaveResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TransactionRestController {
	private final TransactionService service;

	@PostMapping("/api/v1/transactions")
	public ResponseEntity<TransactionSaveResponse> saveTransaction(@AuthenticationPrincipal PrincipalUser user,
		@RequestBody @Valid TransactionRequest request) {
		TransactionDto dto = TransactionDto.builder()
			.type(request.getType())
			.amount(request.getAmount())
			.content(request.getContent())
			.userId(user.getUser().getId())
			.build();
		String transactionId = service.save(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(new TransactionSaveResponse(transactionId));
	}

	@GetMapping("/api/v1/transactions")
	public ResponseEntity<TransactionListResponse> getTransactions(@AuthenticationPrincipal PrincipalUser user,
		@RequestParam(name = "type") String type) {
		List<TransactionDto> transactions = service.getTransactions(TransactionType.valueOf(type),
			user.getUser().getId());
		List<TransactionResponse> responseList = transactions.stream()
			.map(t -> TransactionResponse.builder()
				.transactionId(t.getTransactionId())
				.type(t.getType())
				.amount(t.getAmount())
				.content(t.getContent())
				.createdAt(t.getCreatedAt())
				.build())
			.toList();
		TransactionListResponse response = new TransactionListResponse(responseList);
		return ResponseEntity.ok(response);
	}
}
