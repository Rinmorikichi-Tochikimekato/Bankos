package com.bankos;

import java.math.BigDecimal;
import java.time.Instant;

public class Account {
    private String name;
    private int id;
    private BigDecimal balance;
    private static int idCounter = 1000;

    private int depositCount;
    private int withdrawCount;

    private Instant depositTime;
    private Instant withdrawTime;

    public int getDepositCount() {
        return depositCount;
    }

    public void incrementDepositCount() {
        ++this.depositCount;
    }

    public int getWithdrawCount() {
        return withdrawCount;
    }

    public void incrementWithdrawCount() {
        ++this.withdrawCount;
    }

    public Instant getDepositTime() {
        return depositTime;
    }

    public void setDepositTime(Instant depositTime) {
        this.depositTime = depositTime;
    }

    public Instant getWithdrawTime() {
        return withdrawTime;
    }

    public void setWithdrawTime(Instant withdrawTime) {
        this.withdrawTime = withdrawTime;
    }

    public Account(String name){
        this.name = name;
        this.id = ++idCounter;
        this.withdrawTime = Instant.now();
        this.depositTime = Instant.now();
        this.balance = new BigDecimal("0");
    }

    public void resetWithdrawCount(){
        this.withdrawCount = 0;
    }

    public void resetDepositCount(){
        this.depositCount = 0;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "main.java.com.bankos.Account{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", balance=" + balance +
                '}';
    }
}
