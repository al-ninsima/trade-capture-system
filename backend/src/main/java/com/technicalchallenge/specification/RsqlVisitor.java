package com.technicalchallenge.specification;

import cz.jirutka.rsql.parser.ast.*;
import org.springframework.data.jpa.domain.Specification;

public class RsqlVisitor<T> implements RSQLVisitor<Specification<T>, Void> {

    @Override
    public Specification<T> visit(AndNode node, Void param) {
        return node.getChildren().stream()
                .map(n -> n.accept(this, null))
                .reduce(Specification::and)
                .orElse(null);
    }

    @Override
    public Specification<T> visit(OrNode node, Void param) {
        return node.getChildren().stream()
                .map(n -> n.accept(this, null))
                .reduce(Specification::or)
                .orElse(null);
    }

    @Override
    public Specification<T> visit(ComparisonNode node, Void param) {
        RsqlSearchOperation op = convertOp(node.getOperator().getSymbol());
        return new RsqlSpecification<>(new RsqlCriteria(
                node.getSelector(),
                op,
                node.getArguments().get(0)
        ));
    }

    private RsqlSearchOperation convertOp(String symbol) {
        return switch (symbol) {
            case "==" -> RsqlSearchOperation.EQUAL;
            case "!=" -> RsqlSearchOperation.NOT_EQUAL;
            case ">" -> RsqlSearchOperation.GREATER_THAN;
            case ">=" -> RsqlSearchOperation.GREATER_THAN_OR_EQUAL;
            case "<" -> RsqlSearchOperation.LESS_THAN;
            case "<=" -> RsqlSearchOperation.LESS_THAN_OR_EQUAL;
            default -> throw new UnsupportedOperationException("Operator not supported: " + symbol);
        };
    }
}
