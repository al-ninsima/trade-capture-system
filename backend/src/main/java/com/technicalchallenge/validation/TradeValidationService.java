package com.technicalchallenge.validation;

import com.technicalchallenge.dto.TradeDTO;
import com.technicalchallenge.model.TradeOperation;
import com.technicalchallenge.model.UserRole;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

public class TradeValidationService {

    // roles that can perform specific trade operations
private static final Map<UserRole, Set<TradeOperation>> ROLE_PERMISSIONS = Map.of(
        UserRole.TRADER, Set.of(
                TradeOperation.CREATE,
                TradeOperation.AMEND,
                TradeOperation.TERMINATE,
                TradeOperation.CANCEL,
                TradeOperation.VIEW
        ),
        UserRole.SALES, Set.of(
                TradeOperation.CREATE,
                TradeOperation.AMEND,
                TradeOperation.VIEW
        ),
        UserRole.MIDDLE_OFFICE, Set.of(
                TradeOperation.AMEND,
                TradeOperation.VIEW
        ),
        UserRole.SUPPORT, Set.of(
                TradeOperation.VIEW
        )
);
public boolean validateUserPrivileges(UserRole role, TradeOperation operation) {
    Set<TradeOperation> allowed = ROLE_PERMISSIONS.get(role);

    return allowed != null && allowed.contains(operation);
}

public ValidationResult validateTradeBusinessRules(TradeDTO tradeDTO) {

    // 1. Basic presence check
    if (tradeDTO == null) {
        return ValidationResult.fail("Trade data cannot be null");
    }

    if (tradeDTO.getTradeStartDate() != null && tradeDTO.getTradeDate() != null) {
    if (tradeDTO.getTradeStartDate().isBefore(tradeDTO.getTradeDate())) {
        return ValidationResult.fail("Start date cannot be before trade date");
    }
}

if (tradeDTO.getTradeMaturityDate() != null && tradeDTO.getTradeStartDate() != null) {
    if (tradeDTO.getTradeMaturityDate().isBefore(tradeDTO.getTradeStartDate())) {
        return ValidationResult.fail("Maturity date cannot be before start date");
    }
}

if (tradeDTO.getTradeMaturityDate() != null && tradeDTO.getTradeDate() != null) {
    if (tradeDTO.getTradeMaturityDate().isBefore(tradeDTO.getTradeDate())) {
        return ValidationResult.fail("Maturity date cannot be before trade date");
    }
}

if (tradeDTO.getTradeDate() != null) {
    LocalDate limit = LocalDate.now().minusDays(30);
    if (tradeDTO.getTradeDate().isBefore(limit)) {
        return ValidationResult.fail("Trade date cannot be more than 30 days in the past");
    }
}
    
    return ValidationResult.ok();
}


}
