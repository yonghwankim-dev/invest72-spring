package co.invest72.financial_product.presentation;

import static co.invest72.investment.domain.interest.InterestType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import co.invest72.financial_product.domain.DepositProduct;
import co.invest72.financial_product.domain.FinancialProduct;
import co.invest72.financial_product.domain.FinancialProductRepository;
import co.invest72.financial_product.domain.ProductAmount;
import co.invest72.financial_product.domain.ProductInvestmentType;
import co.invest72.financial_product.domain.ProductMonths;
import co.invest72.investment.domain.interest.AnnualInterestRate;
import co.invest72.investment.domain.investment.InvestmentType;
import co.invest72.investment.domain.tax.FixedTaxRate;
import co.invest72.investment.domain.tax.TaxType;
import co.invest72.investment.presentation.response.InvestmentCurrency;
import co.invest72.money.domain.Currency;
import co.invest72.security.PrincipalUser;
import co.invest72.user.domain.User;
import source.FinancialProductDataProvider;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class FinancialProductCalculationRestControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private FinancialProductRepository financialProductRepository;

	private PrincipalUser principalUser;

	@BeforeEach
	void setUp() {
		String email = "user1@gmail.com";
		String nickname = "user1";
		String providerId = UUID.randomUUID().toString();
		User testUser = new User(email, nickname, providerId);
		principalUser = PrincipalUser.of()
			.user(testUser)
			.build();
	}

	@AfterEach
	void tearDown() {
		financialProductRepository.clear();
	}

	@DisplayName("상품 수익 계산 - 단리-예금")
	@Test
	void calculateFinancialProduct_whenProductIsSimpleDeposit_thenReturnsCalculationResult() throws Exception {
		// Given
		FinancialProduct product = DepositProduct.builder()
			.userId(principalUser.getUser().getId())
			.name("단리-예금")
			.investmentType(InvestmentType.DEPOSIT)
			.productInvestmentType(ProductInvestmentType.from(InvestmentType.DEPOSIT.name()))
			.amount(ProductAmount.won(BigDecimal.valueOf(1_000_000)))
			.months(new ProductMonths(12))
			.interestRate(new AnnualInterestRate(BigDecimal.valueOf(0.05)))
			.interestType(SIMPLE)
			.taxType(TaxType.NON_TAX)
			.taxRate(new FixedTaxRate(BigDecimal.ZERO))
			.startDate(LocalDate.now())
			.createdAt(LocalDateTime.now())
			.build();
		financialProductRepository.save(product);
		String productId = product.getId();

		InvestmentCurrency investmentCurrency = InvestmentCurrency.from(Currency.won());
		// When & Then
		mockMvc.perform(get("/api/v1/products/{id}/calculate", productId)
				.with(SecurityMockMvcRequestPostProcessors.user(principalUser)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.totalInvestment").value(1_000_000))
			.andExpect(jsonPath("$.totalInterest").value(50_000))
			.andExpect(jsonPath("$.totalTax").value(0))
			.andExpect(jsonPath("$.totalProfit").value(1_050_000))
			.andExpect(jsonPath("$.taxType").value("비과세"))
			.andExpect(jsonPath("$.taxPercent").value("0%"))
			.andExpect(jsonPath("$.monthlyDetails").isArray())
			.andExpect(jsonPath("$.yearlyDetails").isArray())
			.andExpect(jsonPath("$.investmentCurrency.code").value(investmentCurrency.getCode()))
			.andExpect(jsonPath("$.investmentCurrency.unit").value(investmentCurrency.getUnit()))
			.andExpect(jsonPath("$.investmentCurrency.name").value(investmentCurrency.getName()))
			.andDo(MockMvcResultHandlers.print());
	}

	@DisplayName("상품 수익 계산 - 달러-단리-예금")
	@Test
	void calculateFinancialProduct_whenCurrencyIsDollarAndProductIsSimpleFixedDeposit_thenReturnCalculationResult() throws
		Exception {
		// Given
		FinancialProduct product = DepositProduct.builder()
			.userId(principalUser.getUser().getId())
			.name("단리-예금")
			.investmentType(InvestmentType.DEPOSIT)
			.productInvestmentType(ProductInvestmentType.from(InvestmentType.DEPOSIT.name()))
			.amount(ProductAmount.dollar(BigDecimal.valueOf(1_000_000)))
			.months(new ProductMonths(12))
			.interestRate(new AnnualInterestRate(BigDecimal.valueOf(0.05)))
			.interestType(SIMPLE)
			.taxType(TaxType.NON_TAX)
			.taxRate(new FixedTaxRate(BigDecimal.ZERO))
			.startDate(LocalDate.now())
			.createdAt(LocalDateTime.now())
			.build();
		financialProductRepository.save(product);
		String productId = product.getId();

		InvestmentCurrency investmentCurrency = InvestmentCurrency.from(Currency.dollar());
		// When & Then
		mockMvc.perform(get("/api/v1/products/{id}/calculate", productId)
				.with(SecurityMockMvcRequestPostProcessors.user(principalUser)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.totalInvestment").value(1_000_000))
			.andExpect(jsonPath("$.totalInterest").value(50_000))
			.andExpect(jsonPath("$.totalTax").value(0))
			.andExpect(jsonPath("$.totalProfit").value(1_050_000))
			.andExpect(jsonPath("$.taxType").value("비과세"))
			.andExpect(jsonPath("$.taxPercent").value("0%"))
			.andExpect(jsonPath("$.monthlyDetails").isArray())
			.andExpect(jsonPath("$.yearlyDetails").isArray())
			.andExpect(jsonPath("$.investmentCurrency.code").value(investmentCurrency.getCode()))
			.andExpect(jsonPath("$.investmentCurrency.unit").value(investmentCurrency.getUnit()))
			.andExpect(jsonPath("$.investmentCurrency.name").value(investmentCurrency.getName()))
			.andDo(MockMvcResultHandlers.print());
	}

	@DisplayName("상품 수익 계산 - 복리-예금")
	@Test
	void calculateFinancialProduct_whenProductIsCompoundDeposit_thenReturnsCalculationResult() throws Exception {
		// Given
		FinancialProduct product = DepositProduct.builder()
			.userId(principalUser.getUser().getId())
			.name("복리-예금")
			.investmentType(InvestmentType.DEPOSIT)
			.productInvestmentType(ProductInvestmentType.from(InvestmentType.DEPOSIT.name()))
			.amount(ProductAmount.won(BigDecimal.valueOf(1_000_000)))
			.months(new ProductMonths(12))
			.interestRate(new AnnualInterestRate(BigDecimal.valueOf(0.05)))
			.interestType(COMPOUND)
			.taxType(TaxType.NON_TAX)
			.taxRate(new FixedTaxRate(BigDecimal.ZERO))
			.startDate(LocalDate.now())
			.createdAt(LocalDateTime.now())
			.build();
		financialProductRepository.save(product);
		String productId = product.getId();

		InvestmentCurrency investmentCurrency = InvestmentCurrency.from(Currency.won());
		// When & Then
		mockMvc.perform(get("/api/v1/products/{id}/calculate", productId)
				.with(SecurityMockMvcRequestPostProcessors.user(principalUser)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.totalInvestment").value(1_000_000))
			.andExpect(jsonPath("$.totalInterest").value(51_162))
			.andExpect(jsonPath("$.totalTax").value(0))
			.andExpect(jsonPath("$.totalProfit").value(1_051_162))
			.andExpect(jsonPath("$.taxType").value("비과세"))
			.andExpect(jsonPath("$.taxPercent").value("0%"))
			.andExpect(jsonPath("$.monthlyDetails").isArray())
			.andExpect(jsonPath("$.yearlyDetails").isArray())
			.andExpect(jsonPath("$.investmentCurrency.code").value(investmentCurrency.getCode()))
			.andExpect(jsonPath("$.investmentCurrency.unit").value(investmentCurrency.getUnit()))
			.andExpect(jsonPath("$.investmentCurrency.name").value(investmentCurrency.getName()))
			.andDo(MockMvcResultHandlers.print());
	}

	@DisplayName("상품 수익 계산 - 단리-적금")
	@Test
	void calculateFinancialProduct_whenProductIsSimpleSaving_thenReturnsCalculationResult() throws Exception {
		// Given
		FinancialProduct product = FinancialProductDataProvider.createSavingsProduct(principalUser.getUser().getId(),
			SIMPLE);
		financialProductRepository.save(product);
		String productId = product.getId();

		InvestmentCurrency investmentCurrency = InvestmentCurrency.from(Currency.won());
		// When & Then
		mockMvc.perform(get("/api/v1/products/{id}/calculate", productId)
				.with(SecurityMockMvcRequestPostProcessors.user(principalUser)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.totalInvestment").value(12_000_000))
			.andExpect(jsonPath("$.totalInterest").value(325_000))
			.andExpect(jsonPath("$.totalTax").value(50050))
			.andExpect(jsonPath("$.totalProfit").value(12_274_950))
			.andExpect(jsonPath("$.taxType").value(TaxType.STANDARD.getDescription()))
			.andExpect(jsonPath("$.taxPercent").value("15.4%"))
			.andExpect(jsonPath("$.monthlyDetails").isArray())
			.andExpect(jsonPath("$.yearlyDetails").isArray())
			.andExpect(jsonPath("$.investmentCurrency.code").value(investmentCurrency.getCode()))
			.andExpect(jsonPath("$.investmentCurrency.unit").value(investmentCurrency.getUnit()))
			.andExpect(jsonPath("$.investmentCurrency.name").value(investmentCurrency.getName()))
			.andDo(MockMvcResultHandlers.print());
	}

	@DisplayName("상품 수익 계산 - 복리-적금")
	@Test
	void calculateFinancialProduct_whenProductIsCompoundSaving_thenReturnsCalculationResult() throws Exception {
		// Given
		FinancialProduct product = FinancialProductDataProvider.createSavingsProduct(principalUser.getUser().getId(),
			COMPOUND);
		financialProductRepository.save(product);
		String productId = product.getId();

		InvestmentCurrency investmentCurrency = InvestmentCurrency.from(Currency.won());
		// When & Then
		mockMvc.perform(get("/api/v1/products/{id}/calculate", productId)
				.with(SecurityMockMvcRequestPostProcessors.user(principalUser)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.totalInvestment").value(12_000_000))
			.andExpect(jsonPath("$.totalInterest").value(330_017))
			.andExpect(jsonPath("$.totalTax").value(50_823))
			.andExpect(jsonPath("$.totalProfit").value(12_279_194))
			.andExpect(jsonPath("$.taxType").value(TaxType.STANDARD.getDescription()))
			.andExpect(jsonPath("$.taxPercent").value("15.4%"))
			.andExpect(jsonPath("$.monthlyDetails").isArray())
			.andExpect(jsonPath("$.yearlyDetails").isArray())
			.andExpect(jsonPath("$.investmentCurrency.code").value(investmentCurrency.getCode()))
			.andExpect(jsonPath("$.investmentCurrency.unit").value(investmentCurrency.getUnit()))
			.andExpect(jsonPath("$.investmentCurrency.name").value(investmentCurrency.getName()))
			.andDo(MockMvcResultHandlers.print());
	}
}
