package ch.tbz.lb2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

// LÖSUNG A4
@ExtendWith(MockitoExtension.class)      // sonst werden @Mock und @InjectMocks nicht befüllt
class A4_MockitoTest {

    @Mock
    AccountRepository repository;        // Ersatz für die Datenbank

    @Mock
    NotificationService notifier;        // Ersatz für den E-Mail-Versand

    @InjectMocks
    TransferService service;             // echtes Objekt (SUT), bekommt die Mocks im Konstruktor

    @Test
    void transferMovesMoneyAndNotifies() {
        Account muster = new Account(1, "Muster", 500);
        Account gates = new Account(2, "Gates", 100);
        // STUB: feste Antworten vorgeben (State testing)
        when(repository.findById(1)).thenReturn(Optional.of(muster));
        when(repository.findById(2)).thenReturn(Optional.of(gates));

        boolean result = service.transfer(1, 2, 200);

        assertTrue(result);
        assertEquals(300, muster.getBalance());
        assertEquals(300, gates.getBalance());
        // MOCK: prüfen, ob die Methoden aufgerufen wurden (Behavioral testing)
        verify(repository).save(muster);
        verify(repository).save(gates);
        verify(notifier).send(eq("Gates"), anyString());
    }

    @Test
    void transferFailsWhenNotEnoughMoney() {
        when(repository.findById(1)).thenReturn(Optional.of(new Account(1, "Muster", 50)));
        when(repository.findById(2)).thenReturn(Optional.of(new Account(2, "Gates", 100)));

        assertFalse(service.transfer(1, 2, 200));
        verify(repository, never()).save(any());
        verifyNoInteractions(notifier);
    }

    @Test
    void transferToUnknownAccountThrows() {
        when(repository.findById(1)).thenReturn(Optional.of(new Account(1, "Muster", 500)));
        when(repository.findById(99)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.transfer(1, 99, 100));
    }
}
