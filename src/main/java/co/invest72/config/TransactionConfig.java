package co.invest72.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.invest72.transaction.infrastructure.repository.InMemoryTransactionRepository;

@Configuration
public class TransactionConfig {
	@Bean
	public InMemoryTransactionRepository inMemoryTransactionRepository() {
		return new InMemoryTransactionRepository();
	}
}
