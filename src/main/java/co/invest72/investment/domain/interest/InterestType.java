package co.invest72.investment.domain.interest;

public enum InterestType {
	NONE("없음"), // 없음
	SIMPLE("단리"), // 단리
	COMPOUND("복리") // 복리
	;

	private final String typeName;

	InterestType(String typeName) {
		this.typeName = typeName;
	}

	public static InterestType from(String interestType) {
		for (InterestType type : values()) {
			if (type.typeName.equalsIgnoreCase(interestType)) {
				return type;
			}
		}
		throw new IllegalArgumentException("Unknown totalInterest type: " + interestType);
	}

	public String getTypeName() {
		return typeName;
	}
}
