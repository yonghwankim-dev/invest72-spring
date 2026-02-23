package co.invest72.financial_product.infrastructure;

import java.util.UUID;

import org.springframework.stereotype.Component;

import co.invest72.financial_product.domain.IdGenerator;

@Component
public class UuidGenerator implements IdGenerator {

	@Override
	public String generateId() {
		return UUID.randomUUID().toString();
	}
}
