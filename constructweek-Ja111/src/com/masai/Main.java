package com.masai;



//import org.msceb.entity.BillEntity;
//import org.msceb.entity.ConsumerEntity;
//import org.msceb.entity.Transaction;
//import org.msceb.entity.UserEntity;
//import org.msceb.service.BillingService;
//import org.msceb.service.LoginService;
//import org.msceb.service.impl.BillingServiceImpl;
//import org.msceb.service.impl.LoginServiceImpl;


import java.text.DecimalFormat;
import java.time.LocalDate;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.*;

public class Main {


    static void generateData(){
        createConsumer("abc@gmail.com","123456");
        createConsumer("abc123@gmail.com","123456");
        createConsumer("abc456@gmail.com","123456");
        createConsumer("abc789@gmail.com","123456");
        createConsumer("abc111@gmail.com","123456");
    }
    private static final DecimalFormat df = new DecimalFormat("0.00");
    static void createConsumer(String userName, String password){
        ConsumerEntity ce = new ConsumerEntity();
        ce.setDeleted(Boolean.FALSE);
        ce.setEmail(userName);
        ce.setPassword(password);
        ce.setAddress("abc chowk pune");
        ce.setMobileNumber(1111111111);
        ce.setFirstName(userName);
        ce.setLastName("");
        loginService.register(ce);
        billingService.addToBillBook(ce,createBill(Double.valueOf(df.format(Math.random()* 10)), 1));
        billingService.addToBillBook(ce,createBill(Double.valueOf(df.format(Math.random()* 10)), 2));
        billingService.addToBillBook(ce,createBill(Double.valueOf(df.format(Math.random()* 10)), 3));
        billingService.addToBillBook(ce,createBill(Double.valueOf(df.format(Math.random()* 10)), 4));
        billingService.addToBillBook(ce,createBill(Double.valueOf(df.format(Math.random()* 10)), 5));
    }

    private static BillEntity createBill(Double unitConsumed,int month) {
        BillEntity billEntity = new BillEntity();
        Double total = billEntity.getFixedChargeForConnection() + (unitConsumed * BillEntity.perUnitCost);
        Double taxes = (total / 2.5);
        billEntity.setTotal(total + taxes);
        billEntity.setTaxes(taxes);
        billEntity.setDate(LocalDate.now().minusMonths(month));
        return billEntity;
    }

    static UserEntity user = null;

private
    static LoginService loginService = new LoginServiceImpl();
    static BillingService billingService = new BillingServiceImpl();
    
  public static void main(String []args) {

        generateData();
       while(true){
           Scanner getInput = new Scanner(System.in);
           loginDecision(getInput);
           if(user instanceof ConsumerEntity){
               performConsumerOperations(getInput);
           }else{
               //admin
               performUserOperations(getInput);
           }
       }
    }

    private static void performConsumerOperations(Scanner getInput) {
        while (true) {
            System.out.println("1. Pay bill");
            System.out.println("2. View transaction History");
            System.out.println("3. Logout");
            String in = getInput.next();
            if(isNumber(in) && Integer.parseInt(in) == 1){
                System.out.println("Enter unit consumed");
                String unitConsumed = getInput.next();
                while (!isNumber(unitConsumed)){
                    System.out.println("enter valid number");
                    unitConsumed = getInput.next();
                }
                BillEntity billEntity = billingService.generateBill(user,Double.parseDouble(unitConsumed), LocalDate.now());
                System.out.println("Bill generated date "+billEntity.getDate()+" total amount to paid "+billEntity.getTotal());
                String amount = getInput.next();
                while(Double.parseDouble(amount) != billEntity.getTotal()){
                    System.out.println("Please pay full amount "+ billEntity.getTotal());
                    amount = getInput.next();
                }
                billingService.payBill(user,Double.parseDouble(amount),billEntity);
                System.out.println("bill paid successfully.");
            } else if (isNumber(in) && Integer.parseInt(in) == 2) {
                List<Transaction> transactions=  billingService.showAllTransactions(user);
                transactions.stream().forEach(System.out::println);
            }  else if (isNumber(in) && Integer.parseInt(in) == 3) {
                break;
            } else{
                System.out.println("wrong option selected");
            }
        }

    }

        private static void performUserOperations(Scanner getInput) {
        while(true){
            System.out.println("1. Register new consumer");
            System.out.println("2. View all consumers");
            System.out.println("3. View the bill of consumer");
            System.out.println("4. View  all the bills");
            System.out.println("5. View all bills paid and pending of consumer.");
            System.out.println("6. Delete consumer . (means set the consumer connection inactive)");
            System.out.println("7. Logout");
            String in = getInput.next();
            if(isNumber(in) && Integer.parseInt(in) == 1){
                signupUser(getInput);
            }else if(isNumber(in) && Integer.parseInt(in) == 2){
                loginService.getAllConsumers().forEach(System.out::println);
            }else if(isNumber(in) && Integer.parseInt(in) == 3){
               System.out.println("Enter email id of the consumer");
                 String email = getInput.next();
                 UserEntity userEntity = loginService.findByEmail(email);
                showPendingBillByConsumer(userEntity);
            }else if(isNumber(in) && Integer.parseInt(in) == 4){
                loginService.getAllConsumers().forEach(consumer->{
                    showPendingBillByConsumer(consumer);
                });
            }else if(isNumber(in) && Integer.parseInt(in) == 5){
                System.out.println("Enter email id of the consumer");
                String email = getInput.next();
                UserEntity userEntity = loginService.findByEmail(email);
                List<BillEntity> billsOfConsumer = billingService.getAllBillsOfConsumer(userEntity);
                if(billsOfConsumer.isEmpty()){
                    System.out.println("No bills found");
                }else {
                    billsOfConsumer.forEach(bill->{
                        System.out.println("Bill Date "+bill.getDate()+" total "+ bill.getTotal()+" status "+bill.getStatus());
                    });
                }
            }else if(isNumber(in) && Integer.parseInt(in) == 6){
                System.out.println("Enter email id of the consumer");
                String email = getInput.next();
                if(loginService.deleteAccount(email)){
                    System.out.println("Account deleted successfully.");
                }else {
                    System.out.println("No account found with email "+email);
                }
            } else if (isNumber(in) && Integer.parseInt(in) == 7) {
                System.out.println("Logout successfully");
                break;
            }else {
                System.out.println("Wrong option selected");
            }
        }
    }

    private static void showPendingBillByConsumer(UserEntity userEntity) {
        if(Objects.nonNull(userEntity)){
          Double pendingBills =   billingService.getPreviousPendingBills(userEntity);
            System.out.println("Pending bill "+pendingBills+" user "+ userEntity.getEmail());
        }else{
            System.out.println("User not found.");
        }
    }

    static boolean isNumber(String str){
    try{
        Integer.parseInt(str);
        return true;
    }catch (Exception e){
        return false;
    }
}
    private static void loginDecision(Scanner getInput) {
      while (true){
            System.out.println("1 . login user");
            System.out.println("2 . Signup user");
            String loginDecision = getInput.next();
            if(isNumber(loginDecision) && Integer.parseInt(loginDecision) == 1){
                loginUser(getInput);
                break;
            }else if(isNumber(loginDecision) && Integer.parseInt(loginDecision) == 2){
                signupUser(getInput);
                break;
            }else{
                System.out.println("invalid inputs try again");
            }

      }
    }

    private static void loginUser(Scanner getInput) {
        while(validUser(getInput)){
            System.out.println("User not valid login again");
        }
        System.out.println("Login successfully.");
    }

    private static boolean validUser(Scanner getInput) {
        System.out.println("Enter username");
        String userName =  getInput.next();
        System.out.println("Enter password");
        String password =  getInput.next();
        UserEntity userEntity = loginService.login(userName,password);
        if(Objects.nonNull(userEntity))
        {
            user =  userEntity;
            return false;
        }else {

            return true;
        }


    }  private static boolean signupUser(Scanner getInput) {
        System.out.println("Enter Email");
        String email = getValidEmail(getInput);
        System.out.println("Enter password");
        String password = getValidPassword(getInput);
        System.out.println("Enter address");
        String address =  getInput.next();
        
        System.out.println("Enter first Name");
        String firstName =  getInput.next();
        System.out.println("Enter last Name");
        String lastName =  getInput.next();
        System.out.println("Mobile number");
        Integer mobileNUmber =  getInput.nextInt();
        ConsumerEntity ce = new ConsumerEntity();
        
        ce.setLastName(lastName);
        ce.setFirstName(firstName);
        ce.setEmail(email);
        ce.setPassword(password);
        ce.setAddress(address);
        ce.setMobileNumber(mobileNUmber);

        if(loginService.register(ce))
        {
            user =  ce;
            System.out.println("User signup successfully");
            return false;
        }else {
            System.out.println("User already exist with email "+ email+" try with different email id");
            return true;
        }
    }

    private static String getValidEmail(Scanner getInput) {
        boolean isNotValid = true;
        while (isNotValid){
            String email = getInput.next();
            if(isValidEmail(email)){
                return email;
            }else{
                System.out.println("EMail not valid");
            }
        }
        return  "";
    }


    private static String getValidPassword(Scanner getInput) {
        boolean isNotValid = true;
        while (isNotValid){
            String password = getInput.next();
            if(isValidPassword(password)){
                return password;
            }else{
                System.out.println("password should have 1 charactor one capital minimum 8 letters");
            }
        }
        return  "";
    }

    public static boolean isValidEmail(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    public static boolean isValidPassword(String password)
    {
        String regex = "^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=.*[@#$%^&+=])"
                + "(?=\\S+$).{8,20}$";
        Pattern p = Pattern.compile(regex);
        if (password == null) {
            return false;
        }
        Matcher m = p.matcher(password);
        return m.matches();
    }
}

