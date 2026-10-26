package ch.tbz.lb2;

/**
 * Schickt z.B. eine E-Mail. Im Test soll keine echte E-Mail rausgehen, deshalb wird es gemockt.
 */
public interface NotificationService {

    void send(String owner, String message);
}
