package com.technicalchallenge.specification;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

public class RsqlSpecification<T> implements Specification<T> {

    private final RsqlCriteria criteria;

    public RsqlSpecification(RsqlCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        Path<?> path = resolvePath(root, criteria.getProperty());

        switch (criteria.getOperation()) {
            case EQUAL:
                return builder.equal(path, criteria.getValue());
            case NOT_EQUAL:
                return builder.notEqual(path, criteria.getValue());
            case GREATER_THAN:
                return builder.greaterThan(path.as(String.class), criteria.getValue().toString());
            case GREATER_THAN_OR_EQUAL:
                return builder.greaterThanOrEqualTo(path.as(String.class), criteria.getValue().toString());
            case LESS_THAN:
                return builder.lessThan(path.as(String.class), criteria.getValue().toString());
            case LESS_THAN_OR_EQUAL:
                return builder.lessThanOrEqualTo(path.as(String.class), criteria.getValue().toString());
            default:
                throw new UnsupportedOperationException("Unknown operation");
        }
    }

    private Path<?> resolvePath(Root<T> root, String property) {
        if (!property.contains(".")) {
            return root.get(property);
        }
        String[] parts = property.split("\\.");
        Path<?> path = root;
        for (String part : parts) {
            path = path.get(part);
        }
        return path;
    }
}
