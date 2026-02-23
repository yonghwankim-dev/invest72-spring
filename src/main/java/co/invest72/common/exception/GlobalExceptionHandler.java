package co.invest72.common.exception;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
		MethodArgumentNotValidException e) {
		List<ErrorResponse.FieldError> fieldErrors = e.getBindingResult().getFieldErrors().stream()
			.map(error -> new ErrorResponse.FieldError(
				error.getField(),
				String.valueOf(error.getRejectedValue()),
				error.getDefaultMessage()
			))
			.toList();
		ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Invalid Input", fieldErrors);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(response);
	}
}
