package com.technicalchallenge.specification;

public class RsqlSpecification {
}

/*
   temporarily disabled RSQL logic — revisit in Enhancement stretch

import cz.jirutka.rsql.parser.ast.ComparisonNode;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class RsqlSpecification<T> implements Specification<T> {

    private final ComparisonNode comparison;

    public RsqlSpecification(ComparisonNode comparison) {
        this.comparison = comparison;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        // The field name, e.g. "counterparty.name"
        String selector = comparison.getSelector();
        // First argument only (RSQL supports lists but we keep single-value)
        String argument = comparison.getArguments().get(0);
        // Operator (==, !=, =gt=, =ge=, etc.)
        String operator = comparison.getOperator().getSymbol();

        Path<?> path = resolvePath(root, selector);

        switch (operator) {
            case "==":
                return builder.equal(path, argument);
            case "!=":
                return builder.notEqual(path, argument);
            case "=gt=":
                return builder.greaterThan(path.as(String.class), argument);
            case "=ge=":
                return builder.greaterThanOrEqualTo(path.as(String.class), argument);
            case "=lt=":
                return builder.lessThan(path.as(String.class), argument);
            case "=le=":
                return builder.lessThanOrEqualTo(path.as(String.class), argument);
            default:
                throw new UnsupportedOperationException("Unsupported operator: " + operator);
        }
    }

    private Path<?> resolvePath(Root<T> root, String selector) {
        // Support nested fields e.g. counterparty.name
        if (selector.contains(".")) {
            String[] parts = selector.split("\\.");
            Path<?> path = root.get(parts[0]);
            for (int i = 1; i < parts.length; i++) {
                path = path.get(parts[i]);
            }
            return path;
        }
        return root.get(selector);
    }
}
*/