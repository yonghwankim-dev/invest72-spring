package co.invest72.user.infrastructure;

import java.util.UUID;

import org.springframework.stereotype.Component;

import co.invest72.user.domain.UuidGenerator;

@Component
public class DefaultUuidGenerator implements UuidGenerator {
	@Override
	public String generate() {
		return UUID.randomUUID().toString();
	}
}
