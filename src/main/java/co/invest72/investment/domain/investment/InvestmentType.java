package co.invest72.investment.domain.investment;

import lombok.Getter;

@Getter
public enum InvestmentType {
	CASH("현금"),
	DEPOSIT("예금"),
	SAVINGS("적금");

	private final String typeName;

	InvestmentType(String typeName) {
		this.typeName = typeName;
	}

	public static InvestmentType from(String type) {
		for (InvestmentType investmentType : values()) {
			if (investmentType.typeName.equalsIgnoreCase(type)) {
				return investmentType;
			}
		}
		throw new IllegalArgumentException("Unknown investment type: " + type);
	}

}
