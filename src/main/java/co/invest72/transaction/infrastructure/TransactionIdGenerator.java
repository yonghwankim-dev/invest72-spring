package co.invest72.transaction.infrastructure;

import java.util.Objects;
import java.util.UUID;

import co.invest72.financial_product.domain.IdGenerator;

public class TransactionIdGenerator implements IdGenerator {

	private final String prefix;

	public TransactionIdGenerator(String prefix) {
		this.prefix = Objects.requireNonNull(prefix);
		if (prefix.isBlank()) {
			throw new IllegalArgumentException("Prefix cannot be null or empty");
		}
	}

	@Override
	public String generateId() {
		return prefix + "-" + UUID.randomUUID();
	}
}
