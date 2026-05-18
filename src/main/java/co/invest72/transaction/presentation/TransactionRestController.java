package co.invest72.transaction.presentation;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.invest72.security.PrincipalUser;
import co.invest72.transaction.application.TransactionService;
import co.invest72.transaction.domain.TransactionType;
import co.invest72.transaction.dto.TransactionDto;
import co.invest72.transaction.presentation.vo.TransactionDeleteRequest;
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
			.type(request.getType().name())
			.amount(request.getAmount())
			.content(request.getContent())
			.userId(user.getUser().getId())
			.build();
		String transactionId = service.save(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(new TransactionSaveResponse(transactionId));
	}

	@GetMapping("/api/v1/transactions")
	public ResponseEntity<TransactionListResponse> getTransactions(@AuthenticationPrincipal PrincipalUser user,
		@RequestParam(name = "type") TransactionType type) {
		List<TransactionDto> transactions = service.getTransactions(type, user.getUser().getId());
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

	@GetMapping("/api/v1/transactions/{transactionId}")
	public ResponseEntity<TransactionResponse> getDetailedTransaction(@AuthenticationPrincipal PrincipalUser user,
		@PathVariable("transactionId") String transactionId) {
		TransactionDto dto = service.getDetailedTransaction(transactionId, user.getUser().getId());
		TransactionResponse response = TransactionResponse.builder()
			.transactionId(dto.getTransactionId())
			.type(dto.getType())
			.amount(dto.getAmount())
			.content(dto.getContent())
			.createdAt(dto.getCreatedAt())
			.build();
		return ResponseEntity.ok(response);
	}

	@PutMapping("/api/v1/transactions/{transactionId}")
	public ResponseEntity<Void> updateTransaction(@AuthenticationPrincipal PrincipalUser user,
		@PathVariable("transactionId") String transactionId, @RequestBody @Valid TransactionRequest request) {
		TransactionDto dto = TransactionDto.builder()
			.type(request.getType().name())
			.amount(request.getAmount())
			.content(request.getContent())
			.userId(user.getUser().getId())
			.build();
		service.update(dto, transactionId, user.getUser().getId());

		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/api/v1/transactions")
	public ResponseEntity<Void> deleteTransactions(@AuthenticationPrincipal PrincipalUser user,
		@RequestBody @Valid TransactionDeleteRequest request) {
		service.delete(request.getTransactionIds(), user.getUser().getId());
		return ResponseEntity.noContent().build();
	}
}
