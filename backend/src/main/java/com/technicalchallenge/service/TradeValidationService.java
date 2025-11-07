package com.technicalchallenge.service;

import com.technicalchallenge.model.TradeOperation;
import com.technicalchallenge.model.UserRole;
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


}
