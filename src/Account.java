import java.time.Instant;

public class Account {
    private String name;
    private int id;
    private int balance;
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

    Account(String name){
        this.name = name;
        balance = 0;
        this.id = ++idCounter;
        this.withdrawTime = Instant.now();
        this.depositTime = Instant.now();
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

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Account{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", balance=" + balance +
                '}';
    }
}
