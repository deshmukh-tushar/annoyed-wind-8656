package com.masai;

import java.time.LocalDate;
import java.util.List;

public interface BillingService {

	BillEntity generateBill(UserEntity consumerEntity,Double unitConsumed, LocalDate date);
    void payBill(UserEntity consumerEntity, Double billPaidAmount ,BillEntity billEntity);
    Double getPreviousPendingBills(UserEntity consumerEntity);
    public void addToBillBook(UserEntity consumerEntity, BillEntity billEntity );
    List<BillEntity> getAllBillsOfConsumer(UserEntity userEntity);
    List<Transaction> showAllTransactions(UserEntity consumerEntity);
	
}
