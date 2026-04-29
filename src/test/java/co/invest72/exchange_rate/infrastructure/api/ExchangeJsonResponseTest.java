package co.invest72.exchange_rate.infrastructure.api;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

class ExchangeJsonResponseTest {

	@DisplayName("역직렬화 테스트")
	@Test
	void deserialize() throws JsonProcessingException {
		// given
		ObjectMapper objectMapper = new ObjectMapper();
		String json = "{\"result\":1,\"cur_unit\":\"KRW\",\"ttb\":\"0\",\"tts\":\"0\",\"deal_bas_r\":\"1\",\"bkpr\":\"1\",\"yy_efee_r\":\"0\",\"ten_dd_efee_r\":\"0\",\"kftc_bkpr\":\"1\",\"kftc_deal_bas_r\":\"1\",\"cur_nm\":\"한국 원\"}";
		// when
		ExchangeJsonResponse response = objectMapper.readValue(json, ExchangeJsonResponse.class);
		// then
		ExchangeJsonResponse expected = new ExchangeJsonResponse(1, "KRW", "1", "한국 원");
		Assertions.assertThat(response).isEqualTo(expected);
	}

}
