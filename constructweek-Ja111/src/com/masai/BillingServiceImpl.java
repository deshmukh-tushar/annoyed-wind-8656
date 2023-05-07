package com.masai;

import java.time.LocalDate;
import java.util.*;

public class BillingServiceImpl implements BillingService {
	
    private static final Map<UserEntity, List<BillEntity>> billBook = new HashMap<>();
    private static final Map<UserEntity, List<Transaction>> transactionHistory = new HashMap<>();
   private static final Integer perUnitCost = 10;

    @Override
    public BillEntity generateBill(UserEntity consumerEntity, Double unitConsumed, LocalDate date) {
        Double previousBill = getPreviousPendingBills(consumerEntity);
        BillEntity billEntity = new BillEntity();
        Double total = billEntity.getFixedChargeForConnection() + (unitConsumed * perUnitCost);
        Double taxes = (total / 2.5);
        billEntity.setTotal(total + taxes + previousBill);
        billEntity.setTaxes(taxes);
        billEntity.setAdjustment(previousBill);
        billEntity.setDate(date);
        addToBillBook(consumerEntity,billEntity);
        return billEntity;

    }

    @Override
    public void payBill(UserEntity consumerEntity, Double billPaidAmount, BillEntity billEntity) {
      billBook.get(consumerEntity
        ).stream().forEach(bill->{
                bill.setStatus(Status.PAID);
      });
        if(transactionHistory.containsKey(consumerEntity)){
            transactionHistory.get(consumerEntity).add(new Transaction(LocalDate.now(),billEntity,consumerEntity,billPaidAmount));
        }else {
            List<Transaction> transactions = new ArrayList<>();
            transactions.add(new Transaction(LocalDate.now(),billEntity,consumerEntity,billPaidAmount));
            transactionHistory.put(consumerEntity,transactions);
        }
    }

    public List<Transaction> showAllTransactions(UserEntity consumerEntity){
        if(transactionHistory.containsKey(consumerEntity)){
            return transactionHistory.get(consumerEntity);
        }else {
            return Collections.EMPTY_LIST;
        }
    }

    public Double getPreviousPendingBills(UserEntity consumerEntity){
        if(!billBook.containsKey(consumerEntity)){
            return 0.0;
        }
        return  billBook.get(consumerEntity).stream().filter(bill-> bill.getStatus().equals(Status.PENDING)).mapToDouble(bill -> bill.getTotal()).sum();
    }
    public void addToBillBook(UserEntity consumerEntity, BillEntity billEntity ){
       if(billBook.containsKey(consumerEntity)){
           billBook.get(consumerEntity).add(billEntity);
       }else{
           List<BillEntity> newList = new ArrayList<>();
           newList.add(billEntity);
           billBook.put(consumerEntity,newList);
       }
    }

    public List<BillEntity> getAllBillsOfConsumer(UserEntity userEntity){
        if(billBook.containsKey(userEntity)){
           return billBook.get(userEntity);
        }else {
           return Collections.EMPTY_LIST;
        }
    }

	
	
	
	
}
