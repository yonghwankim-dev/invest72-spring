package co.invest72.common.exception;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
		MethodArgumentNotValidException e) {
		List<ErrorResponse.FieldError> fieldErrors = e.getBindingResult().getFieldErrors().stream()
			.map(error -> {
				String rejectedValue = error.getRejectedValue().toString();
				return new ErrorResponse.FieldError(
					error.getField(),
					rejectedValue,
					error.getDefaultMessage()
				);
			})
			.toList();
		ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Invalid Input", fieldErrors);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(response);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	protected ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
		log.warn("IllegalArgumentException: {}", e.getMessage());
		ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Invalid request", List.of());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(response);
	}
}
