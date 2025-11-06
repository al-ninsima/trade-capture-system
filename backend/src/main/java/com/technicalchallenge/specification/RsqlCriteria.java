package com.technicalchallenge.specification;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RsqlCriteria {
    private String property;
    private RsqlSearchOperation operation;
    private Object value;
}
