package co.invest72.financial_product.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("DEPOSIT")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SuperBuilder(toBuilder = true)
public class DepositProduct extends FinancialProduct {

	@Override
	public BigDecimal getBalanceByLocalDate(LocalDate today) {
		return getInvestmentType().calculateBalance(this, today);
	}
}
