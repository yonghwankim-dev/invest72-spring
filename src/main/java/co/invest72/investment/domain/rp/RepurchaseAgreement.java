package co.invest72.investment.domain.rp;

import java.time.LocalDate;

/**
 * 환매조건부채권(RP)
 */
public interface RepurchaseAgreement {

	LocalDate calculateExpirationDate(LocalDate startDate, int dayOfMonth);
}
