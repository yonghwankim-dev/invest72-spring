package co.invest72.financial_product.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public interface FinancialProductData {

	String getName();

	String getInvestmentType();

	BigDecimal getAmount();

	Integer getMonths();

	Optional<Integer> getPaymentDay();

	BigDecimal getInterestRate();

	String getInterestType();

	String getTaxType();

	BigDecimal getTaxRate();

	LocalDate getStartDate();

	String getCurrencyCode();

	Optional<String> getProductId();

	Optional<String> getUserId();

	Optional<LocalDateTime> getCreatedAt();

	FinancialProductData withProductId(String productId);

	FinancialProductData withUserId(String userId);

	FinancialProductData withCreatedAt(LocalDateTime createdAt);
}
