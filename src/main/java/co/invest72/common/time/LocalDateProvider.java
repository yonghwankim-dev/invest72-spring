package co.invest72.common.time;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface LocalDateProvider {
	LocalDate now();

	LocalDateTime nowDateTime();
}
