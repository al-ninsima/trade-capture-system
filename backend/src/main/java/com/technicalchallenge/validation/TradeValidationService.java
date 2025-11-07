package com.technicalchallenge.validation;

import com.technicalchallenge.dto.TradeDTO;
import com.technicalchallenge.dto.TradeLegDTO;
import com.technicalchallenge.model.TradeOperation;
import com.technicalchallenge.model.UserRole;
import com.technicalchallenge.repository.ApplicationUserRepository;
import com.technicalchallenge.repository.BookRepository;
import com.technicalchallenge.repository.CounterpartyRepository;
import com.technicalchallenge.validation.ValidationResult;
import com.technicalchallenge.repository.BookRepository;
import com.technicalchallenge.repository.CounterpartyRepository;
import com.technicalchallenge.repository.ApplicationUserRepository;
import com.technicalchallenge.model.Book;
import com.technicalchallenge.model.Counterparty;
import com.technicalchallenge.model.ApplicationUser;


import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TradeValidationService {

    private final BookRepository bookRepository;
    private final CounterpartyRepository counterpartyRepository;
    private final ApplicationUserRepository applicationUserRepository;

    private static final String OP_CREATE = "CREATE";
    private static final String OP_AMEND = "AMEND";
    private static final String OP_TERMINATE = "TERMINATE";
    private static final String OP_CANCEL = "CANCEL";
    private static final String OP_VIEW = "VIEW";


    @Autowired
    public TradeValidationService(BookRepository bookRepository,
                                  CounterpartyRepository counterpartyRepository,
                                  ApplicationUserRepository applicationUserRepository) {
        this.bookRepository = bookRepository;
        this.counterpartyRepository = counterpartyRepository;
        this.applicationUserRepository = applicationUserRepository;
    }

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

public ValidationResult validateTradeDates(TradeDTO trade) {
    ValidationResult result = new ValidationResult();

    // Trade date cannot be more than 30 days old
    if (trade.getTradeDate() != null) {
        if (trade.getTradeDate().isBefore(LocalDate.now().minusDays(30))) {
            result.addError("Trade date cannot be more than 30 days in the past.");
        }
    }

    // Start date must be on or after trade date
    if (trade.getTradeStartDate() != null && trade.getTradeDate() != null) {
        if (trade.getTradeStartDate().isBefore(trade.getTradeDate())) {
            result.addError("Trade start date cannot be before trade date.");
        }
    }

    // Maturity date must be after start date AND trade date
    if (trade.getTradeMaturityDate() != null && trade.getTradeStartDate() != null) {
        if (trade.getTradeMaturityDate().isBefore(trade.getTradeStartDate())) {
            result.addError("Maturity date cannot be before start date.");
        }
    }

    return result;
}
public ValidationResult validateTrade(TradeDTO tradeDTO) {
    ValidationResult result = new ValidationResult();

    result.merge(validateTradeDates(tradeDTO));
    result.merge(validateReferenceData(tradeDTO));
    result.merge(validateUserPermissions(tradeDTO.getTraderUserId(), "AMEND", tradeDTO));
    result.merge(validateLegConsistency(tradeDTO.getTradeLegs()));

    return result;
}
public ValidationResult validateReferenceData(TradeDTO tradeDTO) {
    ValidationResult result = ValidationResult.ok();

    // Validate Book
    Book book = bookRepository.findByBookNameIgnoreCase(tradeDTO.getBookName())
            .orElse(null);
    if (book == null || !book.isActive()) {
        return ValidationResult.fail("Book is invalid or inactive.");
    }

    // Validate Counterparty
    Counterparty counterparty = counterpartyRepository.findByNameIgnoreCase(tradeDTO.getCounterpartyName())
            .orElse(null);
    if (counterparty == null || !counterparty.isActive()) {
        return ValidationResult.fail("Counterparty is invalid or inactive.");
    }

    // Validate Trader User
    ApplicationUser trader = applicationUserRepository.findByLoginIdIgnoreCase(tradeDTO.getTraderUserName())
            .orElse(null);
    if (trader == null || !trader.isActive()) {
        return ValidationResult.fail("Trader user is invalid or inactive.");
    }

    // Validate Inputter User
    ApplicationUser inputter = applicationUserRepository.findByLoginIdIgnoreCase(tradeDTO.getInputterUserName())
            .orElse(null);
    if (inputter == null || !inputter.isActive()) {
        return ValidationResult.fail("Inputter user is invalid or inactive.");
    }

    return result;
}


public ValidationResult validateUserPermissions(String userLoginId, String operation) {
    // Look up user
    Optional<ApplicationUser> userOpt = applicationUserRepository.findByLoginIdIgnoreCase(userLoginId);
    if (userOpt.isEmpty()) {
        return ValidationResult.fail("User " + userLoginId + " does not exist.");
    }

    ApplicationUser user = userOpt.get();
    String role = user.getUserProfile().getUserType().toUpperCase();

    switch (role) {
        case "TRADER":
            return ValidationResult.ok(); // trader can do everything

        case "SALES":
            if (operation.equals(OP_CREATE) || operation.equals(OP_AMEND)) {
                return ValidationResult.ok();
            }
            return ValidationResult.fail("SALES users cannot " + operation.toLowerCase() + " trades.");

        case "MIDDLE_OFFICE":
            if (operation.equals(OP_AMEND) || operation.equals(OP_VIEW)) {
                return ValidationResult.ok();
            }
            return ValidationResult.fail("MIDDLE_OFFICE users cannot " + operation.toLowerCase() + " trades.");

        case "SUPPORT":
            if (operation.equals(OP_VIEW)) {
                return ValidationResult.ok();
            }
            return ValidationResult.fail("SUPPORT users have view-only access.");

        default:
            return ValidationResult.fail("Unknown user role: " + role);
    }
}


// Placeholder: Leg Consistency Validation
private ValidationResult validateLegConsistency(List<TradeLegDTO> legs) {
    ValidationResult result = new ValidationResult();
    // Actual leg consistency rules will be added later
    return result;
}


}

