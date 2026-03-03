package co.invest72.financial_product.infrastructure.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import co.invest72.financial_product.domain.FinancialProduct;

public interface JpaFinancialProductRepository extends JpaRepository<FinancialProduct, String> {

	@Query("SELECT p FROM FinancialProduct p WHERE p.userId = :userId")
	List<FinancialProduct> findByUserId(String userId);
}
