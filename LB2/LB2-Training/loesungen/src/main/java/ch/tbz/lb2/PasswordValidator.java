package ch.tbz.lb2;

// LÖSUNG A5 (nach Green + Refactor)
public class PasswordValidator {

    private static final int MIN_LENGTH = 8;

    public boolean isValid(String password) {
        if (password == null || password.length() < MIN_LENGTH) {
            return false;
        }
        if (password.contains(" ")) {
            return false;
        }
        boolean hasDigit = false;
        boolean hasUppercase = false;
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                hasDigit = true;
            }
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            }
        }
        return hasDigit && hasUppercase;
    }
}
