package com.bankos;

import com.bankos.Exceptions.InsufficientFundsException;
import com.bankos.Exceptions.TransferConstraintsException;
import com.bankos.Exceptions.UserNotFoundException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/***
 * @author mohit.nirwan
 */

public class BankApplication {

    private final List<Account> accounts;

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
        if(!Helper.within24Hours(account.getDepositTime())){
            account.resetDepositCount();
            account.setDepositTime(Instant.now());
        }
        Helper.validateDeposit(depositAmount, account);
        account.setBalance(account.getBalance().add(depositAmount));
        account.incrementDepositCount();
        return account.getBalance();
    }

    public BigDecimal withdraw(int id, BigDecimal withdrawAmount) throws TransferConstraintsException,InsufficientFundsException{
        Account account = this.findCustomer(id);
        //check if the number of transactions has exceeded 3 transactions
        if(!Helper.within24Hours(account.getWithdrawTime())){
            account.resetWithdrawCount();
            account.setWithdrawTime(Instant.now());
        }
        Helper.validateWithdraw(withdrawAmount,account);
        account.setBalance(account.getBalance().subtract(withdrawAmount));
        account.incrementWithdrawCount();
        return account.getBalance();
    }

    public String transfer(int sender, int receiver, BigDecimal transferAmount){
        Account senderCust = findCustomer(sender);
        Account receiverCust = findCustomer(receiver);
        Helper.validateTransfer(transferAmount,senderCust,receiverCust);
        senderCust.setBalance(senderCust.getBalance().subtract(transferAmount));
        receiverCust.setBalance(receiverCust.getBalance().add(transferAmount));
        return "Success";

    }

    public Account findCustomer(int id) throws UserNotFoundException{
        Optional<Account> optionalCustomer = accounts.stream().filter(cust -> cust.getId() == id).findFirst();
        if(optionalCustomer.isEmpty()){
            throw new UserNotFoundException("User Not found, Please check the account number");
        }
        return optionalCustomer.get();
    }

    public int findCustomerByName(String name){
       Optional<Account> account =  this.accounts.stream().filter(accountParse -> accountParse.getName().equalsIgnoreCase(name)).findFirst();
       if(account.isEmpty()){
           throw new UserNotFoundException("User Not found");
       }
       return account.get().getId();
    }

    public BigDecimal getBalance(int id) throws IndexOutOfBoundsException{
        return this.findCustomer(id).getBalance();
    }
}

