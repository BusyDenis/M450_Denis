package ch.tbz.lb2;

import java.util.Optional;

/**
 * Zugriff auf die Datenbank. Im Test gibt es keine echte DB, deshalb wird dieses Interface gemockt.
 */
public interface AccountRepository {

    Optional<Account> findById(int id);

    void save(Account account);
}
