package ch.tbz.lb2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// TODO A4: TransferService testen, OHNE echte Datenbank und OHNE echte Benachrichtigung.
//  AccountRepository und NotificationService sind Interfaces -> mit Mockito ersetzen.
//  1. Klasse mit der Mockito-Extension ausführen
//  2. repository und notifier als Mocks anlegen, service mit den Mocks erstellen lassen
//  3. Die drei Tests ausprogrammieren (Stub: when/thenReturn, Mock: verify)
class A4_MockitoTest {

    // TODO: Felder für repository, notifier und service

    @Test
    void transferMovesMoneyAndNotifies() {
        // Gegeben: Konto 1 (Muster, 500 CHF) und Konto 2 (Gates, 100 CHF) im Repository (Stub)
        // Wenn:    200 CHF von 1 nach 2 überweisen
        // Dann:    true, beide Kontostände 300, beide Konten gespeichert, Gates wurde benachrichtigt (verify)
        fail("TODO A4.1");
    }

    @Test
    void transferFailsWhenNotEnoughMoney() {
        // Konto 1 hat nur 50 CHF, es sollen 200 CHF überwiesen werden
        // Dann: false, nichts gespeichert (never), keine Benachrichtigung
        fail("TODO A4.2");
    }

    @Test
    void transferToUnknownAccountThrows() {
        // Konto 99 gibt es nicht (Optional.empty())
        // Dann: IllegalArgumentException
        fail("TODO A4.3");
    }
}
