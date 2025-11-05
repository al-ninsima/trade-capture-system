package com.technicalchallenge.specification;

import com.technicalchallenge.model.Trade;
import com.technicalchallenge.model.Counterparty;
import com.technicalchallenge.model.Book;
import com.technicalchallenge.model.ApplicationUser;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import java.time.LocalDate;

public class TradeSpecification {

    public static Specification<Trade> counterpartyNameContains(String counterparty) {
        return (root, query, cb) -> {
            Join<Trade, Counterparty> cp = root.join("counterparty", JoinType.LEFT);
            return cb.like(cb.lower(cp.get("name")), "%" + counterparty.toLowerCase() + "%");
        };
    }

    public static Specification<Trade> hasBookId(Long bookId) {
        return (root, query, cb) ->
                cb.equal(root.get("book").get("id"), bookId);
    }

    public static Specification<Trade> traderNameContains(String trader) {
        return (root, query, cb) -> {
            Join<Trade, ApplicationUser> tr = root.join("traderUser", JoinType.LEFT);
            return cb.like(cb.lower(tr.get("username")), "%" + trader.toLowerCase() + "%");
        };
    }

    public static Specification<Trade> hasStatus(String status) {
        return (root, query, cb) ->
                cb.equal(root.get("tradeStatus").get("tradeStatus"), status);
    }

    public static Specification<Trade> tradeDateAfterOrEqual(LocalDate fromDate) {
        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(root.get("tradeDate"), fromDate);
    }

    public static Specification<Trade> tradeDateBeforeOrEqual(LocalDate toDate) {
        return (root, query, cb) ->
                cb.lessThanOrEqualTo(root.get("tradeDate"), toDate);
    }
}
