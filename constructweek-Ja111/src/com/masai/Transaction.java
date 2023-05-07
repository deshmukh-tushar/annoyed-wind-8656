package com.masai;


import java.time.LocalDate;

public class Transaction {
    LocalDate date;
    BillEntity billEntity;
    UserEntity paidBy;
    Double paidAMount;

    public Transaction(LocalDate date, BillEntity billEntity, UserEntity paidBy, Double paidAMount) {
        this.date = date;
        this.billEntity = billEntity;
        this.paidBy = paidBy;
        this.paidAMount = paidAMount;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "date=" + date +
                ", billEntity=" + billEntity +
                ", paidBy=" + paidBy +
                ", paidAMount=" + paidAMount +
                '}';
    }
}

