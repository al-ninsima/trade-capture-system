package com.technicalchallenge.model;

//List of operations a user can perform on a trade...validate user permissions and business rules against these values.

public enum TradeOperation {
    CREATE,
    AMEND,
    TERMINATE,
    CANCEL,
    VIEW
}
