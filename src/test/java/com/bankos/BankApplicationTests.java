package com.bankos;


import com.bankos.Exceptions.InsufficientFundsException;
import com.bankos.Exceptions.TransferConstraintsException;
import com.bankos.Exceptions.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class BankApplicationTests {
    private BankApplication bankApplicationTestObj;

    @BeforeEach
    public void setUp(){
        bankApplicationTestObj = new BankApplication();
    }

    //account creation test
    @Test
    void accountCreationTest() {
        int accountId = bankApplicationTestObj.create("Dummy Name");
        int checkAccountId = bankApplicationTestObj.findCustomerByName("Dummy Name");
        assertEquals(checkAccountId,accountId);
    }

    //find account test
    @Test
    void findAccountTest(){
            bankApplicationTestObj.create("Monkey Luffy");
            int checkAccountId = bankApplicationTestObj.findCustomerByName("Monkey Luffy");
            Account account = bankApplicationTestObj.findCustomer(checkAccountId);
            assertEquals("Monkey Luffy",account.getName());
    }

    @Test()
    void findAccountExceptionTest(){
        bankApplicationTestObj.create("Monkey Luffy");
        UserNotFoundException userNotFoundException = assertThrows(UserNotFoundException.class, () -> bankApplicationTestObj.findCustomer(1100));
        assertTrue(userNotFoundException.getMessage().contains("User Not found, Please check the account number"));
    }

    //deposit Test
    @Test
    void successfulDepositTest(){
        bankApplicationTestObj.create("Monkey Luffy");
        int checkAccountId = bankApplicationTestObj.findCustomerByName("Monkey Luffy");
        assertEquals(BigDecimal.valueOf(10000),bankApplicationTestObj.deposit(checkAccountId, BigDecimal.valueOf(10000)));
    }

    @Test
    void minDepositViolationTest(){
        bankApplicationTestObj.create("Monkey Luffy");
        int checkAccountId = bankApplicationTestObj.findCustomerByName("Monkey Luffy");
        TransferConstraintsException transferConstraintsException = assertThrows(TransferConstraintsException.class, () -> bankApplicationTestObj.deposit(checkAccountId,BigDecimal.valueOf(100)));
        assertTrue(transferConstraintsException.getMessage().contains("Minimum deposit amount is 500"));
    }

    @Test
    void maxDepositViolationTest(){
        bankApplicationTestObj.create("Monkey Luffy");
        int checkAccountId = bankApplicationTestObj.findCustomerByName("Monkey Luffy");
        TransferConstraintsException transferConstraintsException = assertThrows(TransferConstraintsException.class, () -> bankApplicationTestObj.deposit(checkAccountId,BigDecimal.valueOf(100000)));
        assertTrue(transferConstraintsException.getMessage().contains("Maximum deposit amount is 50000"));
    }

    @Test
    void maximumAllowedBalanceExceededTest(){
        bankApplicationTestObj.create("Monkey Luffy");
        int checkAccountId = bankApplicationTestObj.findCustomerByName("Monkey Luffy");
        assertEquals(BigDecimal.valueOf(40000),bankApplicationTestObj.deposit(checkAccountId, BigDecimal.valueOf(40000)));
        assertEquals(BigDecimal.valueOf(80000),bankApplicationTestObj.deposit(checkAccountId, BigDecimal.valueOf(40000)));
        TransferConstraintsException transferConstraintsException = assertThrows(TransferConstraintsException.class, () -> bankApplicationTestObj.deposit(checkAccountId,BigDecimal.valueOf(30000)));
        assertTrue(transferConstraintsException.getMessage().contains("Receiver could not accept these funds"));
    }

    //withdraw tests
    @Test
    void successfulWithdrawalTest(){
        bankApplicationTestObj.create("Monkey Luffy");
        int checkAccountId = bankApplicationTestObj.findCustomerByName("Monkey Luffy");
        assertEquals(BigDecimal.valueOf(10000),bankApplicationTestObj.deposit(checkAccountId, BigDecimal.valueOf(10000)));
        assertEquals(BigDecimal.valueOf(5000),bankApplicationTestObj.withdraw(checkAccountId, BigDecimal.valueOf(5000)));
    }

    @Test
    void minWithdrawalViolationTest(){
        bankApplicationTestObj.create("Monkey Luffy");
        int checkAccountId = bankApplicationTestObj.findCustomerByName("Monkey Luffy");
        assertEquals(BigDecimal.valueOf(10000),bankApplicationTestObj.deposit(checkAccountId, BigDecimal.valueOf(10000)));
        TransferConstraintsException transferConstraintsException = assertThrows(TransferConstraintsException.class, () -> bankApplicationTestObj.withdraw(checkAccountId,BigDecimal.valueOf(100)));
        assertTrue(transferConstraintsException.getMessage().contains("Minimum Withdrawal limit is 1000"));
    }

    @Test
    void maxWithdrawalViolationTest(){
        bankApplicationTestObj.create("Monkey Luffy");
        int checkAccountId = bankApplicationTestObj.findCustomerByName("Monkey Luffy");
        assertEquals(BigDecimal.valueOf(10000),bankApplicationTestObj.deposit(checkAccountId, BigDecimal.valueOf(10000)));
        TransferConstraintsException transferConstraintsException = assertThrows(TransferConstraintsException.class, () -> bankApplicationTestObj.withdraw(checkAccountId,BigDecimal.valueOf(30000)));
        assertTrue(transferConstraintsException.getMessage().contains("Maximum Withdrawal limit is 25000"));
    }

    @Test
    void insufficientFundsTest(){
        bankApplicationTestObj.create("Monkey Luffy");
        int checkAccountId = bankApplicationTestObj.findCustomerByName("Monkey Luffy");
        assertEquals(BigDecimal.valueOf(10000),bankApplicationTestObj.deposit(checkAccountId, BigDecimal.valueOf(10000)));
        InsufficientFundsException insufficientFundsException = assertThrows(InsufficientFundsException.class, () -> bankApplicationTestObj.withdraw(checkAccountId,BigDecimal.valueOf(20000)));
        assertTrue(insufficientFundsException.getMessage().contains("Insufficient Funds"));
    }

    //check Balance Tests
    @Test
    void checkAccountBalTest(){
        bankApplicationTestObj.create("Monkey Luffy");
        int checkAccountId = bankApplicationTestObj.findCustomerByName("Monkey Luffy");
        assertEquals(BigDecimal.valueOf(10000),bankApplicationTestObj.deposit(checkAccountId, BigDecimal.valueOf(10000)));
        assertEquals(BigDecimal.valueOf(10000),bankApplicationTestObj.getBalance(checkAccountId));
    }

    //transfer tests
    @Test
    void successfulTransferTest(){
        bankApplicationTestObj.create("Monkey Luffy");
        int checkAccountIdCustomer1 = bankApplicationTestObj.findCustomerByName("Monkey Luffy");
        assertEquals(BigDecimal.valueOf(10000),bankApplicationTestObj.deposit(checkAccountIdCustomer1, BigDecimal.valueOf(10000)));
        bankApplicationTestObj.create("Rob Marco");
        int checkAccountIdCustomer2 = bankApplicationTestObj.findCustomerByName("Rob Marco");

        assertEquals("Success",bankApplicationTestObj.transfer(
                checkAccountIdCustomer1,checkAccountIdCustomer2,BigDecimal.valueOf(5000)
        ));
        assertEquals(BigDecimal.valueOf(5000),bankApplicationTestObj.getBalance(checkAccountIdCustomer1));
        assertEquals(BigDecimal.valueOf(5000),bankApplicationTestObj.getBalance(checkAccountIdCustomer2));
    }

    @Test
    void minTransferLimitViolationTransferTest(){
        bankApplicationTestObj.create("Monkey Luffy");
        int checkAccountIdCustomer1 = bankApplicationTestObj.findCustomerByName("Monkey Luffy");
        assertEquals(BigDecimal.valueOf(10000),bankApplicationTestObj.deposit(checkAccountIdCustomer1, BigDecimal.valueOf(10000)));
        bankApplicationTestObj.create("Rob Marco");
        int checkAccountIdCustomer2 = bankApplicationTestObj.findCustomerByName("Rob Marco");

        TransferConstraintsException transferConstraintsException = assertThrows(TransferConstraintsException.class, () -> bankApplicationTestObj.transfer(checkAccountIdCustomer1,checkAccountIdCustomer2,BigDecimal.valueOf(100)));
        assertTrue(transferConstraintsException.getMessage().contains("Failure : Minimum withdrawal amount is 1000 for Customer: " + checkAccountIdCustomer2));
    }

    @Test
    void maxTransferLimitViolationTransferTest(){
        bankApplicationTestObj.create("Monkey Luffy");
        int checkAccountIdCustomer1 = bankApplicationTestObj.findCustomerByName("Monkey Luffy");
        assertEquals(BigDecimal.valueOf(10000),bankApplicationTestObj.deposit(checkAccountIdCustomer1, BigDecimal.valueOf(10000)));
        bankApplicationTestObj.create("Rob Marco");
        int checkAccountIdCustomer2 = bankApplicationTestObj.findCustomerByName("Rob Marco");

        TransferConstraintsException transferConstraintsException = assertThrows(TransferConstraintsException.class, () -> bankApplicationTestObj.transfer(checkAccountIdCustomer1,checkAccountIdCustomer2,BigDecimal.valueOf(100000)));
        assertTrue(transferConstraintsException.getMessage().contains("Failure : Maximum withdrawal amount is 25000 for Customer: " + checkAccountIdCustomer2));
    }

    @Test
    void insufficientBalanceTransferTest(){
        bankApplicationTestObj.create("Monkey Luffy");
        int checkAccountIdCustomer1 = bankApplicationTestObj.findCustomerByName("Monkey Luffy");
        assertEquals(BigDecimal.valueOf(10000),bankApplicationTestObj.deposit(checkAccountIdCustomer1, BigDecimal.valueOf(10000)));
        bankApplicationTestObj.create("Rob Marco");
        int checkAccountIdCustomer2 = bankApplicationTestObj.findCustomerByName("Rob Marco");
        TransferConstraintsException transferConstraintsException = assertThrows(TransferConstraintsException.class, () -> bankApplicationTestObj.transfer(checkAccountIdCustomer1,checkAccountIdCustomer2,BigDecimal.valueOf(12000)));
        assertTrue(transferConstraintsException.getMessage().contains("Failure : Insufficient Balance for transfer"));
    }

    //find account by Name test
    @Test
    void findCustomerByNameTest(){
        bankApplicationTestObj.create("Monkey Luffy");
        int checkAccountId = bankApplicationTestObj.findCustomerByName("Monkey Luffy");
        assertEquals("Monkey Luffy", bankApplicationTestObj.findCustomer(checkAccountId).getName());
    }

}
