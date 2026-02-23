package co.invest72.financial_product.domain;

public enum ProductType {
	CASH("현금"), DEPOSIT("예금"), SAVINGS("적금");

	private final String description;

	ProductType(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "금융 상품 유형(%s)".formatted(description);
	}
}
