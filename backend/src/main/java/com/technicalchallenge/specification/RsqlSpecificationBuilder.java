package com.technicalchallenge.specification;

// TODO: RSQL support temporarily disabled until we add Spring Boot 3 compatible implementation.

/*

import com.technicalchallenge.model.Trade;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import org.springframework.data.jpa.domain.Specification;

public class RsqlSpecificationBuilder {

    public static Specification<Trade> build(String query) {
        Node rootNode = new RSQLParser().parse(query);
        return rootNode.accept(new RsqlVisitor<>());
    }
}
*/

public class RsqlSpecificationBuilder<T> {}