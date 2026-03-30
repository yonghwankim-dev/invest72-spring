package co.invest72.financial_product.domain;

import co.invest72.financial_product.domain.entity.FinancialProductData;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("DEPOSIT")
@Getter
@SuperBuilder(toBuilder = true)
public class DepositProduct extends FinancialProduct {

	protected DepositProduct() {
	}

	public DepositProduct(FinancialProductData data) {
		super(data);
	}

	@Override
	public FinancialProduct update(FinancialProductData data) {
		throw new UnsupportedOperationException("todo implement");
	}

	@Override
	public void update(FinancialProduct updatedProduct) {
		validateOnUpdate(updatedProduct);
		super.update(updatedProduct);
	}

	private void validateOnUpdate(FinancialProduct updatedProduct) {
		if (!(updatedProduct instanceof DepositProduct)) {
			throw new IllegalArgumentException("업데이트된 상품은 DepositProduct여야 합니다.");
		}
	}
}
