package co.invest72.rp.presentation.dto;

import co.invest72.rp.entity.RepurchaseAgreementEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class RpDetailedResponse {
	private String id;

	public static RpDetailedResponse fromEntity(RepurchaseAgreementEntity rp) {
		return RpDetailedResponse.builder()
			.id(rp.getId())
			.build();
	}
}
