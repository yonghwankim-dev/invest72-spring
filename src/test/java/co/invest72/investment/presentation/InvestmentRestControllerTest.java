package co.invest72.investment.presentation;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import co.invest72.investment.presentation.request.MonthlyCompoundInterestCalculateRequest;
import util.TestFileUtils;

@SpringBootTest
class InvestmentRestControllerTest {
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private WebApplicationContext context;

	public static Stream<Arguments> validCalculateInvestmentRequests() {
		String filePath = "src/test/resources/calculate_investment_request/data.csv";
		List<Map<String, Object>> jsonList = TestFileUtils.readCsvFile(filePath);
		List<Arguments> arguments = new ArrayList<>();
		for (Map<String, Object> data : jsonList) {
			Map<String, Object> request = new LinkedHashMap<>();
			Map<String, Object> expected = new LinkedHashMap<>();
			for (Map.Entry<String, Object> entry : data.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				if (key.startsWith("expected")) {
					expected.put(key, value);
				} else {
					request.put(key, value);
				}
			}
			arguments.add(Arguments.of(request, expected));
		}
		return arguments.stream();
	}

	public static Stream<Arguments> invalidCalculateInvestmentRequests() {
		String filePath = "src/test/resources/calculate_investment_request/invalid_data.csv";
		return TestFileUtils.readCsvFile(filePath).stream()
			.map(Arguments::of);
	}

	private MockMvc createMockMvc() {
		return MockMvcBuilders.webAppContextSetup(context)
			.alwaysDo(print())
			.build();
	}

	@BeforeEach
	void setUp() {
		this.mockMvc = createMockMvc();
	}

	@ParameterizedTest
	@MethodSource(value = "validCalculateInvestmentRequests")
	void calculateExpiration(Map<String, Object> request, Map<String, Object> expected) throws Exception {
		mockMvc.perform(post("/investments/calculate/expiration")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.totalInvestment").value(expected.get("expectedTotalInvestment")))
			.andExpect(jsonPath("$.totalInterest").value(expected.get("expectedTotalInterest")))
			.andExpect(jsonPath("$.totalTax").value(expected.get("expectedTotalTax")))
			.andExpect(jsonPath("$.totalProfit").value(expected.get("expectedTotalProfit")))
			.andExpect(jsonPath("$.taxType").value(expected.get("expectedTaxType")))
			.andExpect(jsonPath("$.taxPercent").value(expected.get("expectedTaxPercent")));
	}

	@ParameterizedTest
	@MethodSource(value = "invalidCalculateInvestmentRequests")
	void calculateExpiration_whenInvalidRequest_thenReturnErrorResponse(Map<String, Object> request) throws Exception {
		mockMvc.perform(post("/investments/calculate/expiration")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest());
	}

	@ParameterizedTest
	@MethodSource(value = "validCalculateInvestmentRequests")
	void calculateMonthly(Map<String, Object> request, Map<String, Object> expected) throws Exception {
		mockMvc.perform(post("/investments/calculate/monthly")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.totalInvestment")
				.value(expected.get("expectedTotalInvestment")))
			.andExpect(jsonPath("$.totalPrincipal")
				.value(expected.get("expectedTotalPrincipal")))
			.andExpect(jsonPath("$.totalInterest")
				.value(expected.get("expectedTotalInterest")))
			.andExpect(jsonPath("$.totalTax")
				.value(expected.get("expectedTotalTax")))
			.andExpect(jsonPath("$.totalProfit")
				.value(expected.get("expectedTotalProfit")))
			.andExpect(jsonPath("$.taxType")
				.value(expected.get("expectedTaxType")))
			.andExpect(jsonPath("$.taxPercent")
				.value(expected.get("expectedTaxPercent")));
	}

	@ParameterizedTest
	@MethodSource(value = "invalidCalculateInvestmentRequests")
	void calculateMonthly_whenInvalidRequest_thenReturnErrorResponse(Map<String, Object> request) throws Exception {
		mockMvc.perform(post("/investments/calculate/monthly")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest());
	}

	@Test
	void calculateMonthlyCompoundInterest() throws Exception {
		MonthlyCompoundInterestCalculateRequest request = MonthlyCompoundInterestCalculateRequest.builder()
			.initialAmount(0)
			.monthlyDeposit(1_000_000)
			.investmentYears(1)
			.annualInterestRate(0.05)
			.compoundingMethod("monthly")
			.build();

		mockMvc.perform(post("/investments/calculate/monthly-compound-interest")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.totalInvestment").value(equalTo(11_000_000)))
			.andExpect(jsonPath("$.totalInterest").value(equalTo(278_855)))
			.andExpect(jsonPath("$.totalProfit").value(equalTo(11_278_855)))
			.andExpect(jsonPath("$.details").isArray());
	}
}
