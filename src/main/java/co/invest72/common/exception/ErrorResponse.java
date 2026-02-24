package co.invest72.common.exception;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
	private final int status;
	private final String message;
	private final List<FieldError> errors;

	@Getter
	public static class FieldError {
		private final String field;
		private final String rejectedValue;
		private final String message;

		public FieldError(String field, String rejectedValue, String message) {
			this.field = field;
			this.rejectedValue = rejectedValue;
			this.message = message;
		}

	}
}
