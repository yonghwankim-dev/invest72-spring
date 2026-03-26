package co.invest72.financial_product.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public interface FinancialProductData {

	Optional<String> getId();

	Optional<String> getUserId();

	String getName();

	String getInvestmentType();

	BigDecimal getAmount();

	Integer getMonths();

	Integer getPaymentDay();

	BigDecimal getInterestRate();

	String getInterestType();

	String getTaxType();

	BigDecimal getTaxRate();

	LocalDate getStartDate();

	String getCurrencyCode();

	LocalDateTime getCreatedAt();

	FinancialProductData withProductId(String productId);

	FinancialProductData withUserId(String userId);

	FinancialProductData withCreatedAt(LocalDateTime createdAt);
}
