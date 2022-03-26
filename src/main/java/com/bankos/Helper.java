package com.bankos;

import com.bankos.Exceptions.InsufficientFundsException;
import com.bankos.Exceptions.TransferConstraintsException;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class Helper {
    private static final BigDecimal MAX_DEPOSIT_AMOUNT = new BigDecimal(50000);
    private static final BigDecimal MIN_DEPOSIT_AMOUNT = new BigDecimal(500);
    private static final BigDecimal MAX_ACCOUNT_BAL_ALLOWED = new BigDecimal(100000);

    private static final BigDecimal MAX_WITHDRAW_AMOUNT = new BigDecimal(25000);
    private static final BigDecimal MIN_WITHDRAW_AMOUNT = new BigDecimal(1000);
    private static final int MAX_DEPOSIT_COUNT = 3;
    private static final int MAX_WITHDRAW_COUNT = 3;



    public static void validateDeposit(BigDecimal depositAmount, Account account){
        if(depositAmount.compareTo(MAX_DEPOSIT_AMOUNT) > 0)
            throw new TransferConstraintsException("Maximum deposit amount is 50000");
        if(depositAmount.compareTo(MIN_DEPOSIT_AMOUNT) < 0)
            throw new TransferConstraintsException("Minimum deposit amount is 500");
        if(account.getBalance().add(depositAmount).compareTo(MAX_ACCOUNT_BAL_ALLOWED) > 0)
            throw new TransferConstraintsException("Receiver could not accept these funds");
        if(account.getDepositCount() >= MAX_DEPOSIT_COUNT){
            throw new TransferConstraintsException("Only 3 deposits are allowed in a day");
        }
    }

    public static void validateWithdraw(BigDecimal withdrawAmount, Account account){
        if(withdrawAmount.compareTo(MAX_WITHDRAW_AMOUNT)>0)
            throw new TransferConstraintsException("Maximum Withdrawal limit is 25000");
        if(withdrawAmount.compareTo(MIN_WITHDRAW_AMOUNT)<0)
            throw new TransferConstraintsException("Minimum Withdrawal limit is 1000");
        if(account.getBalance().compareTo(withdrawAmount)< 0)
            throw new InsufficientFundsException("Insufficient Funds");
        if(account.getWithdrawCount() >= MAX_WITHDRAW_COUNT){
            throw new TransferConstraintsException("Only 3 withdrawals are allowed in a day");
        }
    }

    public static void validateTransfer(BigDecimal transferAmount, Account senderCust, Account receiverCust){
        validateWithdraw(transferAmount,senderCust);
        validateDeposit(transferAmount,receiverCust);
    }

    public static boolean within24Hours(Instant then){
        Instant now = Instant.now();
        Instant twentyFourHoursEarlier = now.minus( 24 , ChronoUnit.HOURS );
        // Is that moment (a) not before 24 hours ago, AND (b) before now (not in the future)?
        return ( ! then.isBefore( twentyFourHoursEarlier ) ) &&  then.isBefore( now );
    }
}
