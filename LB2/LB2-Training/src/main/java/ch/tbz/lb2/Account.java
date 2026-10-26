package ch.tbz.lb2;

public class Account {

    private final int id;
    private final String owner;
    private double balance;

    public Account(int id, String owner, double balance) {
        this.id = id;
        this.owner = owner;
        this.balance = balance;
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Betrag muss positiv sein");
        }
        balance += amount;
    }

    /**
     * @return true, wenn abgehoben wurde; false, wenn zu wenig Geld auf dem Konto ist
     */
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Betrag muss positiv sein");
        }
        if (amount > balance) {
            return false;
        }
        balance -= amount;
        return true;
    }

    public int getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public double getBalance() {
        return balance;
    }
}
