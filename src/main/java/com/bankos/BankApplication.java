package com.bankos;

import com.bankos.Exceptions.InsufficientFundsException;
import com.bankos.Exceptions.TransferConstraintsException;
import com.bankos.Exceptions.UserNotFoundException;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/***
 * Exception handling (Done)
 * ID auto generate (Done)
 * UTC
 * Concurrency
 * Use BigInteger(Done)
 * Restructure(Done)
 */

public class BankApplication {

    private final List<Account> accounts;
    private static final int MAX_DEPOSIT_COUNT = 3;
    private static final int MAX_WITHDRAW_COUNT = 3;


    public BankApplication(){
        this.accounts = new ArrayList<>();
    }

    public int create(String name){
        Account c = new Account(name);
        this.accounts.add((c));
        return c.getId();
    }

    public BigDecimal deposit(int id, BigDecimal depositAmount) throws TransferConstraintsException{
        Account account = this.findCustomer(id);
        //check if the number of transactions has exceeded 3 transactions

        if(!within24Hours(account.getDepositTime())){
            account.resetDepositCount();
            account.setDepositTime(Instant.now());
        }else{
            if(account.getDepositCount() >= MAX_DEPOSIT_COUNT){
                throw new TransferConstraintsException("Only 3 deposits are allowed in a day");
            }
        }


        if(depositAmount.compareTo(BigDecimal.valueOf(50000)) > 0){
            throw new TransferConstraintsException("Maximum deposit amount is 50000");
        }else if(depositAmount.compareTo(BigDecimal.valueOf(500)) < 0){
            throw new TransferConstraintsException("Minimum deposit amount is 500");
        }

        if(account.getBalance().add(depositAmount).compareTo(BigDecimal.valueOf(100000)) > 0){
            throw new TransferConstraintsException("Receiver could not accept these funds");
        }else{
            account.setBalance(account.getBalance().add(depositAmount));
            account.incrementDepositCount();
        }
        return account.getBalance();
    }

    public Account findCustomer(int id) throws UserNotFoundException{
        Optional<Account> optionalCustomer = accounts.stream().filter(cust -> cust.getId() == id).findFirst();
        if(optionalCustomer.isEmpty()){
            throw new UserNotFoundException("User Not found, Please check the account number");
        }
        return optionalCustomer.get();
    }

    public BigDecimal withdraw(int id, BigDecimal withdrawAmount) throws TransferConstraintsException,InsufficientFundsException{
        Account account = this.findCustomer(id);

        //check if the number of transactions has exceeded 3 transactions
        if(!within24Hours(account.getWithdrawTime())){
            account.resetWithdrawCount();
            account.setWithdrawTime(Instant.now());
        }else{
            if(account.getWithdrawCount() >= MAX_WITHDRAW_COUNT){
                throw new TransferConstraintsException("Only 3 withdrawals are allowed in a day");
            }
        }


        if(withdrawAmount.compareTo(BigDecimal.valueOf(25000))>0){
            throw new TransferConstraintsException("Maximum Withdrawal limit is 25000");
        }else if(withdrawAmount.compareTo(BigDecimal.valueOf(1000))<0){
            throw new TransferConstraintsException("Minimum Withdrawal limit is 1000");
        }

        if(account.getBalance().compareTo(withdrawAmount)< 0){
            throw new InsufficientFundsException("Insufficient Funds");
        }else{
            account.setBalance(account.getBalance().subtract(withdrawAmount));
            account.incrementWithdrawCount();
        }
        return account.getBalance();
    }

    public BigDecimal getBalance(int id) throws IndexOutOfBoundsException{
        return this.findCustomer(id).getBalance();
    }

    public String transfer(int sender, int receiver, BigDecimal transferAmount){
        Account senderCust = findCustomer(sender);
        Account receiverCust = findCustomer(receiver);


        if(transferAmount.compareTo(BigDecimal.valueOf(1000)) < 0 ){
            throw new TransferConstraintsException("Failure : Minimum withdrawal amount is 1000 for Customer: "+receiverCust.getId());
        }else if(transferAmount.compareTo(BigDecimal.valueOf(25000)) > 0){
            throw new TransferConstraintsException("Failure : Maximum withdrawal amount is 25000 for Customer: "+receiverCust.getId());
        }

        if( senderCust.getBalance().compareTo(transferAmount) < 0 ) {
            throw  new TransferConstraintsException("Failure : Insufficient Balance for transfer");
        }
        else if( receiverCust.getBalance().add(transferAmount).compareTo(BigDecimal.valueOf(100000)) > 0){
            throw new TransferConstraintsException("Failure : Receiver could not accept these funds");
        }
        else{
            senderCust.setBalance(senderCust.getBalance().subtract(transferAmount));
            receiverCust.setBalance(receiverCust.getBalance().add(transferAmount));
            return "Success";
        }

    }

    public boolean within24Hours(Instant then){
        Instant now = Instant.now();
        Instant twentyFourHoursEarlier = now.minus( 24 , ChronoUnit.HOURS );
        // Is that moment (a) not before 24 hours ago, AND (b) before now (not in the future)?
        return ( ! then.isBefore( twentyFourHoursEarlier ) ) &&  then.isBefore( now );
    }

    public int findCustomerByName(String name){
       Optional<Account> account =  this.accounts.stream().filter(accountParse -> accountParse.getName().equalsIgnoreCase(name)).findFirst();
       if(account.isEmpty()){
           throw new UserNotFoundException("User Not found");
       }
       return account.get().getId();
    }

    public static void main(String[] args) {
        BankApplication app = new BankApplication();

        File file = new File("src/main/resources/input.txt");
        try (Scanner sc = new Scanner(file)) {

            while (sc.hasNextLine()) {
                String inputLine = sc.nextLine();
                String[] inputArray  = inputLine.split(" ");

                String switchString = inputArray[0];

                switch (switchString){
                    case "CREATE":
                        System.out.println(app.create(inputArray[1]));
                        break;

                    case "DEPOSIT":
                        try{
                            System.out.println(app.deposit(Integer.parseInt(inputArray[1]),new BigDecimal(inputArray[2])));
                        }catch (RuntimeException re){
                            System.out.println(re.getMessage());
                        }finally {
                            break;
                        }

                    case "BALANCE":
                        try{
                            System.out.println(app.getBalance(Integer.parseInt(inputArray[1])));
                        }catch (RuntimeException e){
                            System.out.println(e.getMessage());
                        }finally {
                            break;
                        }


                    case "WITHDRAW":
                        try{
                            System.out.println(app.withdraw(Integer.parseInt(inputArray[1]),new BigDecimal(inputArray[2])));
                        }catch (RuntimeException re){
                            System.out.println(re.getMessage());
                        }finally {
                            break;
                        }


                    case "TRANSFER":
                        try{
                            System.out.println(app.transfer(Integer.parseInt(inputArray[1]),Integer.parseInt(inputArray[2]),new BigDecimal(inputArray[3])));
                        }catch (RuntimeException re){
                            System.out.println(re.getMessage());
                        }finally {
                            break;
                        }

                    default :
                        System.out.println("Please try again with an valid option");
                        break;
                }

            }
        }catch (FileNotFoundException fileEx){
            System.out.println(fileEx.getMessage());
        }

    }
}

