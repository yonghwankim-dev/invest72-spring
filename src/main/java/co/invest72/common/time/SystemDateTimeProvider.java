package co.invest72.common.time;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class SystemDateTimeProvider implements LocalDateProvider {

	@Override
	public LocalDate now() {
		return LocalDate.now();
	}

	@Override
	public LocalDateTime nowDateTime() {
		return LocalDateTime.now();
	}
}
