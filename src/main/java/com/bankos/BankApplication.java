package main.java.com.bankos;

import main.java.com.bankos.Exceptions.InsufficientFundsException;
import main.java.com.bankos.Exceptions.TransferConstraintsException;
import main.java.com.bankos.Exceptions.UserNotFoundException;

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
 * COncurrency
 * Use BigInteger(Done)
 * Restructure
 */

public class BankApplication {

    private List<Account> accounts;
    private static final int MAX_DEPOSIT_COUNT = 3;
    private static final int MAX_WITHDRAW_COUNT = 3;
    public List<Account> getCustomers() {
        return accounts;
    }

    public void setCustomers(List<Account> accounts) {
        this.accounts = accounts;
    }

    public BankApplication(){
        this.accounts = new ArrayList<>();
    }

    public int create(String name){
        Account c = new Account(name);
        this.accounts.add((c));
        return c.getId();
    }

    public BigDecimal deposit(int id, BigDecimal depositAmount) throws RuntimeException{
        Account account = this.findCustomer(id);
        //check if the number of transactions has exceeeded 3 transactions

        if(!timeExpired(account.getDepositTime())){
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
            throw new TransferConstraintsException("Reciever could not accept these funds");
        }else{
            account.setBalance(account.getBalance().add(depositAmount));
            account.incrementDepositCount();
        }
        return account.getBalance();
    }

    public Account findCustomer(int id) throws RuntimeException{
        Optional<Account> optionalCustomer = accounts.stream().filter((cust) -> cust.getId() == id).findFirst();
        if(!optionalCustomer.isPresent()){
            throw new UserNotFoundException("User Not found, Please check the account number");
        }
        return optionalCustomer.get();
    }

    public BigDecimal withdraw(int id, BigDecimal withdrawAmount) throws RuntimeException{
        Account account = this.findCustomer(id);

        //check if the number of transactions has exceeeded 3 transactions
        if(!timeExpired(account.getWithdrawTime())){
            account.resetWithdrawCount();
            account.setWithdrawTime(Instant.now());
        }else{
            if(account.getWithdrawCount() >= MAX_WITHDRAW_COUNT){
                throw new TransferConstraintsException("Only 3 withdrawls are allowed in a day");
            }
        }


        if(withdrawAmount.compareTo(BigDecimal.valueOf(25000))>0){
            throw new TransferConstraintsException("Maximum Withdrawl limit is 25000");
        }else if(withdrawAmount.compareTo(BigDecimal.valueOf(1000))<0){
            throw new TransferConstraintsException("Minimum Withdrawl limit is 1000");
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

    public String transfer(int sender, int reciever, BigDecimal transferAmount){
        Account senderCust = findCustomer(sender);
        Account recieverCust = findCustomer(reciever);


        if(transferAmount.compareTo(BigDecimal.valueOf(1000)) < 0 ){
            throw new TransferConstraintsException("Failure : Minimum withdrawl amount is 1000 for main.java.com.bankos.Account"+recieverCust.getId());
        }else if(transferAmount.compareTo(BigDecimal.valueOf(25000)) > 0){
            throw new TransferConstraintsException("Failure : Maximum withdrawl amount is 25000 for main.java.com.bankos.Account"+recieverCust.getId());
        }

        if( senderCust.getBalance().compareTo(transferAmount) < 0 ) {
            throw  new TransferConstraintsException("Failure : Insufficient Balance for transfer");
        }
        else if( recieverCust.getBalance().add(transferAmount).compareTo(BigDecimal.valueOf(100000)) > 0){
            throw new TransferConstraintsException("Failure : Reciever could not accept these funds");
        }
        else{
            senderCust.setBalance(senderCust.getBalance().subtract(transferAmount));
            recieverCust.setBalance(recieverCust.getBalance().add(transferAmount));
            return "Success";
        }

    }

    public boolean timeExpired(Instant then){
        Instant now = Instant.now();
        Instant twentyFourHoursEarlier = now.minus( 24 , ChronoUnit.HOURS );
        // Is that moment (a) not before 24 hours ago, AND (b) before now (not in the future)?
        Boolean within24Hours = ( ! then.isBefore( twentyFourHoursEarlier ) ) &&  then.isBefore( now );
        return  within24Hours;
    }

    public static void main(String[] args) throws FileNotFoundException {
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
                            System.out.println(app.deposit(Integer.valueOf(inputArray[1]),new BigDecimal(inputArray[2])));
                        }catch (RuntimeException re){
                            System.out.println(re.getMessage());
                        }finally {
                            break;
                        }

                    case "BALANCE":
                        try{
                            System.out.println(app.getBalance(Integer.valueOf(inputArray[1])));
                        }catch (RuntimeException e){
                            System.out.println(e.getMessage());
                        }finally {
                            break;
                        }


                    case "WITHDRAW":
                        try{
                            System.out.println(app.withdraw(Integer.valueOf(inputArray[1]),new BigDecimal(inputArray[2])));
                        }catch (RuntimeException re){
                            System.out.println(re.getMessage());
                        }finally {
                            break;
                        }


                    case "TRANSFER":
                        try{
                            System.out.println(app.transfer(Integer.valueOf(inputArray[1]),Integer.valueOf(inputArray[2]),new BigDecimal(inputArray[3])));
                        }catch (RuntimeException re){
                            System.out.println(re.getMessage());
                        }finally {
                            break;
                        }
                }

            }
        }catch (FileNotFoundException fileEx){
            System.out.println(fileEx.getMessage());
        }

    }
}

