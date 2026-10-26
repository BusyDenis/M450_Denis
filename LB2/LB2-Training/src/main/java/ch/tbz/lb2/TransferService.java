package ch.tbz.lb2;

public class TransferService {

    private final AccountRepository repository;
    private final NotificationService notifier;

    public TransferService(AccountRepository repository, NotificationService notifier) {
        this.repository = repository;
        this.notifier = notifier;
    }

    /**
     * @return true, wenn überwiesen wurde; false, wenn zu wenig Geld da ist
     * @throws IllegalArgumentException wenn ein Konto nicht existiert
     */
    public boolean transfer(int fromId, int toId, double amount) {
        Account from = repository.findById(fromId)
                .orElseThrow(() -> new IllegalArgumentException("Konto " + fromId + " nicht gefunden"));
        Account to = repository.findById(toId)
                .orElseThrow(() -> new IllegalArgumentException("Konto " + toId + " nicht gefunden"));

        if (!from.withdraw(amount)) {
            return false;
        }
        to.deposit(amount);
        repository.save(from);
        repository.save(to);
        notifier.send(to.getOwner(), "Sie haben " + amount + " CHF erhalten");
        return true;
    }
}
