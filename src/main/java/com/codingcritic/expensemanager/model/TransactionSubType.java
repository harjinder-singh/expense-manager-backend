package com.codingcritic.expensemanager.model;

public enum TransactionSubType {
    RENT("Rent"), GROCERY("Grocery"), SHOPPING("Shopping"), EATOUT("Eatout"),
    SALARY("Salary"), TRANSFER("Transfer"), DEPOSIT("Deposit"), SNACK("Snack"), RETURN("Return"),
    CASHBACK("Cashback"), UTILITY("Utility"), TRANSPORTATION("Transportation"),
    MISCELLANEOUS("Miscellaneous");

    private String type;
    private TransactionSubType(String type) {
        this.type = type;
    }

    @Override
    public String toString(){
        return type;
    }
}
